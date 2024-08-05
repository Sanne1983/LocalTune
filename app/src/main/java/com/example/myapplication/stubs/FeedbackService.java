package com.example.myapplication.stubs;
import android.util.Log;

public class FeedbackService {
    // Methode zum Senden von Feedback an den Kommentator
    public void sendFeedback(float rating, String comment) {
        // Simuliere das Speichern des Feedbacks
        Log.d("FeedbackService", "Feedback received: Rating: " + rating + ", Comment: " + comment);
        // Speichern des Feedbacks und ggf. des Kommentars in einer Datenbank oder Senden an einen Server
    }
}
