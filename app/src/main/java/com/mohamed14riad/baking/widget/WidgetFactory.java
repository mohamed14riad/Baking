package com.mohamed14riad.baking.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import com.mohamed14riad.baking.R;
import com.mohamed14riad.baking.data.DatabaseHelper;
import com.mohamed14riad.baking.models.Ingredient;

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private int index;

    private DatabaseHelper databaseHelper;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public WidgetFactory(Context context) {
        this.context = context;
        this.index = 3;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public void onCreate() {
        ingredients = databaseHelper.getIngredients(index);
    }

    @Override
    public void onDataSetChanged() {
        ingredients.clear();
        ingredients = databaseHelper.getIngredients(index);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_row_item);
        remoteViews.setTextViewText(R.id.widget_ingredient_description, ingredients.get(position).getDescription());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
