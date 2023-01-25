package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeadActivity extends AppCompatActivity implements View.OnClickListener {

    private Button BT_Rejouer;
    private Button BT_Quitter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dead);

        // Récupération des informations de la GameActivity et affichage du nombre de pomme(s) mangée(s)
        TextView TV_Pomme = findViewById(R.id.TV_Pomme);
        Intent receiveIntent = this.getIntent();
        int nbrPomme = receiveIntent.getIntExtra("pommeMange", 0);
        String nbrPommeString = String.valueOf(nbrPomme);

        TV_Pomme.setText(String.format("Vous avez mangé %s pomme(s)", nbrPommeString));

        intent = new Intent(this, GameActivity.class);
        BT_Rejouer = findViewById(R.id.BT_Rejouer);
        BT_Quitter = findViewById(R.id.BT_Quitter);
        BT_Rejouer.setOnClickListener(this);
        BT_Quitter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == BT_Rejouer) {
            startActivity(intent);
        } else if (view == BT_Quitter) {
            finish();
        }
    }
}