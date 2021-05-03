package com.plenart.a7minuteworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_excercise.*
import org.w3c.dom.Text
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null;
    private var restProgress = 0;

    private var exerciseTimer: CountDownTimer? = null;
    private var exerciseProgress = 0;

    private var exerciseList: ArrayList<ExerciseModel>?= null;
    private var currentExercisePosition = -1;

    private var tts : TextToSpeech? = null;
    private var player: MediaPlayer? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise)

        setSupportActionBar(toolbar_exercise_activity);

        tts = TextToSpeech(this,this);

        val actionBar = supportActionBar;
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed();
        }

        exerciseList = Constants.defaultExerciseList();     //kao static u javi
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
                currentExercisePosition++;
                setUpExerciseView();
            }

        }.start();

    }

    private fun setUpRestView(){

        try{
            //val soundURI = Uri.parse("android:resource://com.plenart.a7minuteworkout/"+ R.raw.press_start)
            player = MediaPlayer.create(applicationContext,R.raw.press_start);
            player!!.isLooping = false;
            player!!.start();
        }
        catch (e: Exception){
            e.printStackTrace();
        }

        llRestView.visibility = View.VISIBLE;
        llExerciseView.visibility = View.GONE;


        if(restTimer != null){
            restTimer!!.cancel();
            restProgress = 0;
        }
        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition+1].getName();
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

        if(tts != null){
            tts!!.stop();
            tts!!.shutdown();
        }

        if( player != null) player!!.stop()
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
                if(currentExercisePosition < exerciseList?.size!! -1)
                    setUpRestView();
                else{
                    Toast.makeText(this@ExerciseActivity,"Congrats, you completed 7min workout!", Toast.LENGTH_SHORT).show(); //TODO
                }
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

        speakOut(exerciseList!![currentExercisePosition].getName());

        setExerciseProgressBar();

        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage());
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName();

    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US);
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","specified language not supported");
            }
        }
        else{
            Log.e("TTS","Initialization failed!");
        }

    }


    private fun speakOut(text:String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null,null);
    }

}