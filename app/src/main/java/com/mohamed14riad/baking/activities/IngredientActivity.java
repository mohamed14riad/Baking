package com.mohamed14riad.baking.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import com.mohamed14riad.baking.R;
import com.mohamed14riad.baking.adapters.IngredientsAdapter;
import com.mohamed14riad.baking.models.Ingredient;

public class IngredientActivity extends AppCompatActivity {

    private ArrayList<Ingredient> ingredients = null;

    private RecyclerView ingredientList = null;
    private IngredientsAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        if (getIntent() != null) {
            ingredients = getIntent().getParcelableArrayListExtra("ingredients");
        } else if (savedInstanceState != null) {
            ingredients = savedInstanceState.getParcelableArrayList("ingredients");
        }

        ingredientList = (RecyclerView) findViewById(R.id.ingredient_list);
        adapter = new IngredientsAdapter(ingredients, this);
        ingredientList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ingredientList.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("ingredients", ingredients);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ingredients = savedInstanceState.getParcelableArrayList("ingredients");
    }
}
