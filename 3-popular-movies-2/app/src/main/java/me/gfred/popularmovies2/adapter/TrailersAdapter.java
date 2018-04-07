package me.gfred.popularmovies2.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import me.gfred.popularmovies2.R;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyViewHolder> {

    Context mContext;
    List<Pair<String, URL>> mTrailers;

    public TrailersAdapter(Context context, List<Pair<String, URL>> trailers) {
        this.mContext = context;
        this.mTrailers = trailers;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardview_trailers, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.trailerTextView.setText(mTrailers.get(position).first);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
       TextView trailerTextView;

        MyViewHolder(View itemView) {
            super(itemView);
            trailerTextView = itemView.findViewById(R.id.trailer_tv);
        }
    }
}
