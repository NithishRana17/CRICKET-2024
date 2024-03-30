package com.example.cricket2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity1 extends AppCompatActivity {

    private TextView numPlayersTextView;
    private EditText numPlayersEditText;
    private Button generateButton, next;
    private LinearLayout playerInputContainer;
    private ScrollView scrollView;
    private ImageView custom_team;
    MediaPlayer clickSoundPlayer, errorSoundPlayer;

    ArrayList<String> playerNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        numPlayersTextView = findViewById(R.id.num_players_text);
        numPlayersEditText = findViewById(R.id.num_players_edittext);
        generateButton = findViewById(R.id.generate_button);
        scrollView = findViewById(R.id.scrollView);
        custom_team = findViewById(R.id.imageView);
        next = findViewById(R.id.next);
        clickSoundPlayer = MediaPlayer.create(this, R.raw.click_sound);
        errorSoundPlayer = MediaPlayer.create(this, R.raw.error);

        playerInputContainer = findViewById(R.id.player_input_container);

        generateButton.setOnClickListener(v -> {
            generatePlayerInputFields();
            next.setVisibility(View.VISIBLE);
        });

        next.setOnClickListener(v -> sendPlayerDataToNextActivity());
    }
    private void generatePlayerInputFields() {
        String numPlayersStr = numPlayersEditText.getText().toString();

        int numPlayers = Integer.parseInt(numPlayersStr);
        // Limit input 2 to 11
        if (numPlayers < 2 || numPlayers > 11)
        {
            Toast.makeText(this,"Player count must be 2 - 11", Toast.LENGTH_SHORT).show();
            errorSoundPlayer.start();
            return;
        }
        clickSoundPlayer.start();
        for (int i = 1; i <= numPlayers; i++) {
            // Create TextView
            TextView playerTextView = new TextView(this);
            playerTextView.setId(View.generateViewId()); // API 17 and above
            playerTextView.setText("Player " + i + ": ");
            playerTextView.setTextSize(20.0f);
            playerTextView.setTextColor(Color.WHITE);

            // Create EditText
            EditText playerEditText = new EditText(this);
            playerEditText.setId(View.generateViewId());
            int maxLength = 10; // Change this to your desired maximum length
            InputFilter[] filters = new InputFilter[] { new InputFilter.LengthFilter(maxLength) };
            playerEditText.setFilters(filters);
            playerEditText.setHint("Player " + i + " Name");
            playerEditText.setTextColor(Color.BLACK);
            playerEditText.setBackgroundColor(Color.WHITE);

            // Add to the layout
            playerInputContainer.addView(playerTextView);
            playerInputContainer.addView(playerEditText);
        }
        numPlayersTextView.setVisibility(View.INVISIBLE);
        numPlayersEditText.setVisibility(View.INVISIBLE);
        generateButton.setVisibility(View.INVISIBLE);
        custom_team.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
    }

    private void sendPlayerDataToNextActivity() {

        String numPlayersStr = numPlayersEditText.getText().toString();
        int playerCount = Integer.parseInt(numPlayersStr);

        boolean isEmptyField = false;
        for (int i = 0; i < playerCount; i++) {
            EditText playerEditText = (EditText) playerInputContainer.getChildAt(i * 2 + 1); // Get EditText at odd positions (i * 2 + 1)
            String playerName = playerEditText.getText().toString().trim();
            if (playerName.isEmpty()) {
                isEmptyField = true;
                break; // Exit the loop if an empty field is found
            }
            playerNames.add(playerName);
        }

        if (isEmptyField) {
            errorSoundPlayer.start();
            Toast.makeText(this, "Please fill all player names", Toast.LENGTH_SHORT).show();
        } else {
            clickSoundPlayer.start();
            // Create Intent to launch MainActivity2
            Intent intent = new Intent(this, MainActivity2.class);

            // Put player count and names as extras
            intent.putExtra("team", "custom_team");
            intent.putExtra("PLAYER_COUNT", playerCount);
            intent.putExtra("PLAYER_NAMES", playerNames);
            startActivity(intent);
        }
    }
}
