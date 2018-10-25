package com.mohamed14riad.baking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    @SerializedName("id")
    @Expose
    private int recipeId;

    @SerializedName("name")
    @Expose
    private String recipeName;

    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> recipeIngredients = null;

    @SerializedName("steps")
    @Expose
    private ArrayList<Step> recipeSteps = null;

    @SerializedName("servings")
    @Expose
    private int recipeServings;

    @SerializedName("image")
    @Expose
    private String recipeImage;

    public Recipe() {
    }

    public Recipe(int recipeId, String recipeName, ArrayList<Ingredient> recipeIngredients, ArrayList<Step> recipeSteps, int recipeServings, String recipeImage) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
        this.recipeServings = recipeServings;
        this.recipeImage = recipeImage;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public ArrayList<Ingredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(ArrayList<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public ArrayList<Step> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(ArrayList<Step> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public int getRecipeServings() {
        return recipeServings;
    }

    public void setRecipeServings(int recipeServings) {
        this.recipeServings = recipeServings;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.recipeId);
        dest.writeString(this.recipeName);
        dest.writeTypedList(this.recipeIngredients);
        dest.writeTypedList(this.recipeSteps);
        dest.writeInt(this.recipeServings);
        dest.writeString(this.recipeImage);
    }

    private Recipe(Parcel in) {
        this.recipeId = in.readInt();
        this.recipeName = in.readString();
        this.recipeIngredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.recipeSteps = in.createTypedArrayList(Step.CREATOR);
        this.recipeServings = in.readInt();
        this.recipeImage = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
