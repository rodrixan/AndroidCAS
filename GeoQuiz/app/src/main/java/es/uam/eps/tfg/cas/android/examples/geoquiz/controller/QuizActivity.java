package es.uam.eps.tfg.cas.android.examples.geoquiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import es.uam.eps.tfg.cas.android.examples.geoquiz.R;
import es.uam.eps.tfg.cas.android.examples.geoquiz.model.Question;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    public static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private final Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_africa, true),
            new Question(R.string.question_mom, false),
            new Question(R.string.question_ocean, true)
    };

    private int mCurrentIndex = 0;
    private boolean mHasCheated;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        wireComponents();
        setListeners();
        setToolBar();
        restoreData(savedInstanceState);
        setActualQuestion();

    }


    private void wireComponents() {
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
    }

    private void setListeners() {
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int toastMsgId = checkAnswer(true);
                showToast(QuizActivity.this, toastMsgId);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int toastMsgId = checkAnswer(false);
                showToast(QuizActivity.this, toastMsgId);
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //launch activity
                Log.d(TAG, "Launching cheat activity");

                final boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                final Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                //used for hearing back from the child that this activity launches
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                nextQuestion();
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                previousQuestion();
            }
        });

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                nextQuestion();
            }
        });
    }

    private int checkAnswer(final boolean userPressedValue) {
        final boolean questionAnswer = mQuestionBank[mCurrentIndex].isAnswerTrue();

        if (mHasCheated) {
            return R.string.judgement_toast;
        }
        final int mResId = (userPressedValue == questionAnswer) ? R.string.correct_toast : R.string.incorrect_toast;
        return mResId;
    }

    private void showToast(final AppCompatActivity context, final int stringId) {

        Toast.makeText(QuizActivity.this, stringId, Toast.LENGTH_SHORT).show();
    }

    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

        setActualQuestion();
    }

    private void previousQuestion() {
        mCurrentIndex--;
        if (mCurrentIndex == -1) {
            mCurrentIndex = mQuestionBank.length - 1;
        }
        setActualQuestion();
    }

    private void setToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void restoreData(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.d(TAG, "restoring instance: current index");
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        }
    }

    private void setActualQuestion() {
        mHasCheated = false;
        final int questionId = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(questionId);
        Log.d(TAG, "Question #" + questionId + " setted");
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving instance: current index");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data != null) {
                mHasCheated = CheatActivity.wasAnswerShown(data);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
