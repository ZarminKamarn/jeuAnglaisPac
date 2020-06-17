package p1803242.jeuxanglais;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import p1803242.jeuxanglais.AdminManager.MainAdminActivity;
import p1803242.jeuxanglais.PacMan.Globals;
import p1803242.jeuxanglais.PacMan.HelpActicity;
import p1803242.jeuxanglais.PacMan.PlayActivity;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(player == null){
            player = MediaPlayer.create(this, R.raw.pacman_music);
            player.setVolume(100, 100);
            player.setLooping(true);
            player.start();
        }

        Globals g = Globals.getInstance();
        g.setLife(3);
        g.setCurScore(0);
    }

    public void onPacBtnClick(View view){
        Intent myIntent = new Intent(MainActivity.this, PlayActivity.class);
        startActivity(myIntent);
    }

    public void onAdminBtnClick(View view){
        Intent myIntent = new Intent(MainActivity.this, MainAdminActivity.class);
        startActivity(myIntent);
    }

    public void onHelpBtnClick(View view){
        Intent myIntent = new Intent(MainActivity.this, HelpActicity.class);
        startActivity(myIntent);
    }

    public static MediaPlayer getPlayer() {
        return player;
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onResume() {
        Log.i("info", "MainActivity onResume");
        super.onResume();
        player.start();
    }
}
