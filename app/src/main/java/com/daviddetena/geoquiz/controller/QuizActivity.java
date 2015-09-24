package com.daviddetena.geoquiz.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daviddetena.geoquiz.R;
import com.daviddetena.geoquiz.model.Question;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    // Key where the index for the current question will be stored so it preserves between rotations
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;        // Request Code for Intent
    private boolean mIsCheater;

    private TextView mQuestionTextView;
    private Button mFalseButton;
    private Button mTrueButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private int mCurrentIndex = 0;      // current index of question in Array


    // Initial array of questions
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // Check if there is already an instance to preserve data across rotations
        if(savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        // Wire up View
        // Sets up the TextView with the question title of the current index in the array
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();

        // Wire up Buttons
        mFalseButton = (Button) findViewById(R.id.false_button);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mPrevButton = (ImageButton) findViewById(R.id.previous_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);


        // Listener for the TextView to show next question when tapping on it
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Next index and update text of the new question
                nextQuestion();
            }
        });

        // Set up listener for the buttons and make a toast appear informing about the answer
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity using CheatActivity static method with the answer
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);


                // Disable the navigation buttons when "cheat" button pressed
                // Animations only available if Android API >=21
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cxP = mPrevButton.getWidth() / 2;
                    int cyP = mPrevButton.getWidth() / 2;
                    float radiusP = mPrevButton.getWidth();

                    int cxN = mNextButton.getWidth() / 2;
                    int cyN = mNextButton.getWidth() / 2;
                    float radiusN = mNextButton.getWidth();

                    // Create an animation to hide navigation buttons
                    Animator animP = ViewAnimationUtils.createCircularReveal(mPrevButton, cxP, cyP, radiusP, 0);
                    animP.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mPrevButton.setVisibility(View.INVISIBLE);

                        }
                    });

                    Animator animN = ViewAnimationUtils.createCircularReveal(mNextButton, cxN, cyN, radiusN, 0);
                    animN.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mNextButton.setVisibility(View.INVISIBLE);
                        }
                    });

                    // Start animating
                    animP.start();
                    animN.start();

                } else {
                    // ANIMATION NOT SUPPORTED API <21 => Just hide buttons
                    mPrevButton.setVisibility(View.INVISIBLE);
                    mNextButton.setVisibility(View.INVISIBLE);
                }
            }
        });


        // Set up New Question Title when clicking Prev
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Previous index and update text of the previous question
                mIsCheater = false;
                previousQuestion();
            }
        });

        // Set up New Question Title when clicking Next
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Next index and update text of the new question
                mIsCheater = false;
                nextQuestion();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        Log.i(TAG, "onSaveInstanceState()");

        // Save current index in Bundle key-value pair
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when receiving data from a child Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult()");
        Log.d(TAG, String.valueOf(resultCode));
        Log.d(TAG, String.valueOf(requestCode));

        if(resultCode != Activity.RESULT_OK){
            return;     // Something was wrong
        }

        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;     // No back data found
            }
            // Check if answer was shown
            mIsCheater = CheatActivity.wasAnswerShown(data);

            // Make the navigation button appear again
            mPrevButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
        }

        Log.d(TAG, String.valueOf(requestCode));
    }

    // UTILS
    private void nextQuestion(){
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    }

    private void previousQuestion(){
        if(mCurrentIndex>0){
            mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
        }
        else{
            mCurrentIndex = mQuestionBank.length - 1;
        }
        updateQuestion();
    }


    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    /**
     * Method to check whether the answer pressed by the user is correct or not
     * @param userPressedTrue User's answer: true or false
     */
    private void checkAnswer(boolean userPressedTrue){

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if(mIsCheater){
            // Cheater toast
            messageResId = R.string.judgment_toast;
        }
        else{
            if(userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
            }
            else{
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_LONG).show();
    }
}
