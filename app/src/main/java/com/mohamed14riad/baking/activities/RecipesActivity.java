package com.mohamed14riad.baking.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import com.mohamed14riad.baking.R;
import com.mohamed14riad.baking.adapters.RecipesAdapter;
import com.mohamed14riad.baking.api.ApiClient;
import com.mohamed14riad.baking.api.ApiService;
import com.mohamed14riad.baking.data.DatabaseHelper;
import com.mohamed14riad.baking.models.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity
        implements RecipesAdapter.OnItemClickListener {

    private CountingIdlingResource idlingResource = new CountingIdlingResource("RECIPES_DATA");

    private ApiService apiService = null;

    private ArrayList<Recipe> recipes = null;
    private ArrayList<String> recipesNames = null;

    private RecyclerView recipesList = null;
    private ProgressBar progressBar = null;
    private RecipesAdapter adapter = null;

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        initializeViews();

        idlingResource.increment();

        databaseHelper = new DatabaseHelper(this);

        apiService = ApiClient.getClient().create(ApiService.class);

        if (isConnected()) {
            progressBar.setVisibility(View.VISIBLE);

            Call<ArrayList<Recipe>> recipesCall = apiService.getRecipes();
            recipesCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                    if (response.isSuccessful()) {
                        recipes = response.body();

                        databaseHelper.deleteAll();
                        databaseHelper.insertRecipes(recipes);

                        recipesNames = new ArrayList<>();
                        for (int i = 0; i < recipes.size(); i++) {
                            recipesNames.add(recipes.get(i).getRecipeName());
                        }

                        adapter.setNames(recipesNames);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        recipes = databaseHelper.getRecipes();

                        if (recipes.isEmpty()) {
                            Toast.makeText(RecipesActivity.this, "No Data Stored!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            recipesNames = new ArrayList<>();
                            for (int i = 0; i < recipes.size(); i++) {
                                recipesNames.add(recipes.get(i).getRecipeName());
                            }

                            adapter.setNames(recipesNames);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    idlingResource.decrement();
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    Toast.makeText(RecipesActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    idlingResource.decrement();
                }
            });
        } else {
            progressBar.setVisibility(View.VISIBLE);

            recipes = databaseHelper.getRecipes();

            if (recipes.isEmpty()) {
                Toast.makeText(RecipesActivity.this, "No Data Stored!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            } else {
                recipesNames = new ArrayList<>();
                for (int i = 0; i < recipes.size(); i++) {
                    recipesNames.add(recipes.get(i).getRecipeName());
                }

                adapter.setNames(recipesNames);
                progressBar.setVisibility(View.GONE);
            }

            idlingResource.decrement();
        }
    }

    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

    private void initializeViews() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            columns = 1;
        } else {
            columns = 2;
        }

        recipesList = (RecyclerView) findViewById(R.id.recipes_list);
        recipesList.setLayoutManager(new GridLayoutManager(this, columns));
        adapter = new RecipesAdapter(this, this);
        recipesList.setAdapter(adapter);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, StepsActivity.class);
        intent.putParcelableArrayListExtra("ingredients", recipes.get(position).getRecipeIngredients());
        intent.putParcelableArrayListExtra("steps", recipes.get(position).getRecipeSteps());
        startActivity(intent);
    }
}
