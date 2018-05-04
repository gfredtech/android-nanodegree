package me.gfred.bakingapp.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    private Step mSteps;


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

    public OnNavigationClickListener mCallback;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        long currentPosition = -1L;

        if(savedInstanceState != null) {

            mSteps = savedInstanceState.getParcelable("steps");
            if(savedInstanceState.getLong("position") != 0) currentPosition = savedInstanceState.getLong("position");
        }

        setViewElements();

        if(currentPosition != -1L && notAvailableImage.getVisibility() == View.INVISIBLE) {
            player.seekTo(currentPosition);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("steps", mSteps);

       if(player != null)
           outState.putLong("position", player.getCurrentPosition());
    }

    public void setStep(Step steps) {
        this.mSteps = steps;
    }

    void setViewElements() {
        description.setText(mSteps.getDescription());
        String x = mSteps.getVideoURL();

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
       mCallback.onNavigationClicked(true);
    }

    @OnClick(R.id.button_previous)
    public void previousClick() {
        mCallback.onNavigationClicked(false);
    }

    public void setButtonsVisibility(int index, int stepSize) {

    }

    public void enablePreviousButton(boolean enable) {
        buttonPrevious.setClickable(enable);
        buttonPrevious.setEnabled(enable);
    }

    public void enableNextButton(boolean enable) {
        buttonNext.setClickable(enable);
        buttonNext.setEnabled(enable);
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
            player.setPlayWhenReady(true);
        }
    }

    public interface OnNavigationClickListener {
        void onNavigationClicked(boolean next);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnNavigationClickListener) context;
        }catch (ClassCastException e) {
            e.printStackTrace();
            Log.e("StepFragment", "Class must implement OnNavigationClickListener interface");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player != null) {
            player.stop();
            player.release();
            player = null;

        }
    }
}
