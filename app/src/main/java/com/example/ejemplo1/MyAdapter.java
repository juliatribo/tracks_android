package com.example.ejemplo1;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Track> values;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }


    public void add(int position, Track item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    public MyAdapter(List<Track> myDataset) {
        values = myDataset;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = values.get(position).getTitle();
        final String singer = values.get(position).getSinger();
        final String id = values.get(position).getId();
        holder.txtHeader.setText("Song: " + name);
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.clickedSong = name;
                MainActivity.clickedArtist = singer;
                MainActivity.clickedID = id;
                MainActivity.ClickedSong();
            }
        });

        holder.txtFooter.setText("Author: " + singer);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }



}
