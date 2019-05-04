package com.hua.geoquiz;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.hua.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.hua.geoquiz.answer_shown";
    private boolean mAnswerIsTrue = false;

    private Button mShowAnswerButton;
    private TextView mAnswer;
    private TextView mAPILevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mAnswer = findViewById(R.id.answer_text_view);
        mAPILevel = findViewById(R.id.api_level);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue)
                    mAnswer.setText(R.string.true_button);
                else
                    mAnswer.setText(R.string.false_button);
                setAnswerShowResult(true);
            }
        });
        mAPILevel.setText("" + Build.VERSION.SDK_INT);
    }

    private void setAnswerShowResult(boolean isAnswerShown) {
        Intent intent = new Intent(CheatActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, intent);
    }
}
