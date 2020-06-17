package p1803242.jeuxanglais.PacMan;

public class Globals{
    private static Globals instance;

    // Global variable
    private int highScore;
    private int curScore = 0;
    private int life = 3;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setHighScore(int newScore){
        this.highScore =newScore;
    }
    public int getHighScore(){
        return this.highScore;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    public int getCurScore() {
        return curScore;
    }

    public void setCurScore(int curScore) {
        this.curScore = curScore;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}