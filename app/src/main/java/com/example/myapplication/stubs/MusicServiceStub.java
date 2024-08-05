package com.example.myapplication.stubs;

import com.example.myapplication.interfaces.MusicService;
import com.example.myapplication.models.Song;

public class MusicServiceStub implements MusicService {
    @Override
    public Song getCurrentSongInfo() {
        // Rückgabe vordefinierter Werte
        return new Song("Bohemian Rhapsody", "Queen", "A Night at the Opera", 1975);
    }
}
