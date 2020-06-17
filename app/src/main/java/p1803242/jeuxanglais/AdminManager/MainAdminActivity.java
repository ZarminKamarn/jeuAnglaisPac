package p1803242.jeuxanglais.AdminManager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import p1803242.jeuxanglais.R;

public class MainAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
    }

    public void onAddBtnClick(View view){
        Intent addIntent = new Intent(MainAdminActivity.this, AdminAddActivity.class);
        startActivity(addIntent);
    }

    public void onUpdBtnClick(View view){
        Intent updIntent = new Intent(MainAdminActivity.this, AdminUpdateActivity.class);
        startActivity(updIntent);
    }

    public void onDelBtnClick(View view){
        Intent delIntent = new Intent(MainAdminActivity.this, AdminDeleteActivity.class);
        startActivity(delIntent);
    }
}
