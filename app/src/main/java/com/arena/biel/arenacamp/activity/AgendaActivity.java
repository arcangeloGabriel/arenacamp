package com.arena.biel.arenacamp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arena.biel.arenacamp.R;

public class AgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        getSupportActionBar().setTitle("Agenda");
    }
}
