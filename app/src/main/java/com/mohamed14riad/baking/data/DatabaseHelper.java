package com.mohamed14riad.baking.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import com.mohamed14riad.baking.models.Ingredient;
import com.mohamed14riad.baking.models.Recipe;
import com.mohamed14riad.baking.models.Step;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.IngredientEntry.CREATE_TABLE);
        db.execSQL(Contract.StepsEntry.CREATE_TABLE);
        db.execSQL(Contract.RecipesEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL(Contract.IngredientEntry.DROP_TABLE);
            db.execSQL(Contract.StepsEntry.DROP_TABLE);
            db.execSQL(Contract.RecipesEntry.DROP_TABLE);
            onCreate(db);
        }
    }

    public void insertRecipes(ArrayList<Recipe> recipes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            values.put(Contract.RecipesEntry.COLUMN_RECIPE_ID, recipe.getRecipeId());
            values.put(Contract.RecipesEntry.COLUMN_RECIPE_NAME, recipe.getRecipeName());
            values.put(Contract.RecipesEntry.COLUMN_RECIPE_SERVINGS, recipe.getRecipeServings());
            values.put(Contract.RecipesEntry.COLUMN_RECIPE_IMAGE, recipe.getRecipeImage());
            db.insert(Contract.RecipesEntry.TABLE_RECIPES, null, values);
            values.clear();

            ArrayList<Ingredient> ingredients = recipe.getRecipeIngredients();
            for (int j = 0; j < ingredients.size(); j++) {
                Ingredient ingredient = ingredients.get(j);
                values.put(Contract.IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());
                values.put(Contract.IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
                values.put(Contract.IngredientEntry.COLUMN_INGREDIENT_DESCRIPTION, ingredient.getDescription());
                values.put(Contract.IngredientEntry.COLUMN_RECIPE_ID, recipe.getRecipeId());
                db.insert(Contract.IngredientEntry.TABLE_INGREDIENT, null, values);
                values.clear();
            }

            ArrayList<Step> steps = recipe.getRecipeSteps();
            for (int k = 0; k < steps.size(); k++) {
                Step step = steps.get(k);
                values.put(Contract.StepsEntry.COLUMN_ID, step.getStepId());
                values.put(Contract.StepsEntry.COLUMN_SHORT_DESCRIPTION, step.getShortDescription());
                values.put(Contract.StepsEntry.COLUMN_DESCRIPTION, step.getDescription());
                values.put(Contract.StepsEntry.COLUMN_VIDEO_URL, step.getVideoUrl());
                values.put(Contract.StepsEntry.COLUMN_THUMBNAIL_URL, step.getThumbnailUrl());
                values.put(Contract.StepsEntry.COLUMN_RECIPE_ID, recipe.getRecipeId());
                db.insert(Contract.StepsEntry.TABLE_STEPS, null, values);
                values.clear();
            }

            values.clear();
        }
    }

    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Contract.RecipesEntry.TABLE_RECIPES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();

                recipe.setRecipeId(cursor.getInt(0));
                recipe.setRecipeName(cursor.getString(1));
                recipe.setRecipeServings(cursor.getInt(2));
                recipe.setRecipeImage(cursor.getString(3));

                recipe.setRecipeIngredients(getIngredients(recipe.getRecipeId()));
                recipe.setRecipeSteps(getSteps(recipe.getRecipeId()));

                recipes.add(recipe);
            } while (cursor.moveToNext());
        }

        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error : Close Cursor");
        }

        return recipes;
    }

    public ArrayList<Ingredient> getIngredients(int recipeId) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Contract.IngredientEntry.TABLE_INGREDIENT, null,
                Contract.IngredientEntry.COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();

                ingredient.setQuantity(cursor.getDouble(0));
                ingredient.setMeasure(cursor.getString(1));
                ingredient.setDescription(cursor.getString(2));

                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }

        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error : Close Cursor");
        }

        return ingredients;
    }

    public ArrayList<Step> getSteps(int recipeId) {
        ArrayList<Step> steps = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Contract.StepsEntry.TABLE_STEPS, null,
                Contract.StepsEntry.COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Step step = new Step();

                step.setStepId(cursor.getInt(0));
                step.setShortDescription(cursor.getString(1));
                step.setDescription(cursor.getString(2));
                step.setVideoUrl(cursor.getString(3));
                step.setThumbnailUrl(cursor.getString(4));

                steps.add(step);
            } while (cursor.moveToNext());
        }

        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error : Close Cursor");
        }

        return steps;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Contract.IngredientEntry.TABLE_INGREDIENT, null, null);
        db.delete(Contract.StepsEntry.TABLE_STEPS, null, null);
        db.delete(Contract.RecipesEntry.TABLE_RECIPES, null, null);
    }
}
