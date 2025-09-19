package com.example.breaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class HistoryActivity extends AppCompatActivity {
    TextView historyTitleLabel;
    LinearLayout titleLayout;
    TextView gameNumberTitle;
    TextView dateTitle;
    TextView guessesTitle;
    TextView resultTitle;
    RecyclerView historyRecyclerView;
    Button backButton;

    DB db;
    DbDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setSelectedLanguage(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        historyTitleLabel = findViewById(R.id.history_title_label);
        titleLayout = findViewById(R.id.title_layout);
        gameNumberTitle = findViewById(R.id.game_number_title);
        dateTitle = findViewById(R.id.date_title);
        guessesTitle = findViewById(R.id.guesses_title);
        resultTitle = findViewById(R.id.result_title);
        historyRecyclerView = findViewById(R.id.history_recycler_view);

        db = Room.databaseBuilder(this, DB.class, "games").build();
        dao = db.DbDao();
        dao.getAll().observe(this, games -> {
            historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            historyRecyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.history_item_layout, parent, false);
                    return new RecyclerView.ViewHolder(view) {};
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    DbGame game = games.get(position);
                    TextView gameNumberItem = holder.itemView.findViewById(R.id.game_number_item);
                    TextView dateItem = holder.itemView.findViewById(R.id.date_item);
                    TextView guessesItem = holder.itemView.findViewById(R.id.guesses_item);
                    TextView resultItem = holder.itemView.findViewById(R.id.result_item);

                    gameNumberItem.setText(String.valueOf(game.id));
                    dateItem.setText(game.getFormattedDate());
                    guessesItem.setText(String.valueOf(game.guesses));
                    resultItem.setText(game.is_win ? getString(R.string.history_win_text) : getString(R.string.history_loss_text));
                    resultItem.setTextColor(game.is_win ? getColor(R.color.history_result_green) : getColor(R.color.history_result_red));
                }

                @Override
                public int getItemCount() {
                    return games.size();
                }
            });
        });

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> finish());
    }
}