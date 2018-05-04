package me.gfred.bakingapp.fragment;


import android.content.res.Configuration;
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
import android.widget.ImageView;
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

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.video_view)
    PlayerView videoView;

    @BindView(R.id.button_previous)
    Button buttonPrevious;

    @BindView(R.id.button_next)
    Button buttonNext;

    @BindView(R.id.not_available_iv)
    ImageView notAvailableImage;

    SimpleExoPlayer player;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        long currentPosition = -1L;

        if(savedInstanceState != null) {

            mSteps = savedInstanceState.getParcelableArrayList("steps");
            index = savedInstanceState.getInt("index", 0);
            if(savedInstanceState.getLong("position") != 0) currentPosition = savedInstanceState.getLong("position");
        }

        setViewElements();

        if(currentPosition != -1L && notAvailableImage.getVisibility() == View.INVISIBLE) {
            player.seekTo(currentPosition);
        }

            if (index == 0) {
                buttonPrevious.setClickable(false);
                buttonPrevious.setEnabled(false);
            } else if (index == mSteps.size() - 1) {
                buttonNext.setClickable(false);
                buttonNext.setEnabled(false);
            }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mSteps);

       if(player != null) outState.putLong("position", player.getCurrentPosition());
    }

    public void setStepAndIndex(int index, List<Step> steps) {
        this.index = index;
        this.mSteps = steps;
    }

    void setViewElements() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            description.setText(mSteps.get(index).getDescription());
            description.setVisibility(View.VISIBLE);
        } else {
            description.setVisibility(View.INVISIBLE);
        }
        String x = mSteps.get(index).getVideoURL();
        if(x != null && x.length() > 0) {
            videoView.setVisibility(View.VISIBLE);
            notAvailableImage.setVisibility(View.INVISIBLE);
            initializePlayer(Uri.parse(x));

        }
        else {
            videoView.setVisibility(View.INVISIBLE);
            notAvailableImage.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.button_next)
    public void nextClick() {
        index++;
        setViewElements();

        if(index == mSteps.size() - 1) {
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
        setViewElements();
        if(index == 0) {
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

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        videoView.setPlayer(player);

        DefaultBandwidthMeter bandMeter = new DefaultBandwidthMeter();

        if(getActivity() != null) {
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "BakingApp"), bandMeter);

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);

            player.prepare(videoSource);
        }
    }
}
