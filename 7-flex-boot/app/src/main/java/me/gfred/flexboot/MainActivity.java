package me.gfred.flexboot;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    SensorEvent sensorEvent;
    SensorManager sensorManager;
    Sensor sensor;
    boolean isSensorAvailable;
    @BindView(R.id.steps_tv)
    TextView stepsTaken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            isSensorAvailable = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null;

            if (isSensorAvailable) {
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            } else {
                AlertDialog dialog = stepSensorNotPresentDialog();
                dialog.show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        stepsTaken.setText(getString(R.string.steps_description) + String.valueOf(sensorEvent.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isSensorAvailable)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isSensorAvailable)
            sensorManager.unregisterListener(this);
    }

    AlertDialog stepSensorNotPresentDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "Sorry, your phone doesn't have the required sensor hardware for the pedometer to function";
        builder.setMessage(message);

        builder.setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        return builder.create();
    }
}
