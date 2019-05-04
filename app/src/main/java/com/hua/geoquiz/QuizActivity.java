package com.hua.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String KEY_INDEX = "index";
    private static final String KEY_COUNT = "count";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.hua.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.hua.geoquiz.answer_shown";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private TextView mCheatCountTextView;
    private int mCurrentIndex = 0;
    private Question[] mQuestionBank =
            {
                    new Question(R.string.question_australia, true),
                    new Question(R.string.question_oceans, true),
                    new Question(R.string.question_mideast, false),
                    new Question(R.string.question_africa, false),
                    new Question(R.string.question_americas, true),
                    new Question(R.string.question_asia, true)
            };
    private boolean mIsCheater = false;
    private int mCheatCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCheatCount = savedInstanceState.getInt(KEY_COUNT, 0);
        }
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mCheatButton = findViewById(R.id.cheat_button);
        mNextButton = findViewById(R.id.next_button);
        mQuestionTextView = findViewById(R.id.question_text_view);
        mCheatCountTextView = findViewById(R.id.cheat_count);
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
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheater = false;
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
        mCheatCountTextView.setText("" + (3 - mCheatCount));
        if (mCheatCount >= 3) {
            mCheatButton.setVisibility(View.GONE);
            mCheatCountTextView.setVisibility(View.GONE);
        }
    }

    private void updateQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }

    private void checkAnswer(boolean userPressedTrue) {
        if (mIsCheater) {
            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPressedTrue == mQuestionBank[mCurrentIndex].isAnswerTrue())
            Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
    }

    private Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_COUNT, mCheatCount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHEAT && resultCode == RESULT_OK) {
            mIsCheater = data.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
            if (mIsCheater)
                mCheatCount++;
            mCheatCountTextView.setText("" + (3 - mCheatCount));
            if (mCheatCount >= 3) {
                mCheatButton.setVisibility(View.GONE);
                mCheatCountTextView.setVisibility(View.GONE);
            }

        }
    }
}
