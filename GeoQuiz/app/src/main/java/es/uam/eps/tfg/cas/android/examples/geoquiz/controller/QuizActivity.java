package es.uam.eps.tfg.cas.android.examples.geoquiz.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import es.uam.eps.tfg.cas.android.examples.geoquiz.R;
import es.uam.eps.tfg.cas.android.examples.geoquiz.model.Question;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    private final Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_africa, true),
            new Question(R.string.question_mom, false),
            new Question(R.string.question_ocean, true)
    };

    private int mCurrentIndex = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        wireButtons();
        wireTextView();
        setListeners();
        setToolBar();
        randomQuestion();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void randomQuestion() {
        mCurrentIndex = (Double.valueOf(Math.random()).intValue()) % mQuestionBank.length;
        final int questionId = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(questionId);
    }


    private void wireButtons() {
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
    }

    private void wireTextView() {
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

    private void showToast(final AppCompatActivity context, final int stringId) {

        Toast.makeText(QuizActivity.this, stringId, Toast.LENGTH_SHORT).show();
    }

    private void setToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        final int questionId = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(questionId);
    }

    private void previousQuestion() {
        mCurrentIndex--;
        if (mCurrentIndex == -1) {
            mCurrentIndex = mQuestionBank.length - 1;
        }
        final int questionId = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(questionId);
    }

    private int checkAnswer(final boolean userPressedValue) {
        final boolean questionAnswer = mQuestionBank[mCurrentIndex].isAnswerTrue();
        final int mResId = (userPressedValue == questionAnswer) ? R.string.correct_toast : R.string.incorrect_toast;
        return mResId;
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
