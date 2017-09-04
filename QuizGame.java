package com.example.devan.quizgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*****************************************************
 This is a Quiz Game for android.

 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001

 There are true and false based questions. Try to get the highest scores (Top 10)

 Quiz can be played by either pressing the true and false buttons
 or tilting the phone left for true and right for false.

 User is allowed to add questions.

 See the high scores and gloat if your name is there and if not, strive to get there!
 Best of luck

 This is the main activity, it gives option to play the game, add questions or check high scores.
 ******************************************************/

public class QuizGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        }

    //method checkHighScore triggered when Check high scores button is pressed
    public void checkHighScore(View view)
    {
        //send intent to CheckHighScore activity
    Intent intent = new Intent(QuizGame.this, CheckHighScore.class);
    startActivity(intent);
    }

    //method addQuestion triggered when Add Questions button is pressed
    public void addQuestion(View view)
    {
        //send intent to AddQuestion
        Intent intent = new Intent(QuizGame.this, AddQuestion.class);
        startActivity(intent);
    }

    //method startGame triggered when Start Game button is pressed
    public void startGame(View view)
    {
        //send intent to QuizPlay
        Intent intent = new Intent(QuizGame.this, QuizPlay.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

