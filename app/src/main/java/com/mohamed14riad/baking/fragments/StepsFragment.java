package com.mohamed14riad.baking.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamed14riad.baking.R;
import com.mohamed14riad.baking.adapters.StepsAdapter;
import com.mohamed14riad.baking.models.Step;

import java.util.ArrayList;

import static com.mohamed14riad.baking.activities.StepsActivity.DETAILS_FRAGMENT;
import static com.mohamed14riad.baking.activities.StepsActivity.twoPaneMode;

public class StepsFragment extends Fragment
        implements View.OnClickListener, StepsAdapter.OnItemClickListener {

    private ArrayList<Step> steps = null;

    private RecyclerView stepsList = null;
    private StepsAdapter stepsAdapter = null;

    private FloatingActionButton ingredientFab = null;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void fragmentOnClick(View view);

        void fragmentOnItemClick(int position);
    }

    public StepsFragment() {
        // Required empty public constructor
    }

    public static StepsFragment newInstance(ArrayList<Step> steps) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("steps", steps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList("steps");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        stepsList = (RecyclerView) rootView.findViewById(R.id.steps_list);

        ingredientFab = (FloatingActionButton) rootView.findViewById(R.id.ingredient_fab);
        ingredientFab.setOnClickListener(this);

        stepsAdapter = new StepsAdapter(steps, getActivity(), this);
        stepsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        stepsList.setAdapter(stepsAdapter);

        if (twoPaneMode) {
            loadDetailsFragment(0);
        }

        return rootView;
    }

    private void loadDetailsFragment(int position) {
        StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(steps, position);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.step_details_container, stepDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }

    @Override
    public void onClick(View view) {
        mListener.fragmentOnClick(view);
    }

    @Override
    public void onItemClick(int position) {
        mListener.fragmentOnItemClick(position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
