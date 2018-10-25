package com.mohamed14riad.baking.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import com.mohamed14riad.baking.R;
import com.mohamed14riad.baking.activities.StepDetailsActivity;
import com.mohamed14riad.baking.models.Step;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class StepDetailsFragment extends Fragment
        implements View.OnClickListener, ExoPlayer.EventListener {

    private ArrayList<Step> steps = null;
    private int stepPosition;
    private Step currentStep = null;
    private int index;

    private TextView stepDescription = null;
    private ImageButton next = null, previous = null;

    private SimpleExoPlayerView playerView = null;
    private SimpleExoPlayer exoPlayer = null;

    private MediaSessionCompat mediaSession = null;
    private PlaybackStateCompat.Builder stateBuilder = null;

    private NotificationManager notificationManager;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public static StepDetailsFragment newInstance(ArrayList<Step> steps, int position) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("steps", steps);
        args.putInt("step_position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList("steps");
            stepPosition = getArguments().getInt("step_position", -1);

            currentStep = steps.get(stepPosition);
            index = stepPosition;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        playerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);

        stepDescription = (TextView) rootView.findViewById(R.id.step_description);

        previous = (ImageButton) rootView.findViewById(R.id.skip_previous);
        previous.setOnClickListener(this);

        next = (ImageButton) rootView.findViewById(R.id.skip_next);
        next.setOnClickListener(this);

        initializePlayer();
        initializeMediaSession();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip_previous:
                if (index > 0) {
                    index--;
                    currentStep = steps.get(index);
                    initializeVideo();
                }
                break;
            case R.id.skip_next:
                if (index < steps.size() - 1) {
                    index++;
                    currentStep = steps.get(index);
                    initializeVideo();
                }
                break;
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void initializeVideo() {
        String userAgent = Util.getUserAgent(getActivity(), "baking");

        stepDescription.setText(currentStep.getDescription());

        Uri uri;
        if (currentStep.getVideoUrl().isEmpty()) {
            if (currentStep.getThumbnailUrl().isEmpty()) {
                uri = null;
                Toast.makeText(getActivity(), "No Video For This Step!", Toast.LENGTH_SHORT).show();
            } else {
                uri = Uri.parse(currentStep.getThumbnailUrl());
            }
        } else {
            uri = Uri.parse(currentStep.getVideoUrl());
        }

        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getActivity(), userAgent),
                new DefaultExtractorsFactory(), null, null);

        if (isConnected()) {
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            initializeVideo();
        }
    }

    private void initializeMediaSession() {
        String TAG = StepDetailsActivity.class.getSimpleName();
        mediaSession = new MediaSessionCompat(getActivity(), TAG);

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new MediaSessionCall());

        mediaSession.setActive(true);
    }

    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());

        int icon;
        String playPause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            playPause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            playPause = getString(R.string.play);
        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, playPause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(getActivity(),
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (getActivity(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (getActivity(), 0, new Intent(getActivity(), StepDetailsActivity.class), 0);

        builder.setContentTitle(getString(R.string.baking))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_music_note)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1));

        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void releasePlayer() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object o) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

    }

    @Override
    public void onLoadingChanged(boolean b) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }

        mediaSession.setPlaybackState(stateBuilder.build());
        showNotification(stateBuilder.build());
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPause() {
        super.onPause();

        mediaSession.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        initializeMediaSession();
        initializePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();

        releasePlayer();
        mediaSession.setActive(false);
    }

    private class MediaSessionCall extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();

            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();

            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();

            exoPlayer.seekTo(0);
        }
    }

    public class MediaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }
}
