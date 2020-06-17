package p1803242.jeuxanglais.PacMan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import p1803242.jeuxanglais.MainActivity;
import p1803242.jeuxanglais.R;

public class WordVerifActivity extends AppCompatActivity {
    private String wordFromGame;
    private String correctWord;
    private String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_verif);

        Intent intent = getIntent();
        wordFromGame = intent.getStringExtra("foundWord");
        correctWord = intent.getStringExtra("correctWord");

        TextView textView = findViewById(R.id.textViewVerif);
        textView.setText(textView.getText() + wordFromGame);
    }

    public void onBtnValClick(View view){
        TextView textView = findViewById(R.id.wordInput);
        input = textView.getText().toString();
        if(correctWord.toLowerCase().equals(input.toLowerCase())){
            Intent resumeIntent = new Intent(this, PlayActivity.class);
            resumeIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(resumeIntent);
            this.finish();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("La bonne réponse était " + correctWord);
            builder.setTitle("FAUX");
            builder.setNeutralButton("quitter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent quitIntent = new Intent(WordVerifActivity.this, MainActivity.class);
                    startActivity(quitIntent);
                    WordVerifActivity.this.finish();
                }
            });

            AlertDialog infos = builder.create();
            infos.show();
        }
    }
}
