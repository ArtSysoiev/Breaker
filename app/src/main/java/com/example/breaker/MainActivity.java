package com.example.breaker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TableLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    TableLayout table;
    TextView titleLabel, instructionLabel;
    StartButton startBtn;
    ImageButton restartButton, menuButton;
    ConstraintLayout layout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View navigationHeader;
    TextView totalGamesInfo;
    TextView winsInfo;
    TextView winstreakInfo;

    DB db;
    DbDao dao;
    private ExecutorService executor;

    InputRow currentRow;
    int[] numbers = new Random().ints(1, 10).distinct().limit(Config.inputCount).toArray();
    int guesses = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onResume() {
        super.onResume();
        restart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setSelectedLanguage(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!Utils.isPreferencesSet(this)) {
            startActivity(SettingsActivity.class);
        }

//        Utils.setSelectedLanguage(this);
//        Utils.setSelectedTheme(this);

        table = findViewById(R.id.table);
        titleLabel = findViewById(R.id.title_label);
        instructionLabel = findViewById(R.id.instruction_label);
        startBtn = findViewById(R.id.start_btn);
        restartButton = findViewById(R.id.restart_button);
        menuButton = findViewById(R.id.menu_button);
        layout = findViewById(R.id.main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_menu);

        db = Room.databaseBuilder(this, DB.class, "games").build();
        dao = db.DbDao();

        navigationHeader = navigationView.getHeaderView(0);
        totalGamesInfo = navigationHeader.findViewById(R.id.total_games_info);
        winsInfo = navigationHeader.findViewById(R.id.wins_info);
        winstreakInfo = navigationHeader.findViewById(R.id.winstreak_info);

        executor = Executors.newSingleThreadExecutor();

        TextView usernameLabel = navigationHeader.findViewById(R.id.username_label);
        usernameLabel.setText(Utils.getPreferencesUsernameOrDefault(this));

        setStats();

        startBtn.setOnClickListener(v -> startCallback());
        startBtn.tryCallback = v -> tryCallback();

        restartButton.setOnClickListener(v -> restart());
        restartButton.setEnabled(false);
        restartButton.setAlpha(0f);

        menuButton.setOnClickListener(v -> {
            drawerLayout.open();
            Utils.hideKeyboard(menuButton);
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_item_settings) {
                startActivity(SettingsActivity.class);
            } else if (itemId == R.id.menu_item_game_history) {
                 startActivity(HistoryActivity.class);
            } else if (itemId == R.id.menu_item_exit) {
                finishAndRemoveTask();
            }
            drawerLayout.close();
            return false;
        });
    }

    private void onGameFinished(boolean isWin) {
        executor.execute(() -> dao.insert(new DbGame(guesses, isWin)));
        setStats();
        restartButton.setEnabled(false);
        Utils.animateAlpha(restartButton, 0f);
    }


    private void startActivity(Class<?> cls) {
       Intent intent = new Intent(this, cls);
       startActivity(intent);
    }

    private void startCallback() {
        instructionLabel.setText(getString(R.string.tries_left_text, Config.maxGuesses - guesses));
        currentRow = new InputRow(this, startBtn, new int[0]);

        table.addView(currentRow);
        startBtn.setOnClickListener(v -> tryCallback());
        startBtn.setText(R.string.try_btn_text);
        startBtn.disable(true);
        startBtn.move();
        restartButton.setEnabled(true);
        Utils.animateAlpha(restartButton, 1f);
    }

    private void tryCallback() {
        int animDelay = Config.animationDuration * (Config.inputCount + 1);
        currentRow.clearFocus();
        Utils.hideKeyboard(currentRow);

        startBtn.disable(false);

        instructionLabel.setText(getString(R.string.tries_left_text, Config.maxGuesses - ++guesses));

        // WHen all correct | WIN
        if (currentRow.checkGuess(numbers)) {
            handler.postDelayed(() -> {
                instructionLabel.setText(R.string.win_text);
                startBtn.setText(R.string.restart_btn_text);
                startBtn.enable(v -> restart());
                onGameFinished(true);
            }, animDelay);
            return;
        }

        // When user ran out of guesses | LOSS
        if (guesses == Config.maxGuesses) {
            handler.postDelayed(() -> {
                instructionLabel.setText(R.string.lose_text);
                table.addView(new InputRow(this, startBtn, numbers));
                startBtn.move();
                startBtn.setText(R.string.restart_btn_text);
                startBtn.enable(v -> restart());
                onGameFinished(false);
            }, animDelay);
            return;
        }



        handler.postDelayed(() -> {
            currentRow = new InputRow(this, startBtn, new int[0]);
            table.addView(currentRow);

            handler.post(() -> startBtn.disable(true));
            startBtn.move();
        }, animDelay);
    }

    private void restart() {
        handler.removeCallbacksAndMessages(null);
        startBtn.moveToStart();
        startBtn.setText(R.string.start_btn_text);
        startBtn.enable(v -> startCallback());
        restartButton.setEnabled(false);
        Utils.animateAlpha(restartButton, 0f);
        table.removeAllViews();
        instructionLabel.setText(R.string.instruction_text);
        guesses = 0;
        numbers = new Random().ints(1, 10).distinct().limit(Config.inputCount).toArray();
    }

    private void setStats() {
        dao.getGamesCount().observe(this, count -> {
            totalGamesInfo.setText(count != null ? String.valueOf(count) : "0");
        });

        dao.getWinsCount().observe(this, count -> {
            winsInfo.setText(count != null ? String.valueOf(count) : "0");
        });

        dao.getAll().observe(this, games -> {
            int winstreak = 0;
            for (DbGame game : games) {
                if (game.is_win) {
                    winstreak++;
                } else {
                    break;
                }
            }
            winstreakInfo.setText(String.valueOf(String.valueOf(winstreak)));
        });
    }
}
