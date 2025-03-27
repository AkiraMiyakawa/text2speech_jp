package com.example.text2speech;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;  // Declare the TextToSpeech object
    private EditText editText; // Declare EditText to input text
    private Button speakButton; // Declare Button to trigger TTS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Link the activity with the XML layout

        // Initialize the EditText and Button
        editText = findViewById(R.id.editText);
        speakButton = findViewById(R.id.speakButton);

        // Initialize the TextToSpeech object with OnInitListener
        tts = new TextToSpeech(this, new OnInitListener() {
            @Override
            public void onInit(int status) {
                // Check if the TTS engine has been successfully initialized
                if (status == TextToSpeech.SUCCESS) {
                    // Set the language to Japanese
                    int langResult = tts.setLanguage(Locale.JAPAN);

                    // Check if Japanese language is supported and available
                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Handle the error if Japanese language is not supported
                        System.out.println("Japanese language data is missing or not supported.");
                    } else {
                        System.out.println("TTS Initialized with Japanese Language.");
                    }
                } else {
                    // Handle error in TTS initialization
                    System.out.println("Error initializing TTS.");
                }
            }
        });

        // Set an OnClickListener on the button
        speakButton.setOnClickListener(view -> {
            // Get the text input from the EditText field
            String textToSpeak = editText.getText().toString();

            // Check if the text is not empty
            if (!textToSpeak.isEmpty()) {
                // Speak the entered text
                tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                System.out.println("Please enter some text to speak.");
            }
        });

        // Optional: Set a listener to know when speech starts, finishes, or encounters errors
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // This method is triggered when the speech starts
                System.out.println("Speech started");
            }

            @Override
            public void onDone(String utteranceId) {
                // This method is triggered when the speech finishes
                System.out.println("Speech finished");
            }

            @Override
            public void onError(String utteranceId) {
                // This method is triggered if there is an error during speech
                System.out.println("Speech encountered an error");
            }
        });
    }

    // Make sure to clean up resources when the activity is destroyed
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();   // Stop speaking if it's still running
            tts.shutdown();   // Release the resources used by the TTS engine
        }
        super.onDestroy();
    }
}
