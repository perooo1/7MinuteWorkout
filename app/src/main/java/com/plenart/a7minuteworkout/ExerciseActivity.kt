package com.plenart.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_excercise.*

class ExerciseActivity : AppCompatActivity() {

    private var restTimer: CountDownTimer? = null;
    private var restProgress = 0;

    private var exerciseTimer: CountDownTimer? = null;
    private var exerciseProgress = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise)

        setSupportActionBar(toolbar_exercise_activity);
        val actionBar = supportActionBar;
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed();
        }

        setUpRestView();
    }

    private fun setRestProgressBar(){
        progressBar.progress = restProgress;
        restTimer = object : CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++;
                progressBar.progress = 10- restProgress;
                tvTimer.text = (10-restProgress).toString();
            }

            override fun onFinish() {
                setUpExerciseView();
            }

        }.start();

    }

    private fun setUpRestView(){
        if(restTimer != null){
            restTimer!!.cancel();
            restProgress = 0;
        }
        setRestProgressBar();
    }

    override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel();
            restProgress = 0;
        }

        if(exerciseTimer != null){
            exerciseTimer!!.cancel();
            exerciseProgress=0;
        }

        super.onDestroy();

    }

    private fun setExerciseProgressBar(){
        progressBarExercise.progress = exerciseProgress;
        exerciseTimer = object : CountDownTimer(30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++;
                progressBarExercise.progress = 30- exerciseProgress;
                tvTimerExercise.text = (30-exerciseProgress).toString();
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity, "We will now start the rest screen", Toast.LENGTH_SHORT).show();
            }

        }.start();

    }

    private fun setUpExerciseView(){

        llRestView.visibility = View.GONE;
        llExerciseView.visibility = View.VISIBLE;

        if(exerciseTimer != null){
            exerciseTimer!!.cancel();
            exerciseProgress = 0;
        }
        setExerciseProgressBar();
    }

}