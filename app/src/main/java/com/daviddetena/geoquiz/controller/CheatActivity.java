package com.daviddetena.geoquiz.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daviddetena.geoquiz.R;

public class CheatActivity extends AppCompatActivity {

    // Key string to get the param this class will use when launched via Intent (like input param)
    private static final String EXTRA_ANSWER_IS_TRUE = "com.daviddetena.geoquiz.controller.answer_is_true";

    // Key to put as an extra in the intent this child Activity will get back to his father (like output param)
    private static final String EXTRA_ANSWER_SHOWN = "com.daviddetena.geoquiz.controller.answer_shown";

    private boolean mAnswerIsTrue;

    // UI Widgets
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    /**
     * Method to create a new Intent for this class with the EXTRA param
     * @param packageContext
     * @param answerIsTrue
     * @return
     */
    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        // Get extra parameter and save in variable
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        // Wire up variables with UI Widgets
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);

        // When clicking on the button
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Whether the answer is correct or not is retrieved with EXTRA from QuizActivity
                // intent
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }

                // User did cheat
                setAnswerShownResult(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
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

    // UTILS

    /**
     * Method responsible for setting extra data to an Intent which will be returned to the father
     * @param isAnswerShown Whether the user cheated or not
     */
    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);

        // Get data back to the father
        setResult(RESULT_OK, data);
    }

    /**
     * This static method will serve for the QuizActivity to get noticed about whether the user
     * pressed the cheat button and the answer was shown
     * @param result
     * @return
     */
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
}
