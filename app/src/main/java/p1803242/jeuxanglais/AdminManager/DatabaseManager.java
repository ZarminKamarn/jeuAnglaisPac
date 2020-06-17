package p1803242.jeuxanglais.AdminManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseManager {
    private static SQLiteDatabase database;
    private static DatabaseManager instance = null;
    private static int lengthDb;

    private DatabaseManager(){
        try{
            database = SQLiteDatabase.openDatabase(
                    "/data/user/0/p1803242.jeuxanglais/databases/db.sqlite",
                    null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch(Exception ex){
            database = SQLiteDatabase.openOrCreateDatabase(
                    "/data/user/0/p1803242.jeuxanglais/databases/db.sqlite",
                    null);
            fillDb();
        }

        Cursor cs = database.rawQuery("Select * from Words", null);
        lengthDb = cs.getCount();
    }

    private void fillDb(){
        database.execSQL("create table if not exists Words(id integer primary key autoincrement, word string, length string);");
        database.execSQL("insert into words values(NULL, 'DIAL-UP', '7');");
        database.execSQL("insert into words values(NULL, 'DSL', '3');");
        database.execSQL("insert into words values(NULL, 'BROADBAND', '9');");
        database.execSQL("insert into words values(NULL, 'CABLE', '5');");
        database.execSQL("insert into words values(NULL, 'SATELLITE', '9');");
        database.execSQL("insert into words values(NULL, 'OPTICAL FIBER', '12');");
        database.execSQL("insert into words values(NULL, 'ISP', '3');");
        database.execSQL("insert into words values(NULL, 'MODEM', '5');");
        database.execSQL("insert into words values(NULL, 'ROUTER', '6');");
        database.execSQL("insert into words values(NULL, 'NETWORK', '7');");
        database.execSQL("insert into words values(NULL, 'BROWSER', '7');");
        database.execSQL("insert into words values(NULL, 'TOWER CASE', '9');");
        database.execSQL("insert into words values(NULL, 'POWER SOCKET', '11');");
        database.execSQL("insert into words values(NULL, 'ETHERNET', '8');");
        database.execSQL("insert into words values(NULL, 'MOUSE', '5');");
        database.execSQL("insert into words values(NULL, 'KEYBOARD', '8');");
        database.execSQL("insert into words values(NULL, 'USB', '3');");
        database.execSQL("insert into words values(NULL, 'FLOPPY DISK', '10');");
        database.execSQL("insert into words values(NULL, 'DATABASE', '8');");
        database.execSQL("insert into words values(NULL, 'LINK', '4');");
        database.execSQL("insert into words values(NULL, 'DIRECTORY', '9');");
        database.execSQL("insert into words values(NULL, 'CLIPBOARD', '9');");
        database.execSQL("insert into words values(NULL, 'CURSOR', '6');");
        database.execSQL("insert into words values(NULL, 'INBOX', '5');");
        database.execSQL("insert into words values(NULL, 'PROGRAM', '7');");
        database.execSQL("insert into words values(NULL, 'LAPTOP', '6');");
        database.execSQL("insert into words values(NULL, 'SEARCH ENGINE', '12');");
        database.execSQL("insert into words values(NULL, 'SCREEN', '6');");
        database.execSQL("insert into words values(NULL, 'DOT', '3');");
        database.execSQL("insert into words values(NULL, 'HOMEPAGE', '8');");
        database.execSQL("insert into words values(NULL, 'SYSTEM FILES', '11');");
        database.execSQL("insert into words values(NULL, 'PASSWORD', '8');");
        database.execSQL("insert into words values(NULL, 'LOGIN', '5');");
        database.execSQL("insert into words values(NULL, 'DOWNLOAD', '8');");
        database.execSQL("insert into words values(NULL, 'UPLOAD', '6');");
        database.execSQL("insert into words values(NULL, 'FILE', '4');");
        database.execSQL("insert into words values(NULL, 'SCAN', '4');");
        database.execSQL("insert into words values(NULL, 'COPY', '4');");
        database.execSQL("insert into words values(NULL, 'PASTE', '5');");
        database.execSQL("insert into words values(NULL, 'UNDERSCORE', '10');");
        database.execSQL("insert into words values(NULL, 'PRINTER', '7');");
        database.execSQL("insert into words values(NULL, 'SOFTWARE', '8');");
        database.execSQL("insert into words values(NULL, 'SHORTCUT', '8');");
        database.execSQL("insert into words values(NULL, 'CLICK', '5');");
        database.execSQL("insert into words values(NULL, 'SAVE', '4');");
        database.execSQL("insert into words values(NULL, 'TRASH', '5');");
        database.execSQL("insert into words values(NULL, 'CPU', '3');");
        database.execSQL("insert into words values(NULL, 'WEBMASTER', '9');");
        database.execSQL("insert into words values(NULL, 'DELETE', '6');");
        database.execSQL("insert into words values(NULL, 'INTERNET', '8');");
        database.execSQL("insert into words values(NULL, 'KEYWORD', '7');");
        database.execSQL("insert into words values(NULL, 'REFERENCING', '11');");
        database.execSQL("insert into words values(NULL, 'DEVICE', '6');");
        database.execSQL("insert into words values(NULL, 'OPERATING SYSTEM', '15');");
        database.execSQL("insert into words values(NULL, 'TOUCHSCREEN', '11');");
        database.execSQL("insert into words values(NULL, 'RESPONSIVE', '10');");
        database.execSQL("insert into words values(NULL, 'TABLET', '6');");
        database.execSQL("insert into words values(NULL, 'SMARTPHONE', '10');");
        database.execSQL("insert into words values(NULL, 'BACK-UP', '7');");
        database.execSQL("insert into words values(NULL, 'CLOUD', '5');");
        database.execSQL("insert into words values(NULL, 'GEEK', '4');");
        database.execSQL("insert into words values(NULL, 'NERD', '4');");
        database.execSQL("insert into words values(NULL, 'ROBOTICS', '8');");
        database.execSQL("insert into words values(NULL, 'RESULT', '6');");
        database.execSQL("insert into words values(NULL, 'VIRTUAL REALITY', '14');");
        database.execSQL("insert into words values(NULL, 'PYTHON', '6');");
        database.execSQL("insert into words values(NULL, 'JAVASCRIPT', '10');");
        database.execSQL("insert into words values(NULL, 'MICROSOFT', '9');");
        database.execSQL("insert into words values(NULL, 'INTERFACE', '9');");
        database.execSQL("insert into words values(NULL, 'COMPUTER', '8');");
        database.execSQL("insert into words values(NULL, 'MONITOR', '7');");
        database.execSQL("insert into words values(NULL, 'SERVER', '6');");
        database.execSQL("insert into words values(NULL, 'MOTHERBOARD', '11');");
        database.execSQL("insert into words values(NULL, 'HARD DRIVE', '9');");
        database.execSQL("insert into words values(NULL, 'RAM', '3');");
    }

    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public String getWord(int length){
        String[] args = {String.valueOf(length)};
        Cursor cs = database.rawQuery("Select word from Words where length=?", args);

        int nbWords = cs.getCount();
        int rand = (int)(Math.random()*nbWords);
        cs.moveToPosition(rand);

        String s = cs.getString(0);
        return s;
    }

    public String getWord(){
        Cursor cs = database.rawQuery("Select word from Words", null);

        int rand = (int)(Math.random()*lengthDb);
        cs.moveToPosition(rand);

        String s = cs.getString(0);
        return s;
    }

    public void addWord(String wordToAdd){
        int length = wordToAdd.length();
        if(wordToAdd.contains(" ")){
            length--;
        }

        String[] args = {wordToAdd, String.valueOf(length)};

        database.execSQL("insert into words values(NULL, ?, ?);", args);
    }

    public ArrayList<String> getWords(){
        ArrayList<String> words = new ArrayList<>();
        Cursor cs = database.rawQuery("Select word from Words", null);

        cs.moveToFirst();
        while(!cs.isAfterLast()){
            words.add(cs.getString(0));
            cs.moveToNext();
        }

        return words;
    }

    public void updateWord(String oldWord, String newWord){
        int length = newWord.length();
        if(newWord.contains(" ")){
            length--;
        }

        String[] args = {newWord, String.valueOf(length), oldWord};

        database.execSQL("update words set word = ?, length = ? where word = ?;", args);
    }

    public void deleteWord(String wordToDelete){
        String[] args = {wordToDelete};

        database.execSQL("delete from words where word = ?;", args);
    }
}
