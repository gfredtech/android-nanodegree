package me.gfred.bakingapp.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.model.Step;

public class StepFragment extends Fragment {

    static long currentPosition = 0;
    static boolean setPlayWhenReady;
    public OnNavigationClickListener mCallback;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.video_view)
    PlayerView videoView;
    @BindView(R.id.button_previous)
    Button buttonPrevious;
    @BindView(R.id.button_next)
    Button buttonNext;
    @BindView(R.id.thumbnail_image)
    ImageView thumbnailImage;
    SimpleExoPlayer player;
    private Step mStep;
    private int mSize;
    private int mStepIndex;
    private boolean reset;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mSize = savedInstanceState.getInt("size");
            mStep = savedInstanceState.getParcelable("step");
            mStepIndex = savedInstanceState.getInt("stepIndex");
            currentPosition = savedInstanceState.getLong("position");
            setPlayWhenReady = savedInstanceState.getBoolean("setPlayWhenReady");
        }

        setButtonsVisibility(mStepIndex, mSize);
        setViewElements();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("size", mSize);
        outState.putParcelable("step", mStep);
        outState.putInt("stepIndex", mStepIndex);

        if (player != null) {
            currentPosition = player.getCurrentPosition();

            outState.putLong("position",
                    currentPosition);
            outState.putBoolean("setPlayWhenReady", player.getPlayWhenReady());
        }

    }

    public void setStep(Step step, int stepIndex, int size) {
        this.mStep = step;
        this.mStepIndex = stepIndex;
        this.mSize = size;
    }

    void setViewElements() {
        description.setText(mStep.getDescription());
        String videoURL = mStep.getVideoURL();

        if (videoURL != null && videoURL.length() > 0) {
            videoView.setVisibility(View.VISIBLE);
            thumbnailImage.setVisibility(View.INVISIBLE);
            initializePlayer(Uri.parse(videoURL));

        } else {
            videoView.setVisibility(View.INVISIBLE);
            String thumbnailURL = mStep.getThumbnailURL();

            if (thumbnailURL != null && thumbnailURL.length() > 0) {
                Picasso.get().
                        load(thumbnailURL).error(R.drawable.not_available).
                        into(thumbnailImage);
            } else
                Picasso.get().
                        load(R.drawable.not_available).
                        into(thumbnailImage);


            thumbnailImage.setVisibility(View.VISIBLE);
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

    public void setButtonsVisibility(int stepIndex, int stepSize) {
        if (stepIndex == stepSize - 1) {
            enableNextButton(false);
            enablePreviousButton(true);
            return;
        }
        if (stepIndex == 0) {
            enablePreviousButton(false);
            enableNextButton(true);
        } else {
            enableNextButton(true);
            enablePreviousButton(true);
        }

    }

    private void enablePreviousButton(boolean enable) {
        buttonPrevious.setClickable(enable);
        buttonPrevious.setEnabled(enable);
    }

    private void enableNextButton(boolean enable) {
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

        if (getActivity() != null) {
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "BakingApp"), bandMeter);

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);

            player.prepare(videoSource);
            if (!reset) {
                player.seekTo(currentPosition);
                player.setPlayWhenReady(setPlayWhenReady);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnNavigationClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickedListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            currentPosition = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null && !TextUtils.isEmpty(mStep.getVideoURL())) {
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            currentPosition = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;

        }
    }

    public void resetPosition() {
        this.reset = true;
    }

    public interface OnNavigationClickListener {
        void onNavigationClicked(boolean next);
    }
}
