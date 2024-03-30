package com.example.cricket2024;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageButton IndianTeamImageButton, CustomTeamImageButton;
    Button quit, help;
    MediaPlayer clickSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IndianTeamImageButton = findViewById(R.id.ind_btn);
        CustomTeamImageButton = findViewById(R.id.cust_btn);
        quit = findViewById(R.id.quit);
        help = findViewById(R.id.help);
        clickSoundPlayer = MediaPlayer.create(this, R.raw.click_sound);


        IndianTeamImageButton.setOnClickListener(v -> {
            clickSoundPlayer.start();
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("team", "indian_team");
            startActivity(intent);
        });

        CustomTeamImageButton.setOnClickListener(v -> {
            clickSoundPlayer.start();
            Intent intent = new Intent(MainActivity.this, MainActivity1.class);
            startActivity(intent);
        });
        quit.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> {
            clickSoundPlayer.start();
            showTutorialDialog();
        });
    }
    private void showTutorialDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.play_tutorial, null);
        // Set tutorial text or other content here if needed
        builder.setView(dialogView)
                .setTitle("Game Tutorial")
                .setPositiveButton("Close", null); // Close button, no action
        Dialog dialog = builder.create();
        dialog.show();
    }
}