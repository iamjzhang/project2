package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewScore,textViewQuestion, textViewCount, textViewTimer;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3;
    private Button buttonSubmit;

    //change text color of selection based on if answer is correct or not
    private ColorStateList textColorDefault;
    private ColorStateList textColorDefaultTimer;

    //implement timer
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    //list of questions
    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewScore = findViewById(R.id.textView_score);
        textViewQuestion = findViewById(R.id.textView_question);
        textViewCount = findViewById(R.id.textView_count);
        textViewTimer = findViewById(R.id.textView_timer);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonSubmit = findViewById(R.id.button_submit);

        textColorDefault = rb1.getTextColors();
        textColorDefaultTimer = textViewTimer.getTextColors();

        //make sure instance is not saved
        if (savedInstanceState == null) {

            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter -1); //question count always one ahead
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            //if question not answered yet, resume countdown timer

            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){
                        openDialog();
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    //displays next question
    private void showNextQuestion() {
        rb1.setTextColor(textColorDefault);
        rb2.setTextColor(textColorDefault);
        rb3.setTextColor(textColorDefault);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonSubmit.setText("Submit");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    //time countdown
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished){
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish(){
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        //get time in minutes and reamining seconds
        int minutes = (int)(timeLeftInMillis / 1000) / 60;
        int seconds = (int)(timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewTimer.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewTimer.setTextColor(Color.RED);
        } else {
            textViewTimer.setTextColor(textColorDefaultTimer);
        }
    }

    //checks answer
    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel(); //cancel timer if answered

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNb = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNb == currentQuestion.getAnswerNumb()) {
            score++;
            textViewScore.setText("Score: " + score);

        }

        showSolution();
    }

    //method to show correct answer with green color and incorrect answers with red
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNumb()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonSubmit.setText("Next");
        }else {
            buttonSubmit.setText("Finish");
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    //timer to prevent quiz from restarting when user clicks "back" button
    @Override
    public void onBackPressed(){
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    //dialogbox to confirm answer
    public void openDialog(){
        AlertDialog results = new AlertDialog.Builder(QuizActivity.this)
                .setTitle("Notice:")
                .setMessage("Please confirm your answer")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        results.show();

    }

    @Override
    //destroy countdown timer at end
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    //method to save results due to orientation changes
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}