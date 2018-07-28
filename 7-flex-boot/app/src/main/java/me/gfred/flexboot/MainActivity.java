package me.gfred.flexboot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.hello_world_tv)
    TextView helloWorld;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helloWorld.setText("We dey pivot kraaa");

    }
}
