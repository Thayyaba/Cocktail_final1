package com.example.apsdc.cocktail_final;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.DrinksViewHolder> {
    MainActivity mainActivity;
    List<DrinksModel> drinksModels;
    List<IngrediantsModel> ingrediantsModels;

    public DrinksAdapter(MainActivity mainActivity, List<DrinksModel> drinksModels) {
        this.drinksModels = drinksModels;
        this.mainActivity=mainActivity;
    }



    @NonNull
    @Override
    public DrinksAdapter.DrinksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mainActivity).inflate(R.layout.image, viewGroup, false);
        return new DrinksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinksAdapter.DrinksViewHolder drinksViewHolder, int i) {
        Picasso.with(mainActivity).load(drinksModels.get(i).getDrinkImage()).placeholder(R.drawable.ic_launcher_background).into(drinksViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return drinksModels.size();
    }

    public class DrinksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public DrinksViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_recycle);
itemView.setOnClickListener( this);
        }

        @Override
        public void onClick(View v) {
            int positon = getAdapterPosition();
            Intent intent = new Intent(mainActivity, ActivityDetails.class);
            intent.putExtra("DrinkTitle", drinksModels.get(positon).getDrinkTitle());
            intent.putExtra("DrinkImage", drinksModels.get(positon).getDrinkImage());
            intent.putExtra("DrinkId", drinksModels.get(positon).getId());
            intent.putParcelableArrayListExtra("key", (ArrayList<? extends Parcelable>) drinksModels);
            mainActivity.startActivity(intent);

        }
    }
}