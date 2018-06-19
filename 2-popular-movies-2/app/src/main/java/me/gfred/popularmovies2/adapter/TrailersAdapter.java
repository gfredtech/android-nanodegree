package me.gfred.popularmovies2.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.model.Trailers;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyViewHolder> {
    private static final String YOUTUBE_THUMBNAIL_BASE_URL= "http://img.youtube.com/vi/";
    private static final String YOUTUBE_THUMBNAIL_PARAM = "/0.jpg";

    private Context mContext;
    private List<Trailers> mTrailers;

    final private TrailerClickListener mTrailerClickListener;

    public TrailersAdapter(Context context, TrailerClickListener mTrailerClickListener) {
        this.mContext = context;
        this.mTrailerClickListener = mTrailerClickListener;
    }

    public void setTrailers(List<Trailers> trailers) {
        this.mTrailers = trailers;
    }


    public interface TrailerClickListener {
        void onTrailerClicked(String url);
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

        String title = mTrailers.get(position).getName();
        String imageURL = YOUTUBE_THUMBNAIL_BASE_URL + mTrailers.get(position).getKey() + YOUTUBE_THUMBNAIL_PARAM;
        holder.trailerTextView.setText(title);
        Picasso.with(mContext)
                .load(imageURL)
                .into(holder.trailerThumbnail);

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView trailerTextView;
       ImageView trailerThumbnail;

        MyViewHolder(View itemView) {
            super(itemView);
            trailerTextView = itemView.findViewById(R.id.trailer_tv);
            trailerThumbnail = itemView.findViewById(R.id.trailer_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           String url =  mTrailers.get(getAdapterPosition()).getKey();

           mTrailerClickListener.onTrailerClicked(url);
        }
    }
}
