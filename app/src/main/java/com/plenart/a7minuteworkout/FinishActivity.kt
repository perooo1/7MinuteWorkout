package com.plenart.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_finish.*
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        setSupportActionBar(toolbar_finish_activity);
        val actionBar = supportActionBar;
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed();
        }

        btnFinished.setOnClickListener{
            finish();
        }

        addDateToDatabase();

    }

    private fun addDateToDatabase(){
        val calendar = Calendar.getInstance();
        val datetime = calendar.time;
        Log.e("DATE: ",""+datetime);

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        val date = sdf.format(datetime);

        val dbHandler = SqliteOpenHelper(this, null);
        dbHandler.addDate(date);
        Log.e("Date:", "added");

    }

}