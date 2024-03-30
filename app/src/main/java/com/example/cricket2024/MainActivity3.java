package com.example.cricket2024;


import static java.lang.Math.floor;
import static java.lang.Math.round;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity3 extends AppCompatActivity {
    TextView player_card1, player_card2, your_score, bot_score, your_wickets, bot_wickets, your_overs, bot_overs, user_state, your_total, bot_total,
            ball_1, ball_2, ball_3, ball_4, ball_5, ball_6, target, bowl_stat, player1_run, player2_run, message, textView;
    Drawable drawable_n, drawable_w, drawable_4_5, drawable_6, drawable_win, drawable_lose;
    Spinner spinnerCard1, spinnerCard2;
    Button select, b0, b1, b2, b3, b4, b5, b6;
    int[] p_over_w;
    int[] p_over_d;
    int wholeOvers = 0;
    int decimalOvers = 0;
    int inn;
    int a;
    int b;
    int p_count;
    int overs;
    int o_runs = 0;
    int[] p_out;
    int[] p_runs;
    int[] p_balls;
    int[] p_lo;
    double[] p_overs;
    int[] p_o_runs;
    int[] p_wickets;
    int y_total = 0;
    int b_total = 0;
    int y_wickets = 0;
    int b_wickets = 0;
    double over1 = 0.0;
    double over2 = 0.0;
    private int ballCount = 0;
    int run = 0;
    int out = 0;
    int[] runs = {0, 1, 2, 3, 4, 5, 6};
    String Inns;
    List<String> players;
    List<String> playerCardOptions = new ArrayList<>();
    int selectionStage = 0;
    Random random = new Random();
    MediaPlayer clickSoundPlayer, winSoundPlayer, loseSoundPlayer, wicketSoundPlayer, errorSoundPlayer, dotSoundPlayer, fourSoundPlayer, sixSoundPlayer, fiveSoundPlayer;
    boolean isCooldown = false;
    final long COOLDOWN_DURATION = 1000; // 1 second cool down

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        // Load the click sound from the raw folder
        clickSoundPlayer = MediaPlayer.create(this, R.raw.click_sound);
        winSoundPlayer = MediaPlayer.create(this, R.raw.win);
        loseSoundPlayer = MediaPlayer.create(this, R.raw.lose);
        wicketSoundPlayer = MediaPlayer.create(this, R.raw.stump);
        dotSoundPlayer = MediaPlayer.create(this, R.raw.dot);
        fourSoundPlayer = MediaPlayer.create(this, R.raw.four);
        sixSoundPlayer = MediaPlayer.create(this, R.raw.six);
        errorSoundPlayer = MediaPlayer.create(this, R.raw.error);
        fiveSoundPlayer = MediaPlayer.create(this, R.raw.five);



        // Create a callback for back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Show confirmation dialog
                showConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // Initialize UI elements
        player_card1 = findViewById(R.id.player_card1);
        player_card2 = findViewById(R.id.player_card2);
        b0 = findViewById(R.id.zero);
        b1 = findViewById(R.id.one);
        b2 = findViewById(R.id.two);
        b3 = findViewById(R.id.three);
        b4 = findViewById(R.id.four);
        b5 = findViewById(R.id.five);
        b6 = findViewById(R.id.six);
        ball_1 = findViewById(R.id.over_ball_1);
        ball_2 = findViewById(R.id.over_ball_2);
        ball_3 = findViewById(R.id.over_ball_3);
        ball_4 = findViewById(R.id.over_ball_4);
        ball_5 = findViewById(R.id.over_ball_5);
        ball_6 = findViewById(R.id.over_ball_6);
        your_total = findViewById(R.id.your_runs);
        bot_total = findViewById(R.id.cpu_runs);
        your_score = findViewById(R.id.y_choice);
        bot_score = findViewById(R.id.c_choice);
        your_wickets = findViewById(R.id.your_wickets);
        bot_wickets = findViewById(R.id.cpu_wickets);
        your_overs = findViewById(R.id.y_overs);
        bot_overs = findViewById(R.id.c_overs);
        user_state = findViewById(R.id.user_state);
        bowl_stat = findViewById(R.id.bowl_stat);
        player1_run = findViewById(R.id.player1_run);
        player2_run = findViewById(R.id.player2_run);
        target = findViewById(R.id.target);
        spinnerCard1 = findViewById(R.id.spinner1);
        spinnerCard2 = findViewById(R.id.spinner2);
        select = findViewById(R.id.select);
        message = findViewById(R.id.message);
        textView = findViewById(R.id.textView);
        drawable_n = getResources().getDrawable(R.drawable.round_tv_bg);
        drawable_w = getResources().getDrawable(R.drawable.wicket_bg);
        drawable_4_5 = getResources().getDrawable(R.drawable.fivefour_bg);
        drawable_6 = getResources().getDrawable(R.drawable.six_bg);
        drawable_win = getResources().getDrawable(R.drawable.bg_light_green);
        drawable_lose = getResources().getDrawable(R.drawable.bg_light_red);


        // Get intent data
        Intent intent = getIntent();
        if (intent != null) {
            p_count = intent.getIntExtra("p_count", 0);
            Inns = intent.getStringExtra("Inns");
            overs = intent.getIntExtra("overs", 0);
            players = intent.getStringArrayListExtra("players");
            p_out = new int[p_count];
            p_balls = new int[p_count];
            p_lo = new int[p_count];
            p_overs = new double[p_count];
            p_o_runs = new int[p_count];
            p_wickets = new int[p_count];
            p_runs = new int[p_count];
            p_over_w = new int[p_count];
            p_over_d = new int[p_count];
            if (players != null) {
                your_overs.setText("OVERS : " + String.valueOf(over1) + "/" + String.valueOf(overs));
                bot_overs.setText("OVERS : " + String.valueOf(over2) + "/" + String.valueOf(overs));
                if ("bat".equals(Inns)) {
                    inn = 1;
                    user_state.setText("Batting");
                    player_card2.setVisibility(View.VISIBLE);
                    player1_run.setVisibility(View.VISIBLE);
                    player2_run.setVisibility(View.VISIBLE);
                    spinnerCard1.setVisibility(View.VISIBLE);
                    select.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                    message.setText("Select a Batsman Above");
                    spinner(players, spinnerCard1, p_out, player_card1);
                } else {
                    inn = 2;
                    user_state.setText("Bowling");
                    bowl_stat.setVisibility(View.VISIBLE);
                    selectionStage = 3;
                    spinnerCard1.setVisibility(View.VISIBLE);
                    select.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                    message.setText("Select a Bowler Above");
                    spinner(players, spinnerCard1, p_lo, player_card1);
                }
                    // Set click listener for the select button
                    select.setOnClickListener(v -> {
                        if (selectionStage == 1) {// For Non-Striker
                            // Get second selection
                            if (player_card2.getText().toString().equals("Not Valid")) {
                                Toast.makeText(this, "Select a valid player", Toast.LENGTH_SHORT).show();
                                errorSoundPlayer.start();
                            } else {
                                clickSoundPlayer.start();
                                b = players.indexOf(player_card2.getText().toString());
                                p_out[b] = 1;// Set the player as playing
                                spinnerCard1.setVisibility(View.INVISIBLE);
                                spinnerCard2.setVisibility(View.GONE);
                                select.setVisibility(View.INVISIBLE);
                                message.setVisibility(View.INVISIBLE);
                                buttons_visible();
                                selectionStage = 2;
                                // Hide spinners, button, etc...
                            }
                        } else if (selectionStage == 0) {// For Striker
                            // Get first selection
                            clickSoundPlayer.start();
                            a = players.indexOf(player_card1.getText().toString());
                            p_out[a] = 1;// Set the player as playing
                            selectionStage = 1;
                            spinnerCard1.setVisibility(View.INVISIBLE);
                            spinnerCard2.setVisibility(View.VISIBLE);
                            spinner(players, spinnerCard2, p_out, player_card2); // Setup spinner2
                        } else if (selectionStage == 2) {// For wicket
                            if (player_card1.getText().toString().equals("Not Valid")) {
                                Toast.makeText(this, "Select a valid player", Toast.LENGTH_SHORT).show();
                                errorSoundPlayer.start();
                            } else {
                                clickSoundPlayer.start();
                                a = players.indexOf(player_card1.getText().toString());
                                p_out[a] = 1;// Set the player as playing
                                spinnerCard1.setVisibility(View.INVISIBLE);
                                select.setVisibility(View.INVISIBLE);
                                message.setVisibility(View.INVISIBLE);
                                buttons_visible();
                                if (ballCount % 6 == 0) {
                                    buttons_invisible();
                                    new Handler().postDelayed(() -> {
                                        reset_over();
                                        int temp = a;
                                        a = b;
                                        b = temp;
                                        player_card1.setText(players.get(a));
                                        player_card2.setText(players.get(b));
                                        player1_run.setText(String.valueOf(p_runs[a]) + "*");
                                        player2_run.setText(String.valueOf(p_runs[b]));
                                        buttons_visible();
                                    }, 1000);
                                }
                            }
                        } else if (selectionStage == 3) {// For bowler
                            if (player_card1.getText().toString().equals("Not Valid")) {
                                Toast.makeText(this, "Select a valid player", Toast.LENGTH_SHORT).show();
                                errorSoundPlayer.start();
                            } else {
                                if (p_count > 4){
                                    if (overs == 10){
                                        if (p_overs[a] < 2){
                                            p_lo[a] = 0;
                                        }
                                    }else if (overs == 15){
                                         if (p_overs[a] < 3){
                                            p_lo[a] = 0;
                                         }
                                    }else if (overs == 20){
                                        if (p_overs[a] < 4){
                                            p_lo[a] = 0;
                                        }
                                    }
                                }else{
                                    p_lo[a] = 0;
                                }
                                clickSoundPlayer.start();
                                a = players.indexOf(player_card1.getText().toString());
                                p_lo[a] = 1;
                                bowl_stat.setText(String.valueOf(p_wickets[a]) + "/" + String.valueOf(p_o_runs[a]) + " (" + String.valueOf(floor(p_overs[a] * 10) / 10) + ")");
                                spinnerCard1.setVisibility(View.INVISIBLE);
                                select.setVisibility(View.INVISIBLE);
                                message.setVisibility(View.INVISIBLE);
                                bowl_stat.setVisibility(View.VISIBLE);
                                buttons_visible();
                                reset_over();
                            }
                        }
                    });
                    View.OnClickListener buttonClickListener = view -> {
                        if (!isCooldown) {
                            // Start cooldown
                            isCooldown = true;
                            view.postDelayed(() -> isCooldown = false, COOLDOWN_DURATION);

                            // Increment ball count
                            ballCount++;

                            // Determine the run scored based on the button clicked
                            if (view.getId() == R.id.zero) {
                                if (inn == 1) {
                                    run = 0;
                                } else {
                                    out = 0;
                                }
                            } else if (view.getId() == R.id.one) {
                                if (inn == 1) {
                                    run = 1;
                                } else {
                                    out = 1;
                                }
                            } else if (view.getId() == R.id.two) {
                                if (inn == 1) {
                                    run = 2;
                                } else {
                                    out = 2;
                                }
                            } else if (view.getId() == R.id.three) {
                                if (inn == 1) {
                                    run = 3;
                                } else {
                                    out = 3;
                                }
                            } else if (view.getId() == R.id.four) {
                                if (inn == 1) {
                                    run = 4;
                                } else {
                                    out = 4;
                                }
                            } else if (view.getId() == R.id.five) {
                                if (inn == 1) {
                                    run = 5;
                                } else {
                                    out = 5;
                                }
                            } else if (view.getId() == R.id.six) {
                                if (inn == 1) {
                                    run = 6;
                                } else {
                                    out = 6;
                                }
                            }
                            if (inn == 1) {
                                int[] weights = {1, 2, 3, 6, 10, 25, 20};
                                int totalWeight = 0;
                                int[] cumulativeWeights = new int[weights.length];
                                // Calculate cumulative weights
                                for (int k = 0; k < weights.length; k++) {
                                    totalWeight += weights[k];
                                    cumulativeWeights[k] = totalWeight;
                                }
                                // Generate weighted random choice
                                int randomVal = random.nextInt(totalWeight);
                                for (int j = 0; j < cumulativeWeights.length; j++) {
                                    if (randomVal < cumulativeWeights[j]) {
                                        out = runs[j];
                                        break;
                                    }
                                }
                            } else {
                                run = runs[random.nextInt(runs.length)];
                            }
                            double ov = formatOvers();
                            if (inn == 1) {
                                p_balls[a] += 1;
                                over1 = ov;
                            } else {
                                if (ballCount % 6 == 0) {
                                    p_over_w[a] += 1;
                                    p_over_d[a] -= 5;
                                } else {
                                    p_over_d[a] += 1;
                                }
                                p_overs[a] = Double.parseDouble(String.valueOf(p_over_w[a]) + "." + String.valueOf(p_over_d[a]));
                                over2 = ov;
                            }
                            if (inn == 1) {
                                // Update over with the ball count
                                your_overs.setText("OVERS : " + String.valueOf(over1) + "/" + String.valueOf(overs));
                            } else {
                                bot_overs.setText("OVERS : " + String.valueOf(over2) + "/" + String.valueOf(overs));
                            }
                            if (run != out) {
                                o_runs += run;
                            }
                            this_over();
                            // Update UI with the run scored
                            if (inn == 1) {
                                your_score.setText(String.valueOf(run));
                                bot_score.setText(String.valueOf(out));
                            } else {
                                bot_score.setText(String.valueOf(run));
                                your_score.setText(String.valueOf(out));
                            }
                            // when wicket fall
                            if (run == out) {
                                wicketSoundPlayer.start();
                                VibrationHelper.vibrate(this, 1000);
                                if (inn == 1) {
                                    y_wickets += 1;
                                    p_out[a] = 2; // Set the player as out
                                    spinnerCard1.setVisibility(View.VISIBLE);
                                    select.setVisibility(View.VISIBLE);
                                    buttons_invisible();
                                    message.setVisibility(View.VISIBLE);
                                    your_wickets.setText(String.valueOf(y_wickets));
                                    Toast.makeText(this, player_card1.getText().toString() + " is Out!!!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(this, players.get(a) + " : " + String.valueOf(p_runs[a]) + " runs", Toast.LENGTH_SHORT).show();
                                    player1_run.setText("0*");
                                    spinner(players, spinnerCard1, p_out, player_card1);
                                    if (Objects.equals(Inns, "bat")) {
                                        innings_break();
                                    } else {
                                        if ((b_total + 1 - y_total) >= 0) {
                                            target.setText("YOU needs " + String.valueOf(b_total + 1 - y_total) + " run(s) in " + String.valueOf(overs * 6 - ballCount) + " ball(s)");
                                            result();
                                        } else {
                                            target.setVisibility(View.INVISIBLE);
                                            result();
                                        }
                                    }
                                } else {
                                    b_wickets += 1;
                                    p_wickets[a] += 1;
                                    bot_wickets.setText(String.valueOf(b_wickets));
                                    bowl_stat.setText(String.valueOf(p_wickets[a]) + "/" + String.valueOf(p_o_runs[a]) + " (" + String.valueOf(floor(p_overs[a] * 10) / 10) + ")");
                                    Toast.makeText(this, "Out!!!", Toast.LENGTH_SHORT).show();
                                    if (Inns.equals("bat")) {
                                        if ((y_total + 1 - b_total) >= 0) {
                                            target.setText("BOT needs " + String.valueOf(y_total + 1 - b_total) + " run(s) in " + String.valueOf(overs * 6 - ballCount) + " ball(s)");
                                            result();
                                        } else {
                                            target.setVisibility(View.INVISIBLE);
                                            result();
                                        }
                                    } else {
                                        innings_break();
                                    }
                                }
                                // when run scored
                            } else {
                                if (run == 4 ) {
                                    fourSoundPlayer.start();
                                    VibrationHelper.vibrate(this, 100);
                                } else if (run == 5) {
                                    fiveSoundPlayer.start();
                                    VibrationHelper.vibrate(this, 150);
                                } else if (run == 6) {
                                    sixSoundPlayer.start();
                                    VibrationHelper.vibrate(this, 200);
                                } else {
                                    dotSoundPlayer.start();
                                    VibrationHelper.vibrate(this, 50);
                                }
                                if (inn == 1) {
                                    y_total += run;
                                    your_total.setText(String.valueOf(y_total));
                                    p_runs[a] += run;
                                    player1_run.setText(String.valueOf(p_runs[a]) + "*");
                                    if ((ballCount % 6 == 0 && !(run == 1 || run == 3)) || (ballCount % 6 != 0 && (run == 1 || run == 3))) {
                                        int temp = a;
                                        a = b;
                                        b = temp;
                                        player_card1.setText(players.get(a));
                                        player_card2.setText(players.get(b));
                                        player1_run.setText(String.valueOf(p_runs[a]) + "*");
                                        player2_run.setText(String.valueOf(p_runs[b]));
                                    }
                                    if (Objects.equals(Inns, "bat")) {
                                        innings_break();
                                    } else {
                                        if ((b_total + 1 - y_total) >= 0) {
                                            target.setText("YOU needs " + String.valueOf(b_total + 1 - y_total) + " run(s) in " + String.valueOf(overs * 6 - ballCount) + " ball(s)");
                                            result();
                                        } else {
                                            target.setVisibility(View.INVISIBLE);
                                            result();
                                        }
                                    }
                                } else {
                                    b_total += run;
                                    p_o_runs[a] += run;
                                    bot_total.setText(String.valueOf(b_total));
                                    bowl_stat.setText(String.valueOf(p_wickets[a]) + "/" + String.valueOf(p_o_runs[a]) + " (" + String.valueOf(floor(p_overs[a] * 10) / 10) + ")");
                                    if (Objects.equals(Inns, "bat")) {
                                        if ((y_total + 1 - b_total) >= 0) {
                                            target.setText("BOT needs " + String.valueOf(y_total + 1 - b_total) + " run(s) in " + String.valueOf(overs * 6 - ballCount) + " ball(s)");
                                            result();
                                        } else {
                                            target.setVisibility(View.INVISIBLE);
                                            result();
                                        }
                                    } else {
                                        innings_break();
                                    }
                                }
                            }
                        }
                    };
                    // Attach click listeners to buttons
                    b0.setOnClickListener(buttonClickListener);
                    b1.setOnClickListener(buttonClickListener);
                    b2.setOnClickListener(buttonClickListener);
                    b3.setOnClickListener(buttonClickListener);
                    b4.setOnClickListener(buttonClickListener);
                    b5.setOnClickListener(buttonClickListener);
                    b6.setOnClickListener(buttonClickListener);
            }
        }
    }
    private void showConfirmationDialog() {
        errorSoundPlayer.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to go back?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            clickSoundPlayer.start();
            finish(); // Close the activity
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            clickSoundPlayer.start();
            // Do nothing, simply dismiss the dialog
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void spinner(List<String> players, Spinner spinnerCard, int[] check_list, TextView player_card) {
        playerCardOptions.clear();
        for (int index = 0; index < players.size(); index++) {
            if (check_list[index] == 0) {
                playerCardOptions.add(players.get(index));
            } else {
                playerCardOptions.add("-");
            }
        }
        ArrayAdapter<String> adapterCard = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, playerCardOptions);
        adapterCard.notifyDataSetChanged();
        spinnerCard.setAdapter(adapterCard);
        spinnerCard.setSelection(0);
        spinnerCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Objects.equals(playerCardOptions.get(position), "-")) {
                    player_card.setText("Not Valid");
                } else {
                    player_card.setText(players.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void this_over() {
        int ball = ballCount % 6;
        String print;
        if (ball == 0) {
            Toast.makeText(this, "This Over : " + o_runs, Toast.LENGTH_SHORT).show();
            o_runs = 0;
        }
        if (ballCount % 6 == 0 && inn == 1 && run != out) {
            buttons_invisible();
            new Handler().postDelayed(() -> {
                reset_over();
                buttons_visible();
            }, 1000);
        }
        if (run == out){
            print = "W";
        } else {
            print = String.valueOf(run);
        }
        if (ball == 1) {
            ball_1.setText(print);
            if (print.equals("W")){
                ball_1.setBackground(drawable_w);
            } else if (run == 6){
                ball_1.setBackground(drawable_6);
            } else if (run == 4 | run == 5){
                ball_1.setBackground(drawable_4_5);
            } else {
                ball_1.setBackground(drawable_n);
            }
        }else if (ball == 2) {
            ball_2.setText(print);
            if (print.equals("W")){
                ball_2.setBackground(drawable_w);
            } else if (run == 6){
                ball_2.setBackground(drawable_6);
            } else if (run == 4 | run == 5){
                ball_2.setBackground(drawable_4_5);
            } else {
                ball_2.setBackground(drawable_n);
            }
        }else if (ball == 3) {
            ball_3.setText(print);
            if (print.equals("W")){
                ball_3.setBackground(drawable_w);
            } else if (run == 6){
                ball_3.setBackground(drawable_6);
            } else if (run == 4 | run == 5){
                ball_3.setBackground(drawable_4_5);
            } else {
                ball_3.setBackground(drawable_n);
            }
        }else if (ball == 4) {
            ball_4.setText(print);
            if (print.equals("W")){
                ball_4.setBackground(drawable_w);
            } else if (run == 6){
                ball_4.setBackground(drawable_6);
            } else if (run == 4 | run == 5){
                ball_4.setBackground(drawable_4_5);
            } else {
                ball_4.setBackground(drawable_n);
            }
        }else if (ball == 5) {
            ball_5.setText(print);
            if (print.equals("W")){
                ball_5.setBackground(drawable_w);
            } else if (run == 6){
                ball_5.setBackground(drawable_6);
            } else if (run == 4 | run == 5){
                ball_5.setBackground(drawable_4_5);
            } else {
                ball_5.setBackground(drawable_n);
            }
        }else{
            ball_6.setText(print);
            if (print.equals("W")){
                ball_6.setBackground(drawable_w);
            } else if (run == 6){
                ball_6.setBackground(drawable_6);
            } else if (run == 4 | run == 5){
                ball_6.setBackground(drawable_4_5);
            } else {
                ball_6.setBackground(drawable_n);
            }
        }
    }

    private void buttons_invisible() {
        b0.setVisibility(View.INVISIBLE);
        b1.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);
        b3.setVisibility(View.INVISIBLE);
        b4.setVisibility(View.INVISIBLE);
        b5.setVisibility(View.INVISIBLE);
        b6.setVisibility(View.INVISIBLE);
    }
    private void buttons_visible() {
        b0.setVisibility(View.VISIBLE);
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        b3.setVisibility(View.VISIBLE);
        b4.setVisibility(View.VISIBLE);
        b5.setVisibility(View.VISIBLE);
        b6.setVisibility(View.VISIBLE);
    }
    private void reset_over() {
        ball_1.setText("");
        ball_1.setBackground(drawable_n);
        ball_2.setText("");
        ball_2.setBackground(drawable_n);
        ball_3.setText("");
        ball_3.setBackground(drawable_n);
        ball_4.setText("");
        ball_4.setBackground(drawable_n);
        ball_5.setText("");
        ball_5.setBackground(drawable_n);
        ball_6.setText("");
        ball_6.setBackground(drawable_n);
    }

    public void match_summary () {
        setContentView(R.layout.activity_main4);
        TextView r_your_total = findViewById(R.id.r_your_runs);
        TextView r_bot_total = findViewById(R.id.r_cpu_runs);
        TextView r_your_wickets = findViewById(R.id.r_your_wickets);
        TextView r_bot_wickets = findViewById(R.id.r_cpu_wickets);
        TextView r_your_overs = findViewById(R.id.r_y_overs);
        TextView r_bot_overs = findViewById(R.id.r_c_overs);
        TextView result = findViewById(R.id.result);
        Button rematch = findViewById(R.id.rematch);
        Button home = findViewById(R.id.home);
        r_your_total.setText(String.valueOf(y_total));
        r_bot_total.setText(String.valueOf(b_total));
        r_your_wickets.setText(String.valueOf(y_wickets));
        r_bot_wickets.setText(String.valueOf(b_wickets));
        r_your_overs.setText("OVERS : " + String.valueOf(over1) + "/" + String.valueOf(overs));
        r_bot_overs.setText("OVERS : " + String.valueOf(over2) + "/" + String.valueOf(overs));

        if (user_state.getText().equals("YOU WIN!!!")) {
            if (Inns.equals("bat")) {
                result.setText("YOU WON BY " + String.valueOf(y_total - b_total) + " RUNS");
                result.setBackground(drawable_win);
                result.setTextColor(ContextCompat.getColor(this, R.color.black));
            } else {
                result.setText("YOU WON BY " + String.valueOf((p_count - 1) - y_wickets) + " WICKETS");
                result.setBackground(drawable_win);
                result.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        } else if (user_state.getText().equals("YOU LOSE!!!")){
            if (Inns.equals("bat")) {
                result.setText("BOT WON BY " + String.valueOf((p_count - 1) - b_wickets) + " WICKETS");
                result.setBackground(drawable_lose);
                result.setTextColor(ContextCompat.getColor(this, R.color.black));
            } else {
                result.setText("BOT WON BY " + String.valueOf(b_total - y_total) + " RUNS");
                result.setBackground(drawable_lose);
                result.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        } else {
            if (Inns.equals("bat")) {
                result.setText("MATCH DRAW");
                result.setBackground(drawable_win);
                result.setTextColor(ContextCompat.getColor(this, R.color.black));
            } else {
                result.setText("MATCH DRAW");
                result.setBackground(drawable_lose);
                result.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }

        // Assuming you have a TableLayout defined in your activity_main.xml with the id "players_table_layout"
        TableLayout tableLayout = findViewById(R.id.players_batting);

// Assuming players is a list of player details
        for (int i = 0; i < players.size(); i++) {
            // Create a new row
            TableRow tableRow = new TableRow(this);

            // Set background drawable to the TableRow
            tableRow.setBackgroundResource(R.drawable.scorecard);

            Resources r = getResources();
            float widthInDp = 170; // Width in dp
            float widthInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, r.getDisplayMetrics());

            // Create and configure TextView for player name
            TextView playerNameTextView = new TextView(this);
            playerNameTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // 170dp width
            playerNameTextView.setText(players.get(i));
            playerNameTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
            playerNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            playerNameTextView.setPadding(10, 0, 0, 0);

            if (p_out[i] > 0) {

                widthInDp = 60; // Width in dp
                widthInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, r.getDisplayMetrics());

                // Create and configure TextView for player runs
                TextView playerRunsTextView = new TextView(this);
                playerRunsTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 1f)); // 60dp width
                if (p_out[i] == 2) {
                    playerRunsTextView.setText(String.valueOf(p_runs[i]));
                } else {
                    playerRunsTextView.setText(String.valueOf(p_runs[i]) + "*");
                }
                playerRunsTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerRunsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerRunsTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Create and configure TextView for player balls
                TextView playerBallsTextView = new TextView(this);
                playerBallsTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 1f)); // 60dp width
                playerBallsTextView.setText(String.valueOf(p_balls[i]));
                playerBallsTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerBallsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerBallsTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Create and configure TextView for player strike rate
                TextView playerStrikeRateTextView = new TextView(this);
                playerStrikeRateTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 1f)); // 60dp width
                if (p_balls[i] == 0) {
                    playerStrikeRateTextView.setText("0");
                } else {
                    playerStrikeRateTextView.setText(String.valueOf(p_runs[i] * 100 / p_balls[i]));
                }
                playerStrikeRateTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerStrikeRateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerStrikeRateTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Add TextViews to the TableRow
                tableRow.addView(playerNameTextView);
                tableRow.addView(playerRunsTextView);
                tableRow.addView(playerBallsTextView);
                tableRow.addView(playerStrikeRateTextView);

                // Add the TableRow to the TableLayout
                tableLayout.addView(tableRow);
            } else {
                widthInDp = 60; // Width in dp
                widthInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, r.getDisplayMetrics());

                // Create and configure TextView for player runs
                TextView playerRunsTextView = new TextView(this);
                playerRunsTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 1f)); // 60dp width
                playerRunsTextView.setText("-");
                playerRunsTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerRunsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerRunsTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Create and configure TextView for player balls
                TextView playerBallsTextView = new TextView(this);
                playerBallsTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 1f)); // 60dp width
                playerBallsTextView.setText("-");
                playerBallsTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerBallsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerBallsTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Create and configure TextView for player strike rate
                TextView playerStrikeRateTextView = new TextView(this);
                playerStrikeRateTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 1f)); // 60dp width
                playerStrikeRateTextView.setText("-");
                playerStrikeRateTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerStrikeRateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerStrikeRateTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Add TextViews to the TableRow
                tableRow.addView(playerNameTextView);
                tableRow.addView(playerRunsTextView);
                tableRow.addView(playerBallsTextView);
                tableRow.addView(playerStrikeRateTextView);

                // Add the TableRow to the TableLayout
                tableLayout.addView(tableRow);
            }
        }
        tableLayout = findViewById(R.id.players_bowling);

// Assuming players is a list of player details
        for (int i = 0; i < players.size(); i++) {
            if (p_overs[i] > 0) {            // Create a new row
                TableRow tableRow = new TableRow(this);

                // Set background drawable to the TableRow
                tableRow.setBackgroundResource(R.drawable.scorecard);

                Resources r = getResources();
                float widthInDp = 150; // Width in dp
                float widthInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, r.getDisplayMetrics());


                // Create and configure TextView for player name
                TextView playerNameTextView = new TextView(this);
                playerNameTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.WRAP_CONTENT, 0)); // 150dp width
                playerNameTextView.setText(players.get(i));
                playerNameTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerNameTextView.setPadding(10, 0, 0, 0);

                widthInDp = 50; // Width in dp
                widthInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, r.getDisplayMetrics());

                // Create and configure TextView for player runs
                TextView playerOversTextView = new TextView(this);
                playerOversTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 0)); // 50dp width
                playerOversTextView.setText(String.valueOf(p_overs[i]));
                playerOversTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerOversTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerOversTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView playerWicketsTextView = new TextView(this);
                playerWicketsTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 0)); // 50dp width
                playerWicketsTextView.setText(String.valueOf(p_wickets[i]));
                playerWicketsTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerWicketsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerWicketsTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Create and configure TextView for player balls
                TextView playerRunsGivenTextView = new TextView(this);
                playerRunsGivenTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 1f)); // 50dp width
                playerRunsGivenTextView.setText(String.valueOf(p_o_runs[i]));
                playerRunsGivenTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerRunsGivenTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerRunsGivenTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Create and configure TextView for player strike rate
                TextView playerEconamyTextView = new TextView(this);
                playerEconamyTextView.setLayoutParams(new TableRow.LayoutParams((int) widthInPixels, TableRow.LayoutParams.MATCH_PARENT, 1f)); // 50dp width
                if (p_overs[i] - round(p_overs[i]) != 0) {
                    playerEconamyTextView.setText(String.valueOf(floor((p_o_runs[i] / (0.16666666666666666 * ((p_over_w[i] * 6 )+ p_over_d[i]))) * 10) / 10));
                } else {
                    playerEconamyTextView.setText(String.valueOf(p_o_runs[i] / p_overs[i]));
                }
                playerEconamyTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                playerEconamyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                playerEconamyTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Add TextViews to the TableRow
                tableRow.addView(playerNameTextView);
                tableRow.addView(playerOversTextView);
                tableRow.addView(playerWicketsTextView);
                tableRow.addView(playerRunsGivenTextView);
                tableRow.addView(playerEconamyTextView);

                // Add the TableRow to the TableLayout
                tableLayout.addView(tableRow);
            }
        }
        rematch.setOnClickListener(v -> {
            clickSoundPlayer.start();
            Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
            startActivity(intent);
        });
        home.setOnClickListener(v -> {
            clickSoundPlayer.start();
            Intent intent = new Intent(MainActivity3.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }


    private double formatOvers() {
        if (ballCount % 6 == 0) {
            wholeOvers += 1;
            decimalOvers = 0;
        } else {
            decimalOvers += 1;
        }
        return Double.parseDouble(wholeOvers + "." + decimalOvers);
    }
    public void innings_break() {
        if (Inns.equals("bat")) {
            if ( ballCount == overs * 6) {
                buttons_invisible();
            }
            if (y_wickets == p_count - 1) {
                spinnerCard1.setVisibility(View.INVISIBLE);
                select.setVisibility(View.INVISIBLE);
            }
            new Handler().postDelayed(() -> {
                if (y_wickets == p_count - 1 || ballCount == overs * 6) {
                    o_runs = 0;
                    spinnerCard1.setVisibility(View.INVISIBLE);
                    select.setVisibility(View.INVISIBLE);
                    buttons_invisible();
                    message.setVisibility(View.VISIBLE);
                    message.setText("Take a Breath for 2nd Innings");
                    new Handler().postDelayed(() -> {
                        your_score.setText("");
                        bot_score.setText("");
                        player_card1.setVisibility(View.INVISIBLE);
                        player_card2.setVisibility(View.INVISIBLE);
                        player1_run.setVisibility(View.INVISIBLE);
                        player2_run.setVisibility(View.INVISIBLE);
                        target.setVisibility(View.VISIBLE);
                        target.setText("BOT Target : " + String.valueOf(y_total + 1));
                        user_state.setText("Innings Break");
                        if (y_wickets == p_count - 1) {
                            Toast.makeText(this, "All Out!!!", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(this, "Innings Break...", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> {
                            Toast.makeText(this, "Start Bowling...", Toast.LENGTH_SHORT).show();
                            user_state.setText("Bowling");
                            reset_over();
                            player_card1.setVisibility(View.VISIBLE);
                            bowl_stat.setVisibility(View.VISIBLE);
                            spinnerCard1.setVisibility(View.VISIBLE);
                            select.setVisibility(View.VISIBLE);
                            selectionStage = 3;
                            inn = 2;
                            ballCount = 0;
                            wholeOvers = 0;
                            decimalOvers = 0;
                            message.setText("Select a Bowler Above");
                            spinner(players, spinnerCard1, p_lo, player_card1);
                        }, 3000);
                    }, 3000);
                }
            }, 1100);
        } else {
            if (ballCount % 6 == 0 && ballCount != overs * 6) {
                buttons_invisible();
                bowl_stat.setVisibility(View.INVISIBLE);
                spinnerCard1.setVisibility(View.VISIBLE);
                select.setVisibility(View.VISIBLE);
                message.setVisibility(View.VISIBLE);
                spinner(players, spinnerCard1, p_lo, player_card1);
            }
            if (b_wickets == p_count - 1 || ballCount == overs * 6) {
                o_runs = 0;
                spinnerCard1.setVisibility(View.INVISIBLE);
                select.setVisibility(View.INVISIBLE);
                buttons_invisible();
                message.setVisibility(View.VISIBLE);
                message.setText("Take a Breath for 2nd Innings");
                if (b_wickets == p_count - 1) {
                    Toast.makeText(this, "All Out!!!", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(() -> {
                    your_score.setText("");
                    bot_score.setText("");
                    player_card1.setVisibility(View.INVISIBLE);
                    bowl_stat.setVisibility(View.INVISIBLE);
                    target.setVisibility(View.VISIBLE);
                    target.setText("YOUR Target : " +String.valueOf(b_total + 1));
                    user_state.setText("Innings Break");
                    Toast.makeText(this, "Innings Break...", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> {
                        Toast.makeText(this, "Start Batting...", Toast.LENGTH_SHORT).show();
                        user_state.setText("Batting");
                        reset_over();
                        player_card1.setVisibility(View.VISIBLE);
                        player_card2.setVisibility(View.VISIBLE);
                        player1_run.setVisibility(View.VISIBLE);
                        player2_run.setVisibility(View.VISIBLE);
                        spinnerCard1.setVisibility(View.VISIBLE);
                        select.setVisibility(View.VISIBLE);
                        selectionStage = 0;
                        inn = 1;
                        ballCount = 0;
                        wholeOvers = 0;
                        decimalOvers = 0;
                        message.setText("Select a Batsman Above");
                        spinner(players, spinnerCard1, p_out, player_card1);
                    }, 3000);
                }, 3000);
            }
        }
    }
    public void result() {
        if (Objects.equals(Inns, "bat")) {
            if (ballCount % 6 == 0) {
                buttons_invisible();
                bowl_stat.setVisibility(View.INVISIBLE);
                spinnerCard1.setVisibility(View.VISIBLE);
                select.setVisibility(View.VISIBLE);
                spinner(players, spinnerCard1, p_lo, player_card1);
            }
            if (b_total > y_total || ballCount == overs * 6 || b_wickets == p_count - 1) {
                spinnerCard1.setVisibility(View.INVISIBLE);
                select.setVisibility(View.INVISIBLE);
                buttons_invisible();
                new Handler().postDelayed(() -> {
                    reset_over();
                    player_card1.setVisibility(View.INVISIBLE);
                    bowl_stat.setVisibility(View.INVISIBLE);
                    if (b_total > y_total) {
                       loseSoundPlayer.start();
                        Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_SHORT).show();
                        user_state.setBackground(drawable_lose);
                        user_state.setText("YOU LOSE!!!");
                        new Handler().postDelayed(this::match_summary, 3000);
                    } else if (b_total == y_total) {
                        winSoundPlayer.start();
                        Toast.makeText(this, "DRAW!!!", Toast.LENGTH_SHORT).show();
                        user_state.setBackground(drawable_win);
                        user_state.setText("DRAW!!!");
                        new Handler().postDelayed(this::match_summary, 3000);
                    } else {
                        winSoundPlayer.start();
                        Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_SHORT).show();
                        user_state.setBackground(drawable_win);
                        user_state.setText("YOU WIN!!!");
                        new Handler().postDelayed(this::match_summary, 3000);
                    }
                }, 3000);
            }
        } else {
            if ( ballCount == overs * 6) {
                buttons_invisible();
            }
            if (y_wickets == p_count - 1) {
                spinnerCard1.setVisibility(View.INVISIBLE);
                select.setVisibility(View.INVISIBLE);
            }
            new Handler().postDelayed(() -> {
                if (b_total < y_total || ballCount == overs * 6 || y_wickets == p_count - 1) {
                    spinnerCard1.setVisibility(View.INVISIBLE);
                    select.setVisibility(View.INVISIBLE);
                    buttons_invisible();
                    new Handler().postDelayed(() -> {
                        reset_over();
                        player_card1.setVisibility(View.INVISIBLE);
                        player_card2.setVisibility(View.INVISIBLE);
                        player1_run.setVisibility(View.INVISIBLE);
                        player2_run.setVisibility(View.INVISIBLE);
                        if (b_total < y_total) {
                            winSoundPlayer.start();
                            Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_SHORT).show();
                            user_state.setBackground(drawable_win);
                            user_state.setText("YOU WIN!!!");
                            new Handler().postDelayed(this::match_summary, 3000);
                        } else if (b_total == y_total) {
                            loseSoundPlayer.start();
                            Toast.makeText(this, "DRAW!!!", Toast.LENGTH_SHORT).show();
                            user_state.setBackground(drawable_lose);
                            user_state.setText("DRAW!!!");
                            new Handler().postDelayed(this::match_summary, 3000);
                        } else {
                            loseSoundPlayer.start();
                            Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_SHORT).show();
                            user_state.setBackground(drawable_lose);
                            user_state.setText("YOU LOSE!!!");
                            new Handler().postDelayed(this::match_summary, 3000);
                        }
                    }, 3000);
                }
            }, 1100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clickSoundPlayer != null) {
            clickSoundPlayer.release();
            clickSoundPlayer = null;
        }
        if (winSoundPlayer != null) {
            winSoundPlayer.release();
            winSoundPlayer = null;
        }
        if (loseSoundPlayer != null) {
            loseSoundPlayer.release();
            loseSoundPlayer = null;
        }
        if (sixSoundPlayer != null) {
            sixSoundPlayer.release();
            sixSoundPlayer = null;
        }
        if (fourSoundPlayer != null) {
            fourSoundPlayer.release();
            fourSoundPlayer = null;
        }
        if (dotSoundPlayer != null) {
            dotSoundPlayer.release();
            dotSoundPlayer = null;
        }
    }
}
