package com.ua.viktor.game2048.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;

import com.ua.viktor.game2048.R;
import com.ua.viktor.game2048.activity.MainActivity;

/**
 * Created by viktor on 10.12.15.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class GameView extends GridLayout {
    public Game2048 mGame2048 = new Game2048();
    private Card[][] grid = new Card[4][4];
    private Animation leftAnimation;
    private Animation rigtAnimation;
    private Animation apperiang;

    //private List<Point> emptyPoints = new ArrayList<Point>();
    public GameView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initGame();


    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initGame();
        leftAnimation = AnimationUtils.loadAnimation(context, R.anim.left_anim);
        apperiang = AnimationUtils.loadAnimation(context, R.anim.appearing);
        rigtAnimation = AnimationUtils.loadAnimation(context, R.anim.rigth_anim);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initGame();
    }

    public void initGame() {
        // MainActivity.getMainActivity().clearScore();

        setBackgroundColor(0x00cc33);
        setOnTouchListener(new OnTouchListener() {
            float startX, startY, offsetX, offsetY;

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        startX = event.getX();
                        startY = event.getY();

                        break;
                    case MotionEvent.ACTION_UP:

                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                moveLeft();

                            } else if (offsetX > 5) {
                                moveRight();

                            }
                        } else {
                            if (offsetY < -5) {
                                moveDown();

                            } else if (offsetY > 5) {
                                moveUp();

                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWdith = (Math.min(w, h) - 10) / 4;
        addCard(cardWdith, cardWdith);
        upgradeGrid();

    }

    private void addCard(int cardWdith, int cardHeight) {
        Card c;
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                addView(c, cardWdith, cardHeight);
                grid[x][y] = c;
            }
    }


    private void upgradeGrid() {

        int[][] field = mGame2048.getField();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
             //   grid[i][j].startAnimation(apperiang);
                grid[i][j].setNum(field[i][j]);

            }

        }
        if (isSound()) {
            MainActivity.getMainActivity().playSound();
        }


        if (mGame2048.lost()) {
            GameOver();
        } else if (mGame2048.won()) {
            checkScore();
        }

    }

    private boolean isSound() {
        boolean var = false;
        Changes[] myChange = mGame2048.getChanges();
        for (int i = 0; i < myChange.length; i++) {
            for (int j = i + 1; j < myChange.length; j++) {
                if (myChange[i].newPositionX == myChange[j].oldPositionX && myChange[i].newPositionY == myChange[j].oldPositionY)

                {
                    var = true;
                }

            }
        }
        return var;
    }


    private void moveLeft() {

        mGame2048.moveUp();

        /*for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j].startAnimation(leftAnimation);
            }

        }*/
        upgradeGrid();

        MainActivity.getMainActivity().showScore(mGame2048.getScore());

    }


    private void moveRight() {

        mGame2048.moveDown();
       /* for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j].startAnimation(rigtAnimation);
            }

        }*/
        upgradeGrid();
        MainActivity.getMainActivity().showScore(mGame2048.getScore());
    }


    private void moveDown() {

        mGame2048.moveLeft();
        upgradeGrid();
        MainActivity.getMainActivity().showScore(mGame2048.getScore());

    }


    private void moveUp() {
        mGame2048.moveRight();
        upgradeGrid();
        MainActivity.getMainActivity().showScore(mGame2048.getScore());
    }


    private void GameOver() {

      /*  final Dialog dialog = new Dialog(this.getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog1);
        dialog.show();
        TextView dialogScore = (TextView) dialog.findViewById(R.id.textView6);
        TextView highScore = (TextView) dialog.findViewById(R.id.textView_2);
        highScore.setText(MainActivity.getMainActivity().getBestScore() + "");
        dialogScore.setText(MainActivity.getScore() + "");
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.image3);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.cancel);
        dialogButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog4 = new Dialog(getContext());
                dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog4.setContentView(R.layout.records);
                dialog4.show();
                TextView highScore = (TextView) dialog4.findViewById(R.id.bestScore);
                highScore.setText(MainActivity.getMainActivity().getBestScore() + "");
            }
        });
        dialogButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just scored " + MainActivity.getScore() + " points in 2048");
                v.getContext().startActivity(Intent.createChooser(shareIntent, "Share score"));
            }
        });
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.clearScore();
                MainActivity.showScore();
                dialog.dismiss();

            }
        });*/
    }

    //виклик діалого вікна коли користувач виграв
    private void checkScore() {

      /*  final Dialog dialog = new Dialog(this.getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog1);
        dialog.show();
        TextView congt = (TextView) dialog.findViewById(R.id.textView1);
        congt.setText("You win!");
        TextView dialogScore = (TextView) dialog.findViewById(R.id.textView6);
        TextView highScore = (TextView) dialog.findViewById(R.id.textView_2);
        highScore.setText(MainActivity.getMainActivity().getBestScore() + "");
        dialogScore.setText(MainActivity.getScore() + "");
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.image3);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.cancel);
        dialogButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog4 = new Dialog(getContext());
                dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog4.setContentView(R.layout.records);
                dialog4.show();
                TextView highScore = (TextView) dialog4.findViewById(R.id.bestScore);
                highScore.setText(MainActivity.getMainActivity().getBestScore() + "");
            }
        });
        dialogButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just scored " + MainActivity.getScore() + " points in 2048");
                v.getContext().startActivity(Intent.createChooser(shareIntent, "Share score"));
            }
        });
        dialogButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mGame2048 = new Game2048();
                upgradeGrid();
                MainActivity.clearScore();
                MainActivity.showScore();
                dialog.dismiss();

            }
        });*/
    }

}
