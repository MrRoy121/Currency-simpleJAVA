package com.example.currency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;

public class countrylist_adapter extends RecyclerView.Adapter<countrylist_adapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<countrylist> coursesArrayList;
    private Context context;

    // creating constructor for our adapter class
    public countrylist_adapter(ArrayList<countrylist> coursesArrayList, Context context) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public countrylist_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull countrylist_adapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        countrylist courses = coursesArrayList.get(position);
        holder.title.setText(courses.getCname());
        holder.tname.setText(courses.getCcur());
        holder.section.setText(courses.getMcap());
        Picasso.get().load(courses.getCimage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return coursesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView title;
        private final TextView tname;
        private final TextView section;
        private final ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            title = itemView.findViewById(R.id.cname);
            tname = itemView.findViewById(R.id.ccur);
            section = itemView.findViewById(R.id.mcap);
            img = itemView.findViewById(R.id.cimage);
        }
    }
}
