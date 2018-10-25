package com.mohamed14riad.baking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.mohamed14riad.baking.R;
import com.mohamed14riad.baking.models.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> ingredients;
    private Context context;

    public IngredientsAdapter(ArrayList<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientViewHolder(LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.ingredientNumber.setText(String.valueOf(position + 1));
        holder.ingredientDescription.setText(ingredient.getDescription());
        holder.ingredientQuantity.setText("Quantity: ".concat(String.valueOf(ingredient.getQuantity())));
        holder.ingredientMeasure.setText("Measure: ".concat(ingredient.getMeasure()));
    }

    @Override
    public int getItemCount() {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0;
        } else {
            return ingredients.size();
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientNumber, ingredientDescription, ingredientQuantity, ingredientMeasure;

        IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientNumber = (TextView) itemView.findViewById(R.id.ingredient_number);
            ingredientDescription = (TextView) itemView.findViewById(R.id.ingredient_description);
            ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredient_quantity);
            ingredientMeasure = (TextView) itemView.findViewById(R.id.ingredient_measure);
        }
    }
}
