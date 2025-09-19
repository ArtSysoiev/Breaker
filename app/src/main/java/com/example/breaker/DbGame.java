package com.example.breaker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "games")
public class DbGame {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "guesses")
    public int guesses;

    @ColumnInfo(name = "is_win")
    public boolean is_win;

    public DbGame(int guesses, boolean is_win) {
        this.date = System.currentTimeMillis();
        this.guesses = guesses;
        this.is_win = is_win;
    }

    public String getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM  HH:mm:ss", Locale.getDefault());
        return formatter.format(new Date(this.date));
    }
}
