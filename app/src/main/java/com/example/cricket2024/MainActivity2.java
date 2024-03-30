package com.example.cricket2024;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    private ImageView tosswaitImageView;
    private Button headButton, tailButton, batButton, bowlButton, playButton;
    private TextView TossResultTextView,BatBowlResultTextView, oversToPlayTextView;
    Drawable drawable_win, drawable_lose;
    String videoPath1;
    String videoPath2;
    Spinner spinner;
    private Random random;
    ArrayList<String> playerNames = new ArrayList<>();
    String team;
    int playerCount;
    String Inns;
    String selectedOvers = "overs";
    int overs;
    String message;

    // User choices
    int userChoice;
    int coinResult;
    Button TossButton;
    VideoView videoView;
    MediaPlayer clickSoundPlayer, tossSoundPlayer, errorSoundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tosswaitImageView = findViewById(R.id.toss_wait);
        headButton = findViewById(R.id.head_button);
        tailButton = findViewById(R.id.tail_button);
        playButton = findViewById(R.id.play_button);
        videoView = findViewById(R.id.video_view);
        tossSoundPlayer = MediaPlayer.create(this, R.raw.toss);
        clickSoundPlayer = MediaPlayer.create(this, R.raw.click_sound);
        errorSoundPlayer = MediaPlayer.create(this, R.raw.error);
        TossButton = findViewById(R.id.toss_button);
        drawable_win = getResources().getDrawable(R.drawable.bg_light_green);
        drawable_lose = getResources().getDrawable(R.drawable.bg_light_red);

        TossResultTextView = findViewById(R.id.toss_result_text);
        BatBowlResultTextView = findViewById(R.id.bat_bowl_result_text);

        batButton = findViewById(R.id.bat_button);
        bowlButton = findViewById(R.id.bowl_button);

        oversToPlayTextView = findViewById(R.id.overs_to_play);
        spinner = findViewById(R.id.spinner);
        playButton = findViewById(R.id.play_button);

        random = new Random();

        // Specify the path to your MP4 video file
        videoPath1 = "android.resource://" + getPackageName() + "/" + R.raw.head;
        videoPath2 = "android.resource://" + getPackageName() + "/" + R.raw.tail;


        // Create a MediaController for controlling playback
        MediaController mediaController = new MediaController(this) {
            @Override
            public void show(int timeout) { }
        };
        mediaController.setAnchorView(videoView); // Attach the MediaController to the VideoView

        // Set the MediaController to the VideoView
        videoView.setMediaController(mediaController);


        headButton.setOnClickListener(v -> {
            clickSoundPlayer.start();
            userChoice = 0;
            TossButton.setVisibility(View.VISIBLE);
            headButton.setVisibility(View.INVISIBLE);
            tailButton.setVisibility(View.INVISIBLE);
            TossResultTextView.setText("Chosen Head");
        });

        tailButton.setOnClickListener(v -> {
            clickSoundPlayer.start();
            userChoice = 1;
            TossButton.setVisibility(View.VISIBLE);
            headButton.setVisibility(View.INVISIBLE);
            tailButton.setVisibility(View.INVISIBLE);
            TossResultTextView.setText("Chosen Tail");
        });

        TossButton.setOnClickListener(v -> {
            playRandomVideo();
            videoView.setVisibility(View.VISIBLE);
            tosswaitImageView.setVisibility(View.INVISIBLE);
            TossButton.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(this::performToss, 4000);
        });

        team = getIntent().getStringExtra("team");
        if (team.equals("indian_team")) {
            // Predefined player count and names retrieved in MainActivity
            playerCount = 11;
            // Predefined player names for Indian Team
            playerNames.addAll(Arrays.asList("Rohit", "Gill", "Kohli", "KL Rahul", "Rishabh", "SKY", "Hardik", "Jaddu", "Ashwin", "Bumrah", "Shami"));

        } else if (team.equals("custom_team")) {
            // Retrieve player count and names from MainActivity1
            Intent intent = getIntent();
            playerCount = intent.getIntExtra("PLAYER_COUNT", 0);
            playerNames = intent.getStringArrayListExtra("PLAYER_NAMES");
        }

        playButton.setOnClickListener(v -> {
            if (!selectedOvers.equals("overs")) {
                clickSoundPlayer.start();
                Intent intent = new Intent(this, MainActivity3.class);
                intent.putExtra("p_count", playerCount);
                intent.putStringArrayListExtra("players", playerNames);
                intent.putExtra("Inns", Inns);
                intent.putExtra("overs", overs);
                startActivity(intent);
            } else {
                errorSoundPlayer.start();
                Toast.makeText(MainActivity2.this, "Please select a valid number of overs", Toast.LENGTH_SHORT).show();
            }
        });

        populateSpinner();
    }

    private void performToss() {
        String message;
        int flag;
        spinner.setSelection(0);
        if (userChoice == coinResult) {
            clickSoundPlayer.start();
            message = "You Won the Toss!";
            TossResultTextView.setBackground(drawable_win);
            flag = 1;
        } else {
            errorSoundPlayer.start();
            message = "OOPS! You Lost the Toss!";
            TossResultTextView.setBackground(drawable_lose);
            flag = 0;
        }
        TossResultTextView.setText(message);// Set the result text
        hideElementsAfterAnimation(flag); // Hide buttons after another delay (optional)
    }



    private void hideElementsAfterAnimation(int flag) {
        tosswaitImageView.setVisibility(View.INVISIBLE);
        headButton.setVisibility(View.INVISIBLE);
        tailButton.setVisibility(View.INVISIBLE);
        TossResultTextView.setVisibility(View.VISIBLE);

        if(flag == 1) {
            batButton.setVisibility(View.VISIBLE);
            bowlButton.setVisibility(View.VISIBLE);

            // Add OnClickListeners for the buttons
            batButton.setOnClickListener(v -> {
                clickSoundPlayer.start();
                String message = "You choose to Bat!";
                Inns = "bat";
                BatBowlResultTextView.setText(message);
                batButton.setVisibility(View.INVISIBLE);
                bowlButton.setVisibility(View.INVISIBLE);
                oversToPlayTextView.setVisibility(View.VISIBLE);// Make overs text view visible
                spinner.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.VISIBLE);
                handleSpinnerSelection();// Make spinner visible
            });

            bowlButton.setOnClickListener(v -> {
                clickSoundPlayer.start();
                String message = "You choose to Bowl!";
                Inns = "bowl";
                BatBowlResultTextView.setText(message);
                batButton.setVisibility(View.INVISIBLE);
                bowlButton.setVisibility(View.INVISIBLE);
                oversToPlayTextView.setVisibility(View.VISIBLE); // Make overs text view visible
                spinner.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.VISIBLE);
                handleSpinnerSelection();// Make spinner visible
            });
        }
        else{
            // Simulate PC choosing bat or bowl randomly
            int pcChoice = random.nextInt(2);
            String pcAction = pcChoice == 0 ? "Bat" : "Bowl";
            if(pcAction.equals("Bat")){
                Inns = "bowl";
            } else {
                Inns = "bat";
            }
            if (Inns.equals("bat")) {
                message = "You Bat First!";
            } else {
                message = "You Bowl First!";
            }
            BatBowlResultTextView.setText(message);
            oversToPlayTextView.setVisibility(View.VISIBLE); // Make overs text view visible
            spinner.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.VISIBLE);
            handleSpinnerSelection();// Make spinner visible
        }
    }

    private void populateSpinner() {
        String[] overs = {"overs", "5", "10", "15", "20"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, overs);
        spinner.setAdapter(adapter);
    }

    // Function to handle spinner selection and play button visibility
    private void handleSpinnerSelection() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOvers = (String) parent.getItemAtPosition(position);
                try {
                    overs = Integer.parseInt(selectedOvers);
                } catch (NumberFormatException e) {
                    // Handle the case where the selected value is not a valid integer
                    e.printStackTrace(); // Print the stack trace for debugging
                    // Display an error message to the user
                    Toast.makeText(getApplicationContext(), "Invalid value selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection case (optional)
            }

        });
    }
    private void playRandomVideo() {
        coinResult = random.nextInt(2);
       // String videoPath;
        Uri videoUri;
        if (coinResult == 0) {
            // Convert the video path to a URI
            videoUri = Uri.parse(videoPath1);

            // Set the video URI to the VideoView
        } else {
            // Convert the video path to a URI
            videoUri = Uri.parse(videoPath2);

            // Set the video URI to the VideoView
        }
        videoView.setVideoURI(videoUri);
        videoView.start();
        tossSoundPlayer.start();
    }
}