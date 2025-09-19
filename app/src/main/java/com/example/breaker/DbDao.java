package com.example.breaker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DbDao {
    @Query("SELECT * FROM games ORDER BY date DESC")
    LiveData<List<DbGame>> getAll();

    @Query("SELECT COUNT(*) FROM games WHERE is_win = 1")
    LiveData<Integer> getWinsCount();

    @Query("SELECT COUNT(*) FROM games")
    LiveData<Integer> getGamesCount();

    @Insert
    void insert(DbGame game);

}
