package p1803242.jeuxanglais.PacMan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;

import p1803242.jeuxanglais.AdminManager.DatabaseManager;
import p1803242.jeuxanglais.MainActivity;
import p1803242.jeuxanglais.R;

public class DrawingView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private Thread thread;
    private SurfaceHolder holder;
    private boolean canDraw = true;

    private Paint paint;
    private Bitmap pacmanRight, pacmanDown, pacmanLeft, pacmanUp;
    private Bitmap ghostBitmap;
    private int totalFrame = 4;             // Total amount of frames fo each direction
    private long frameTicker;               // Current time since last frame has been drawn
    private int xPosPacman;                 // x-axis position of pacman
    private int yPosPacman;                 // y-axis position of pacman
    private int xPosGhost;                  // x-axis position of ghost
    private int yPosGhost;                  // y-axis position of ghost
    int xDistance;
    int yDistance;
    private float x1, x2, y1, y2;           // Initial/Final positions of swipe
    private int direction = 4;              // Direction of the swipe, initial direction is right
    private int nextDirection = 4;          // Buffer for the next direction you choose
    private int viewDirection = 2;          // Direction that pacman is facing
    private int ghostDirection;
    private int screenWidth;                // Width of the phone screen
    private int blockSize;                  // Size of a block on the map
    DatabaseManager database;
    Context context;

    int[][] letterPos = null;
    int life;
    Bitmap[] lifeCounter = new Bitmap[3];
    boolean invincibility;
    char[] letterFound;
    int nbLetterFound = 0;
    int wordlength;
    String word;
    PlayActivity container;
    Globals g;

    public DrawingView(Context context, PlayActivity act) {
        super(context);
        this.context = context;

        container = act;

        database = DatabaseManager.getInstance();

        holder = getHolder();
        holder.addCallback(this);
        frameTicker = 1000/totalFrame;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        blockSize = screenWidth/17;
        blockSize = (blockSize / 5) * 5;
        xPosGhost = 8 * blockSize;
        yPosGhost = 4 * blockSize;
        ghostDirection = 4;
        xPosPacman = 8 * blockSize;
        yPosPacman = 13 * blockSize;

        loadBitmapImages();
        Log.i("info", "Constructor");

        g = Globals.getInstance();
        if(g.getCurScore() <= 6){
            wordlength = g.getCurScore() + 4;
        }
        else{
            wordlength = -1;
        }

        life = g.getLife();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        Looper.prepare();

        Log.i("info", "Run");

        if(wordlength == -1){
            word = database.getWord();
            wordlength = word.length();
            if(word.contains(" "))
                wordlength--;
        }
        else{
            word = database.getWord(wordlength);
        }
        paint.setTextSize(50);

        invincibility = false;

        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            // Set background color to Transparent
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                drawMap(canvas);

                moveGhost(canvas);

                if(invincibility == false)
                    isGameLost();

                drawLives(canvas);

                // Draw the word
                drawWord(canvas);

                // Moves the pacman based on his direction
                movePacman(canvas);

                isGameWon();

                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void isGameWon(){
        if(nbLetterFound == wordlength){
            g.setCurScore(g.getCurScore() + 1);
            Intent verifIntent = new Intent(getContext(), WordVerifActivity.class);
            verifIntent.putExtra("foundWord", String.valueOf(letterFound));
            verifIntent.putExtra("correctWord", word);
            getContext().startActivity(verifIntent);
        }
    }

    private void isGameLost(){
        if(Math.abs(xDistance) <= 30 && Math.abs(yDistance) <= 30){
            life -= 1;
            g.setLife(life - 1);
            if(life <= 0){
                Intent backHome = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(backHome);
            }

            xPosGhost = 8 * blockSize;
            yPosGhost = 4 * blockSize;

            xPosPacman = 8 * blockSize;
            yPosPacman = 13 * blockSize;

            invincibility = true;

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    invincibility = false;
                    this.cancel();
                }
            }, 2000);
        }
    }

    public void drawLives(Canvas canvas) {
        if (life == 0){
            return;
        }
        if(life < 3){
            lifeCounter[life] = null;
        }
        int i = 0;
        while(i < 3 && lifeCounter[i] != null){
            canvas.drawBitmap(lifeCounter[i], i*blockSize, blockSize, paint);
            i++;
        }
    }

    public void moveGhost(Canvas canvas) {
        short ch;

        xDistance = xPosPacman - xPosGhost;
        yDistance = yPosPacman - yPosGhost;

        if ((xPosGhost % blockSize == 0) && (yPosGhost % blockSize == 0)) {
            ch = leveldata1[yPosGhost / blockSize][xPosGhost / blockSize];

            if (xPosGhost >= blockSize * 17) {
                xPosGhost = 0;
            }
            if (xPosGhost < 0) {
                xPosGhost = blockSize * 17;
            }


            if (xDistance >= 0 && yDistance >= 0) { // Move right and down
                if ((ch & 4) == 0 && (ch & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 1;
                    } else {
                        ghostDirection = 2;
                    }
                }
                else if ((ch & 4) == 0) {
                    ghostDirection = 1;
                }
                else if ((ch & 8) == 0) {
                    ghostDirection = 2;
                }
                else
                    ghostDirection = 3;
            }
            if (xDistance >= 0 && yDistance <= 0) { // Move right and up
                if ((ch & 4) == 0 && (ch & 2) == 0 ) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 1;
                    } else {
                        ghostDirection = 0;
                    }
                }
                else if ((ch & 4) == 0) {
                    ghostDirection = 1;
                }
                else if ((ch & 2) == 0) {
                    ghostDirection = 0;
                }
                else ghostDirection = 2;
            }
            if (xDistance <= 0 && yDistance >= 0) { // Move left and down
                if ((ch & 1) == 0 && (ch & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 3;
                    } else {
                        ghostDirection = 2;
                    }
                }
                else if ((ch & 1) == 0) {
                    ghostDirection = 3;
                }
                else if ((ch & 8) == 0) {
                    ghostDirection = 2;
                }
                else ghostDirection = 1;
            }
            if (xDistance <= 0 && yDistance <= 0) { // Move left and up
                if ((ch & 1) == 0 && (ch & 2) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 3;
                    } else {
                        ghostDirection = 0;
                    }
                }
                else if ((ch & 1) == 0) {
                    ghostDirection = 3;
                }
                else if ((ch & 2) == 0) {
                    ghostDirection = 0;
                }
                else ghostDirection = 2;
            }
            // Handles wall collisions
            if ( (ghostDirection == 3 && (ch & 1) != 0) ||
                    (ghostDirection == 1 && (ch & 4) != 0) ||
                    (ghostDirection == 0 && (ch & 2) != 0) ||
                    (ghostDirection == 2 && (ch & 8) != 0) ) {
                ghostDirection = 4;
            }
        }

        if (ghostDirection == 0) {
            yPosGhost += -blockSize / 13;
        } else if (ghostDirection == 1) {
            xPosGhost += blockSize / 13;
        } else if (ghostDirection == 2) {
            yPosGhost += blockSize / 13;
        } else if (ghostDirection == 3) {
            xPosGhost += -blockSize / 13;
        }

        canvas.drawBitmap(ghostBitmap, xPosGhost + 2, yPosGhost + 2, paint);
    }

    // Updates the character sprite and handles collisions
    public void movePacman(Canvas canvas) {
        short ch;

        // Check if xPos and yPos of pacman is both a multiple of block size
        if ( (xPosPacman % blockSize == 0) && (yPosPacman  % blockSize == 0) ) {

            // When pacman goes through tunnel on
            // the right reappear at left tunnel
            if (xPosPacman >= blockSize * 17) {
                xPosPacman = 0;
            }

            // Is used to find the number in the level array in order to
            // check wall placement, pellet placement, and candy placement
            ch = leveldata1[yPosPacman / blockSize][xPosPacman / blockSize];

            // If there is a pellet, eat it
            if ((ch & 16) == 0) {
                // Toggle pellet so it won't be drawn anymore
                leveldata1[yPosPacman / blockSize][xPosPacman / blockSize] = (short) (ch ^ 16);
                addLetterToList();
            }

            // Checks for direction buffering
            if (!((nextDirection == 3 && (ch & 1) != 0) ||
                    (nextDirection == 1 && (ch & 4) != 0) ||
                    (nextDirection == 0 && (ch & 2) != 0) ||
                    (nextDirection == 2 && (ch & 8) != 0))) {
                viewDirection = direction = nextDirection;
            }

            // Checks for wall collisions
            if ((direction == 3 && (ch & 1) != 0) ||
                    (direction == 1 && (ch & 4) != 0) ||
                    (direction == 0 && (ch & 2) != 0) ||
                    (direction == 2 && (ch & 8) != 0)) {
                direction = 4;
        }
        }

        // When pacman goes through tunnel on
        // the left reappear at right tunnel
        if (xPosPacman < 0) {
            xPosPacman = blockSize * 17;
        }

        drawPacman(canvas);

        // Depending on the direction move the position of pacman
        if (direction == 0) {
            yPosPacman += -blockSize/15;
        } else if (direction == 1) {
            xPosPacman += blockSize/15;
        } else if (direction == 2) {
            yPosPacman += blockSize/15;
        } else if (direction == 3) {
            xPosPacman += -blockSize/15;
        }
    }

    // Method that draws pacman based on his viewDirection
    public void drawPacman(Canvas canvas) {
        switch (viewDirection) {
            case (0):
                canvas.drawBitmap(pacmanUp, xPosPacman + 2, yPosPacman + 2, paint);
                break;
            case (1):
                canvas.drawBitmap(pacmanRight, xPosPacman + 2, yPosPacman + 2, paint);
                break;
            case (3):
                canvas.drawBitmap(pacmanLeft, xPosPacman +  2, yPosPacman + 2, paint);
                break;
            default:
                canvas.drawBitmap(pacmanDown, xPosPacman + 2, yPosPacman + 2, paint);
                break;
        }
    }

    // Method that draws letters and updates them
    public void drawWord(Canvas canvas) {
        int x;
        int y;

        if(letterPos != null){
            for(int i = 0; i < wordlength; i++){
                x = letterPos[i][0];
                y = letterPos[i][1];

                if((leveldata1[(y/blockSize)][(x/blockSize)] & 16) >= 16 ){
                    continue;
                }

                canvas.drawText(String.valueOf(word.charAt(i)), x + 20, y + blockSize - 10, paint);
            }
        }
        else{
            letterPos = new int[wordlength][2];
            letterFound = new char[wordlength];
            for(int i = 0; i < wordlength; i++){
                int j = (int)(Math.random()*16);
                int k = (int)(Math.random()*17);

                x = k * blockSize;
                y = j * blockSize;

                if ((leveldata1[j][k] & 16) == 0){
                    i--;
                }
                else{
                    canvas.drawText(String.valueOf(word.charAt(i)), x + 20, y + blockSize - 10, paint);
                    letterPos[i][0] = x;
                    letterPos[i][1] = y;
                    leveldata1[(y/blockSize)][(x/blockSize)] = (short) (leveldata1[(y/blockSize)][(x/blockSize)] ^ 16);
                }
            }
        }
    }

    // Method to draw map layout
    public void drawMap(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2.5f);
        int x;
        int y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;
                if ((leveldata1[i][j] & 1) != 0) // draws left
                    canvas.drawLine(x + 2, y + 2, x + 2, y + blockSize + 1, paint);

                if ((leveldata1[i][j] & 2) != 0) // draws top
                    canvas.drawLine(x + 2, y + 2, x + blockSize + 1, y +2, paint);

                if ((leveldata1[i][j] & 4) != 0) // draws right
                    canvas.drawLine(
                             x + 2 + blockSize, y + 2, x + blockSize + 2, y + blockSize + 1, paint);
                if ((leveldata1[i][j] & 8) != 0) // draws bottom
                    canvas.drawLine(
                            x + 2, y + blockSize + 2, x + blockSize + 1, y + blockSize + 2, paint);
            }
        }
        paint.setColor(Color.WHITE);
    }

    // Method to add a letter to the list of the found letters
    public void addLetterToList(){
        for(int i = 0; i < wordlength; i++){
            if(letterPos[i][0] == xPosPacman && letterPos[i][1] == yPosPacman){
                letterFound[nbLetterFound] = word.charAt(i);
                nbLetterFound++;
            }
        }
    }

    // Method to get touch events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN): {
                x1 = event.getX();
                y1 = event.getY();
                break;
            }
            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                calculateSwipeDirection();
                break;
            }
        }
        return true;
    }

    // Calculates which direction the user swipes
    // based on calculating the differences in
    // initial position vs final position of the swipe
    private void calculateSwipeDirection() {
        float xDiff = (x2 - x1);
        float yDiff = (y2 - y1);

        // Directions
        // 0 means going up
        // 1 means going right
        // 2 means going down
        // 3 means going left
        // 4 means stop moving, look at move function

        // Checks which axis has the greater distance
        // in order to see which direction the swipe is
        // going to be (buffering of direction)
        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            if (yDiff < 0) {
                nextDirection = 0;
            } else if (yDiff > 0) {
                nextDirection = 2;
            }
        } else {
            if (xDiff < 0) {
                nextDirection = 3;
            } else if (xDiff > 0) {
                nextDirection = 1;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("info", "Surface Created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("info", "Surface Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("info", "Surface Destroyed");
    }

    public void pause() {
        Log.i("info", "pause");
        canDraw = false;
        thread = null;
    }

    public void resume() {
        Log.i("info", "resume");
        if (thread != null) {
            thread.start();
        }
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
            Log.i("info", "resume thread");
        }
        canDraw = true;
    }

    private void loadBitmapImages() {
        // Scales the sprites based on screen
        int spriteSize = screenWidth/17;        // Size of Pacman & Ghost
        spriteSize = (spriteSize / 5) * 5;      // Keep it a multiple of 5

        // Add bitmap images of pacman facing right
        pacmanRight = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing down
        pacmanDown = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing left
        pacmanLeft = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing up
        pacmanUp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up), spriteSize, spriteSize, false);

        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.fantom), spriteSize, spriteSize, false);

        lifeCounter[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
        lifeCounter[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
        lifeCounter[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
    }

    final short leveldata1[][] = new short[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {19, 26, 26, 18, 26, 26, 26, 22, 0, 19, 26, 26, 26, 18, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {17, 26, 26, 16, 26, 18, 26, 24, 26, 24, 26, 18, 26, 16, 26, 26, 20},
            {25, 26, 26, 20, 0, 25, 26, 22, 0, 19, 26, 28, 0, 17, 26, 26, 28},
            {0, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 19, 26, 24, 26, 24, 26, 22, 0, 21, 0, 0, 0},
            {26, 26, 26, 16, 26, 20, 0, 0, 0, 0, 0, 17, 26, 16, 26, 26, 26},
            {0, 0, 0, 21, 0, 17, 26, 26, 26, 26, 26, 20, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0},
            {19, 26, 26, 16, 26, 24, 26, 22, 0, 19, 26, 24, 26, 16, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {25, 22, 0, 21, 0, 0, 0, 17, 2, 20, 0, 0, 0, 21, 0, 19, 28}, // "2" in this line is for
            {0, 21, 0, 17, 26, 26, 18, 24, 24, 24, 18, 26, 26, 20, 0, 21, 0}, // pacman's spawn
            {19, 24, 26, 28, 0, 0, 25, 18, 26, 18, 28, 0, 0, 25, 26, 24, 22},
            {21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21},
            {25, 26, 26, 26, 26, 26, 26, 24, 21, 24, 26, 26, 26, 26, 26, 26, 28},
    };
}
