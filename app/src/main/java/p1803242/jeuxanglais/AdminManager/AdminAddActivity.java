package p1803242.jeuxanglais.AdminManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import p1803242.jeuxanglais.R;

public class AdminAddActivity extends AppCompatActivity {
    TextView input;
    DatabaseManager database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        input = findViewById(R.id.addInput);
        database = DatabaseManager.getInstance();
    }

    public void onBtnAddClick(View view){
        String word = input.getText().toString().toUpperCase();
        if(word == ""){
            this.finish();
        }

        database.addWord(word);
        this.finish();
    }
}
