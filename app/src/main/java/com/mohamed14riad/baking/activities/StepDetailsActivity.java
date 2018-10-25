package com.mohamed14riad.baking.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import com.mohamed14riad.baking.R;
import com.mohamed14riad.baking.fragments.StepDetailsFragment;
import com.mohamed14riad.baking.models.Step;

public class StepDetailsActivity extends AppCompatActivity {

    private ArrayList<Step> steps = null;
    private int stepPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        if (getIntent() != null) {
            steps = getIntent().getParcelableArrayListExtra("steps");
            stepPosition = getIntent().getIntExtra("step_position", -1);
        } else if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList("steps");
            stepPosition = savedInstanceState.getInt("step_position", -1);
        }

        if (steps != null && stepPosition != -1) {
            StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(steps, stepPosition);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.step_details_container, stepDetailsFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("steps", steps);
        outState.putInt("step_position", stepPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        steps = savedInstanceState.getParcelableArrayList("steps");
        stepPosition = savedInstanceState.getInt("step_position", -1);
    }
}
