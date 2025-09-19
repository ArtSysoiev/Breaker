package com.example.breaker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DbGame.class}, version = 1)
public abstract class DB extends RoomDatabase {
    public abstract DbDao DbDao();
}
