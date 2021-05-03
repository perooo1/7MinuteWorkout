package com.plenart.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_excercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_conformation.*
import org.w3c.dom.Text
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null;
    private var restProgress = 0;
    private var restTimerDuration: Long = 10;

    private var exerciseTimer: CountDownTimer? = null;
    private var exerciseProgress = 0;
    private var exerciseTimerDuration: Long = 30;

    private var exerciseList: ArrayList<ExerciseModel>?= null;
    private var currentExercisePosition = -1;

    private var tts : TextToSpeech? = null;
    private var player: MediaPlayer? = null;

    private var exerciseAdapter : ExerciseStatusAdapter? = null;

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
            customDialogForBackButton();
        }

        exerciseList = Constants.defaultExerciseList();     //kao static u javi
        setUpRestView();
        setupExerciseStatusRecyclerView();
    }

    private fun setRestProgressBar(){
        progressBar.progress = restProgress;
        restTimer = object : CountDownTimer(restTimerDuration * 1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++;
                progressBar.progress = 10- restProgress;
                tvTimer.text = (10-restProgress).toString();
            }

            override fun onFinish() {
                currentExercisePosition++;

                exerciseList!![currentExercisePosition].setIsSelected(true);
                exerciseAdapter!!.notifyDataSetChanged();

                setUpExerciseView();
            }

        }.start();

    }

    private fun setUpRestView(){

        try{
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
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++;
                progressBarExercise.progress = 30- exerciseProgress;
                tvTimerExercise.text = (30-exerciseProgress).toString();
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! -1){
                    exerciseList!![currentExercisePosition].setIsSelected(false);
                    exerciseList!![currentExercisePosition].setIsCompleted(true);
                    exerciseAdapter!!.notifyDataSetChanged();
                    setUpRestView();
                }
                else{
                    finish();
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java);
                    startActivity(intent);

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
            Log.e("TTS","lInitialization faied!" + status);
        }

    }


    private fun speakOut(text:String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null,null);
    }

    private fun setupExerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this);
        rvExerciseStatus.adapter =  exerciseAdapter;
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this);
        customDialog.setContentView(R.layout.dialog_custom_back_conformation);
        customDialog.btnConformationYes.setOnClickListener {
            finish();
            customDialog.dismiss();
        }

        customDialog.btnConformationNo.setOnClickListener{
            customDialog.dismiss();
        }

        customDialog.show();

    }

}