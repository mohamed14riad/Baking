package com.mohamed14riad.baking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import com.mohamed14riad.baking.R;
import com.mohamed14riad.baking.fragments.StepDetailsFragment;
import com.mohamed14riad.baking.fragments.StepsFragment;
import com.mohamed14riad.baking.models.Ingredient;
import com.mohamed14riad.baking.models.Step;

public class StepsActivity extends AppCompatActivity
        implements StepsFragment.OnFragmentInteractionListener {

    public static final String STEPS_FRAGMENT = "StepsFragment";
    public static final String DETAILS_FRAGMENT = "DetailsFragment";
    public static boolean twoPaneMode;

    private ArrayList<Ingredient> ingredients = null;
    private ArrayList<Step> steps = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        if (getIntent() != null) {
            ingredients = getIntent().getParcelableArrayListExtra("ingredients");
            steps = getIntent().getParcelableArrayListExtra("steps");
        } else if (savedInstanceState != null) {
            ingredients = savedInstanceState.getParcelableArrayList("ingredients");
            steps = savedInstanceState.getParcelableArrayList("steps");
        }

        if (steps != null && ingredients != null) {
            StepsFragment stepsFragment = StepsFragment.newInstance(steps);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_listing, stepsFragment, STEPS_FRAGMENT)
                    .commit();

            if (findViewById(R.id.step_details_container) != null) {
                twoPaneMode = true;
            } else {
                twoPaneMode = false;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("ingredients", ingredients);
        outState.putParcelableArrayList("steps", steps);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ingredients = savedInstanceState.getParcelableArrayList("ingredients");
        steps = savedInstanceState.getParcelableArrayList("steps");
    }

    @Override
    public void fragmentOnClick(View view) {
        switch (view.getId()) {
            case R.id.ingredient_fab:
                Intent intent = new Intent(this, IngredientActivity.class);
                intent.putParcelableArrayListExtra("ingredients", ingredients);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void fragmentOnItemClick(int position) {
        if (twoPaneMode) {
            loadDetailsFragment(position);
        } else {
            startDetailsActivity(position);
        }
    }

    private void loadDetailsFragment(int position) {
        StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(steps, position);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.step_details_container, stepDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }

    private void startDetailsActivity(int position) {
        Intent intent = new Intent(this, StepDetailsActivity.class);
        intent.putParcelableArrayListExtra("steps", steps);
        intent.putExtra("step_position", position);
        startActivity(intent);
    }
}
