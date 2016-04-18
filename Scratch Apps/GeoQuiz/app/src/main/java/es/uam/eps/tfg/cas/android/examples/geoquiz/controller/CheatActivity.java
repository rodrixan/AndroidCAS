package es.uam.eps.tfg.cas.android.examples.geoquiz.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import es.uam.eps.tfg.cas.android.examples.geoquiz.R;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "es.uam.eps.tfg.cas.android.examples.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "es.uam.eps.tfg.cas.android.examples.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        setToolBar();
        getData(getIntent());
        wireComponents();
        setListeners();

    }

    private void getData(final Intent intent) {
        mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
    }

    private void setToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void wireComponents() {
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
    }

    private void setListeners() {
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(QuizActivity.TAG, "cheating...");
                setAnswerText(mAnswerIsTrue);
                setAnswerShownResult(true);
            }
        });
    }


    private void setAnswerText(final boolean answer) {
        final int textId = (answer) ? R.string.true_button : R.string.false_button;
        mAnswerTextView.setText(textId);
    }

    private void setAnswerShownResult(final boolean answerShown) {
        Log.d(QuizActivity.TAG, "putting data for the parent");
        final Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, answerShown);
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(final Context packageContext, final boolean answerIsTrue) {
        final Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(final Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
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
