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
import com.mohamed14riad.baking.models.Step;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private ArrayList<Step> steps;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public StepsAdapter(ArrayList<Step> steps, Context context, OnItemClickListener onItemClickListener) {
        this.steps = steps;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepViewHolder(LayoutInflater.from(context).inflate(R.layout.steps_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = steps.get(position);

        holder.stepNumber.setText(String.valueOf(step.getStepId()));
        holder.stepShortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (steps == null || steps.isEmpty()) {
            return 0;
        } else {
            return steps.size();
        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView stepNumber, stepShortDescription;

        StepViewHolder(View itemView) {
            super(itemView);

            stepNumber = (TextView) itemView.findViewById(R.id.step_number);
            stepShortDescription = (TextView) itemView.findViewById(R.id.step_short_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
