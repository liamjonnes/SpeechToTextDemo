//Credit: http://mrbool.com/how-to-create-an-speechtotext-app-for-android/26279
package com.example.liam.speechtotextdemo;

import android.os.Bundle;
import java.util.ArrayList;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;

    private ImageButton btnSpeak;
    private EditText userSaid; //single lined text box where user is able to edit what they've said
    private EditText spokenWords; //multi-lined text box where the array 'array' gets printed
    private Button btnYes;
    private TextView finishedSpeaking; //text that appears when the user has finished speaking
    public ArrayList<String> words; //stores the list of words into an arrayList so there is no limit to the words that can be used
    private String spokenWord; //stores the String of the TextView item
    public String[] array; //used to store the items in the arrayList and gets printed

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSaid = (EditText) findViewById(R.id.userSaid);
        finishedSpeaking = (TextView) findViewById(R.id.finishedSpeaking);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnYes = (Button) findViewById(R.id.btnYes);
        spokenWords = (EditText) findViewById(R.id.editText);

        //when the button to speak is pressed, do the following
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //the popup for the speech recognition using Googles Speech-to-Text API within the phone
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-GB");

                //tries to gather text from the spoken words; if spoken language is not available, an error is thrown.
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    //initialises the text field
                    userSaid.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //once the user has spoken,
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //once the user has spoken, the 'Yes' button becomes visible, along with what the user has said
        userSaid.setVisibility(View.VISIBLE);
        btnYes.setVisibility(View.VISIBLE);

        //converts what the user has said into an array of characters and then combines them into a String
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //prints out what the user has said along with another message
                    userSaid.setText(text.get(0));
                    finishedSpeaking.setText("If the above text is correct, please press 'Yes'." +
                                "\n" + "If it is not correct, please edit it within the above box.");
                }
                break;
            }
        }

        //things that happen upon tapping the 'Yes' button
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spokenWords.setVisibility(View.VISIBLE);    //sets the visibility of the multi-lined text box to true
                finishedSpeaking.setText("Words and phrases that you have said:");
                btnYes.setVisibility(View.INVISIBLE);

                //creates a new arrayList then adds the spoken word to the arrayList
                words = new ArrayList<String>();
                spokenWord = userSaid.getText().toString();
                words.add(spokenWord);

                StringBuilder sb = new StringBuilder();

                //creates a new array and converts the arrayList to an array
                array = new String[words.size()];
                for(int i = 0; i < words.size(); i++) {
                    array[i] = words.get(i);
                }

                //adds each item of the array to a new line using a sb, then prints the built string to the multi-lined text box
                for (String string : array) {
                    sb.append(string).append("\n");
                }
                spokenWords.append(sb.toString());
                //userSaid.setVisibility(View.INVISIBLE);
                userSaid.setText("");
                userSaid.setHint("Input another word/phrase");
            }

        });
    }


    //public String getSpokenWord() {
    //    return spokenWord;
    //}
}
