package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.interfaces.MusicService;
import com.example.myapplication.models.Song;
import com.example.myapplication.stubs.MusicServiceStub;
import com.example.myapplication.stubs.MusicWish;
import com.example.myapplication.stubs.PlaylistService;
import com.example.myapplication.stubs.FeedbackService;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private MusicService musicService;
    private MusicWish musicWish;
    private PlaylistService playlistService;
    private FeedbackService feedbackService;
    private ScheduledExecutorService scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisierung des MusicService
        musicService = new MusicServiceStub();
        TextView textView = findViewById(R.id.textView);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleSongUpdates(textView);

        musicWish = new MusicWish();
        playlistService = new PlaylistService();
        feedbackService = new FeedbackService();

        // Button 1: Songwunsch
        Button requestSongButton = findViewById(R.id.btn1);
        requestSongButton.setOnClickListener(v -> showSongRequestDialog());

        // Button 2: Playlist bewerten
        Button ratePlaylistButton = findViewById(R.id.btn2);
        ratePlaylistButton.setOnClickListener(v -> showRateDialog());

        // Button 3: Feedback an den Kommentator
        Button feedbackButton = findViewById(R.id.btn3);
        feedbackButton.setOnClickListener(v -> showFeedbackDialog());
    }

    // Regelmäßige Aktualisierung der Songinformationen
    private void scheduleSongUpdates(TextView textView) {
        scheduler.scheduleAtFixedRate(() -> {
            Song currentSong = musicService.getCurrentSongInfo();
            runOnUiThread(() -> textView.setText("Titel: " + currentSong.getTitle() +
                    "\nInterpret: " + currentSong.getArtist() +
                    "\nAlbum: " + currentSong.getAlbum() +
                    "\nJahr: " + currentSong.getYear()));
        }, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduler.shutdownNow();
    }

    // Button 1 Songtext wünschen - Dialogfeld
    private void showSongRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_request_song, null);
        builder.setView(dialogView);

        EditText songRequestInput = dialogView.findViewById(R.id.edit_song_request);
        Button submitButton = dialogView.findViewById(R.id.button_submit);

        AlertDialog dialog = builder.create();
        dialog.show();

        submitButton.setOnClickListener(v -> {
            String songRequest = songRequestInput.getText().toString();
            musicWish.requestSong(songRequest);
            Toast.makeText(MainActivity.this, "Dein Songwunsch wurde erfolgreich übermittelt.", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
    }

    // Button 2 Playlist bewerten - Sternenbewertung
    private void showRateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rate_playlist, null);
        builder.setView(dialogView);

        final RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        final EditText commentInput = dialogView.findViewById(R.id.edit_comment);
        Button submitButton = dialogView.findViewById(R.id.button_submit_rating);

        AlertDialog dialog = builder.create();
        dialog.show();

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = commentInput.getText().toString();
            playlistService.ratePlaylist(rating, comment);  // Angenommen, die Methode ratePlaylist unterstützt jetzt Kommentare
            Toast.makeText(MainActivity.this, "Danke für Deine Bewertung!", Toast.LENGTH_LONG).show();
            dialog.dismiss();

        });
    }

    // Button 3 Feedback an den Kommentator - Sternenbewertung
    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_feedback_commentator, null);
        builder.setView(dialogView);

        final RatingBar ratingBar = dialogView.findViewById(R.id.commentatorRatingBar);
        final EditText commentInput = dialogView.findViewById(R.id.edit_comment);
        Button submitButton = dialogView.findViewById(R.id.submitFeedbackButton);

        AlertDialog dialog = builder.create();
        dialog.show();

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = commentInput.getText().toString();
            feedbackService.sendFeedback(rating, comment);  // Angenommen, die Methode sendFeedback unterstützt jetzt Kommentare
            Toast.makeText(MainActivity.this, "Danke für Dein Feedback!", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
    }
}