package p1803242.jeuxanglais.AdminManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import p1803242.jeuxanglais.R;

public class AdminUpdateActivity extends AppCompatActivity {
    ListView listView;
    TextView textView;
    ArrayList<String> listWords;
    DatabaseManager database;

    String selectedItem = null;
    String newWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);

        textView = findViewById(R.id.updInput);
        database = DatabaseManager.getInstance();

        listWords = database.getWords();
        listView = findViewById(R.id.listViewWordUpd);
        final MyAdapter adapter = new MyAdapter(AdminUpdateActivity.this, listWords);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = listWords.get(position);
            }
        });
    }

    public void onUpdBtnClick(View view){
        newWord = textView.getText().toString().toUpperCase();
        if(newWord == ""){
            this.finish();
        }

        database.updateWord(selectedItem, newWord);
        this.finish();
    }
}
