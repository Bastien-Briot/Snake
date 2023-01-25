package com.example.snake;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private ImageView IV_Snake;
    private ImageView imageView;
    private SensorManager mgr;
    private Sensor gravitySensor;
    private int screenWidth, screenHeight;
    private int pommeMange = 0;
    Intent DeadActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Récupère la taille de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        imageView = findViewById(R.id.IV_Pomme);
        IV_Snake = findViewById(R.id.IV_Snake);
        DeadActivity = new Intent(this, DeadActivity.class);

        // Genère la première pomme
        genererPomme(imageView);

        // Initisalisation du gravity sensor
        mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravitySensor = mgr.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mgr.registerListener(gravitySensorListener, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Récuoère les données du gravity sensor
     */
    private final SensorEventListener gravitySensorListener = new SensorEventListener() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {

                // Récupère les valeurs du gravity Sensor
                float y = event.values[0];
                float x = event.values[1];

                if (x > 0.5 && y < 0.5 && y > -0.5) {
                    // Déplacement vers la droite
                    if (IV_Snake.getX() < screenWidth - IV_Snake.getWidth()) {
                        IV_Snake.setX((float) (IV_Snake.getX() + 10));
                    }
                } else if (x < -0.5 && y < 0.5 && y > -0.5) {
                    // Déplacement vers la gauche
                    if (IV_Snake.getX() > 0) {
                        IV_Snake.setX((float) (IV_Snake.getX() - 10));
                    }
                }

                if (y > 0.5 && x < 0.5 && x > -0.5) {
                    // Déplacement vers le bas
                    if (IV_Snake.getY() < screenHeight - IV_Snake.getHeight()) {
                        IV_Snake.setY((float) (IV_Snake.getY() + 10));
                    }
                } else if (y < -0.5 && x < 0.5 && x > -0.5) {
                    // Déplacement vers le haut
                    if (IV_Snake.getY() > 0) {
                        IV_Snake.setY((float) (IV_Snake.getY() - 10));
                    }
                }

                // Récupère les dimensions de l'image
                int imageViewWidth = IV_Snake.getWidth();
                int imageViewHeight = IV_Snake.getHeight();

                // Obtenir la position
                float imageViewX = IV_Snake.getX();
                float imageViewY = IV_Snake.getY();

                if (imageViewX < 0) {
                    dead(pommeMange);
                }
                if (imageViewY < -30) {
                    dead(pommeMange);
                }
                if (imageViewX + imageViewWidth > screenWidth) {
                    dead(pommeMange);
                }
                if (imageViewY + imageViewHeight > screenHeight) {
                    dead(pommeMange);
                }

            }
            /* Si la tête du serpent touche la pomme, on génère une nouvelle pomme */
            mangerPomme(imageView);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        mgr.registerListener(gravitySensorListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mgr.unregisterListener(gravitySensorListener);
    }

    /**
     * Affichage de l'écran de mort
     */
    public void dead(int pommeMange) {
        DeadActivity.putExtra("pommeMange", pommeMange);
        startActivity(DeadActivity);
    }

    /**
     * Genère une pomme aléatoirement sur la map
     * @param IV imageView de la pomme
     */
    public void genererPomme(ImageView IV) {
        Random random = new Random();
        List<Integer> random_x = Arrays.asList(200,400,600,800,1000);
        List<Integer> random_y = Arrays.asList(200,400,600);

        int xIndex = random_x.get(random.nextInt(random_x.size()));
        int yIndex = random_y.get(random.nextInt(random_y.size()));

        int imageIndex = random.nextInt(getResources().getStringArray(R.array.random_images).length);
        int imageId = getResources().obtainTypedArray(R.array.random_images).getResourceId(imageIndex, -1);
        IV.setImageResource(imageId);
        IV.setX(xIndex);
        IV.setY(yIndex);
    }

    /**
     * Appel la fonction genererPomme quand la pomme a été mangé et incrémente la variable pommeMange
     * @param IV imageView de la pomme
     */
    public void mangerPomme(ImageView IV) {
        float x1 = IV_Snake.getX(), y1 = IV_Snake.getY(), width1 = IV_Snake.getWidth(), height1 = IV_Snake.getHeight();
        float x2 = IV.getX(), y2 = IV.getY(), width2 = IV.getWidth(), height2 = IV.getHeight();

        if (x1 < x2 + width2 && x1 + width1 > x2 && y1 < y2 + height2 && y1 + height1 > y2) {
            genererPomme(IV);
            pommeMange++;
        }

    }

}


