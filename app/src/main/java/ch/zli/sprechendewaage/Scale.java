package ch.zli.sprechendewaage;

import static android.content.Context.SENSOR_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Scale extends Activity implements SensorEventListener {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> publisher;

    volatile TextView xGradient;
    volatile TextView zGradient;
    volatile TextView degree;
    volatile ImageView circle;
    AppCompatActivity context;

    volatile float[] accelerometerValues;
    float x;
    float y;
    float z;
    Boolean isActive = false;

    float lastXCircleValue = 0;
    float lastYCircleValue = 0;
    float lastDegree = 0;
    int degreeCounter = 0;

    public Sensor mySensor;
    private SensorManager mySensorManager;

    public Scale(TextView xGradient, TextView zGradient, TextView degree, ImageView circle, AppCompatActivity context) {
        this.xGradient = xGradient;
        this.zGradient = zGradient;
        this.degree = degree;
        this.context = context;
        this.circle = circle;

        initiateSensor();
    }

    private void initiateSensor() {
        mySensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }

    @SuppressLint("SetTextI18n")
    private void doIt() {
        updateXandZValues();
        if (isActive) {
            double angle = (Math.atan(y/z)) * 57.29;
            float degreeValue = Math.round(angle*100)/100;
            degree.setText(Double.toString(degreeValue));

            if (lastDegree > (degreeValue - 2) && lastDegree < degreeValue + 2) {
                degreeCounter ++;
                System.out.println(degreeCounter);
            } else {
                degreeCounter = 0;
            }
            lastDegree = degreeValue;
        }
    }

    public void pause() {
        isActive = false;
    }

    public void resume() {
        isActive = true;
    }

    public void stop() {
        executor.shutdownNow();
    }

    @SuppressLint("SetTextI18n")
    private void updateXandZValues() {
        xGradient.setText(Double.toString(Math.round(y*100.0)/100.0));
        zGradient.setText(Double.toString(Math.round(x*100.0)/100.0));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = sensorEvent.values;
            x = accelerometerValues[0];
            y = accelerometerValues[1];
            z = accelerometerValues[2];

            doIt();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
