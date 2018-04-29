package me.gfred.bakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.model.Step;

public class StepFragment extends Fragment {

    private List<Step> mSteps;
    private int index;
    private Context mContext;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.video_view)
    PlayerView videoView;

    @BindView(R.id.button_previous)
    Button buttonPrevious;

    @BindView(R.id.button_next)
    Button buttonNext;

    SimpleExoPlayer player;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        long currentPosition = -1L;

        if(savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            index = savedInstanceState.getInt("index");
             currentPosition = savedInstanceState.getLong("position");
        }

        setStuff(index);
        if(currentPosition != -1L) player.seekTo(currentPosition);

            if (index == 0) {
                buttonPrevious.setClickable(false);
                buttonPrevious.setEnabled(false);
            } else if (index + 1 == mSteps.size()) {
                buttonNext.setClickable(false);
                buttonNext.setEnabled(false);
            }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putParcelableArrayList("steps", new ArrayList<Parcelable>(){{
            addAll(mSteps);
        }});
        outState.putLong("position", player.getCurrentPosition());


    }

    public void setArgs(int index, List<Step> steps, Context mContext) {
        this.index = index;
        this.mSteps = steps;
        this.mContext = mContext;
    }

    void setStuff(int index) {
        description.setText(mSteps.get(index).getDescription());
        //TODO: video player
        String x = mSteps.get(index).getVideoURL();
        if(x != null && x.length() > 0) initializePlayer(Uri.parse(x));
    }

    @OnClick(R.id.button_next)
    public void nextClick() {
        index++;
        setStuff(index);


        if(index + 1 == mSteps.size()) {
            buttonNext.setEnabled(false);
            buttonNext.setClickable(false);
        }

        if(!buttonPrevious.isEnabled()) {
            buttonPrevious.setEnabled(true);
            buttonPrevious.setClickable(true);
        }
    }

    @OnClick(R.id.button_previous)
    public void previousClick() {
        index--;
        setStuff(index);
        if(index - 1 < 0) {
            buttonPrevious.setEnabled(false);
            buttonPrevious.setClickable(false);
        }
        if(!buttonNext.isEnabled()) {
            buttonNext.setClickable(true);
            buttonNext.setEnabled(true);
        }
    }

    void initializePlayer(Uri uri) {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create the player
        player =
                ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        videoView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "BakingApp"), bandMeter);
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
// Prepare the player with the source.
        player.prepare(videoSource);

    }

}
