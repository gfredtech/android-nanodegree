package me.gfred.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.model.Step;

public class StepFragment extends Fragment {

    private List<Step> mSteps;
    private int index;
    private Context mContext;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.video_view)
    ImageView imageView;

    @BindView(R.id.button_previous)
    Button buttonPrevious;

    @BindView(R.id.button_next)
    Button buttonNext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);

        description.setText(mSteps.get(index).getDescription());
        if(index == 0) {
            buttonPrevious.setClickable(false);
            buttonPrevious.setEnabled(false);
        } else if(index + 1 >= mSteps.size()) {
            buttonNext.setClickable(false);
            buttonNext.setEnabled(false);
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    description.setText(mSteps.get(++index).getDescription());

                    if(index + 1 >= mSteps.size()) {
                        buttonNext.setEnabled(false);
                        buttonNext.setClickable(false);
                    }

                    if(!buttonPrevious.isEnabled()) {
                        buttonPrevious.setEnabled(true);
                        buttonPrevious.setClickable(true);
                    }
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                description.setText(mSteps.get(--index).getDescription());
                if(index - 1 < 0) {
                    buttonPrevious.setEnabled(false);
                    buttonPrevious.setClickable(false);
                }
                if(!buttonNext.isEnabled()) {
                    buttonNext.setClickable(true);
                    buttonNext.setEnabled(true);
                }
            }
        });
        return rootView;
    }


    public void setArgs(int index, List<Step> steps, Context mContext) {
        this.index = index;
        this.mSteps = steps;
        this.mContext = mContext;
    }


}
