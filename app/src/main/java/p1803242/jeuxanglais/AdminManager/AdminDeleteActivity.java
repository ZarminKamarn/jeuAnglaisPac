package p1803242.jeuxanglais.AdminManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import p1803242.jeuxanglais.R;

public class AdminDeleteActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> listWords;
    DatabaseManager database;

    String selectedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete);

        database = DatabaseManager.getInstance();

        listWords = database.getWords();
        listView = findViewById(R.id.listViewWordDel);
        final MyAdapter adapter = new MyAdapter(AdminDeleteActivity.this, listWords);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = listWords.get(position);
            }
        });
    }

    public void onDelBtnClick(View view){
        database.deleteWord(selectedItem);
        this.finish();
    }
}
