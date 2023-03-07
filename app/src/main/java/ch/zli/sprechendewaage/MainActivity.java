package ch.zli.sprechendewaage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button activate;
    TextView xGradient;
    TextView zGradient;
    TextView degree;
    ImageView circle;
    Scale scaleThreat;

    Boolean scaleIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            initVariables();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        initActivateButton();
    }

    @SuppressLint("SetTextI18n")
    private void initActivateButton() {
        activate.setOnClickListener(click -> {
            if (!scaleIsActive) {
                scaleIsActive = true;
                scaleThreat.resume();
                System.out.println("Starte Messung");
                activate.setText("Beende messung");
            } else {
                scaleIsActive = false;
                scaleThreat.pause();
                System.out.println("Messung beendet");
                activate.setText("starte messung");
            }
        });
    }

    private void initVariables() throws InterruptedException {
        activate = findViewById(R.id.button);
        xGradient = findViewById(R.id.xGradient);
        zGradient = findViewById(R.id.zGradient);
        degree = findViewById(R.id.gForce);
        circle = findViewById(R.id.imageView);
        scaleThreat = new Scale(xGradient, zGradient, degree, circle, this);
        scaleThreat.pause();
        scaleIsActive = false;
    }
}