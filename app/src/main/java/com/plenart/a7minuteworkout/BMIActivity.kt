package com.plenart.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_m_i.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNIT_VIEW = "METRIC_UNIT_VIEW"
    val IMPERIAL_UNIT_VIEW = "IMPERIAL_UNIT_VIEW"

    var currentVisibleView: String = METRIC_UNIT_VIEW;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_bmi_activity);
        val actionbar = supportActionBar;
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.title = "Calculate your BMI";
        }

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed();
        }

        btnCalculateUnits.setOnClickListener {

            if(currentVisibleView.equals(METRIC_UNIT_VIEW)){
                if(validateMetricUnits()){
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100;
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat();

                    val bmi = weightValue / (heightValue * heightValue);

                    displayBMIResult(bmi);
                }
                else
                    Toast.makeText(this, "Please enter valid values!", Toast.LENGTH_SHORT).show();
            }
            else{
                if(validateImperialUnits()){
                    val usUnitHeightValueFeet: String = etUsUnitHeightFeet.text.toString()
                    val usUnitHeightValueInch: String = etUsUnitHeightInch.text.toString()
                    val usUnitWeightValue: Float = etImperialUnitWeight.text.toString().toFloat();

                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12;
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue));

                    displayBMIResult(bmi);

                }else{
                    Toast.makeText(this, "Please enter valid values!", Toast.LENGTH_SHORT).show();
                }
            }


        }

        makeVisibleMetricUnitsView();
        rgUnits.setOnCheckedChangeListener{group,checkedID ->
            if(checkedID == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView();
            }
            else{
                makeVisibleImperialUnitsView();
            }
        }
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String;
        val bmiDescription: String;

        if (java.lang.Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 15f) > 0 && java.lang.Float.compare(
                        bmi,
                        16f
                ) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 16f) > 0 && java.lang.Float.compare(
                        bmi,
                        18.5f
                ) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 18.5f) > 0 && java.lang.Float.compare(
                        bmi,
                        25f
                ) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                        bmi,
                        30f
                ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 30f) > 0 && java.lang.Float.compare(
                        bmi,
                        35f
                ) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 35f) > 0 && java.lang.Float.compare(
                        bmi,
                        40f
                ) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDisplayBMIResult.visibility = View.VISIBLE;

        /* tvYourBMI.visibility = View.VISIBLE;
         tvBMIValue.visibility = View.VISIBLE;
         tvBMIType.visibility = View.VISIBLE;
         tvBMIDescription.visibility = View.VISIBLE;
         */

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString();

        tvBMIValue.text = bmiValue;
        tvBMIType.text = bmiLabel;
        tvBMIDescription.text = bmiDescription;

    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNIT_VIEW;

        etMetricUnitWeight.visibility = View.VISIBLE;
        etMetricUnitHeight.visibility = View.VISIBLE;
        ///////
        etMetricUnitWeight.text!!.clear();
        etMetricUnitHeight.text!!.clear();
        //////
        etImperialUnitWeight.visibility = View.INVISIBLE;
        llImperialUnitsHeight.visibility = View.INVISIBLE;

        llDisplayBMIResult.visibility = View.GONE;

    }

    private fun makeVisibleImperialUnitsView(){
        currentVisibleView = IMPERIAL_UNIT_VIEW;

        etMetricUnitWeight.visibility = View.GONE;
        etMetricUnitHeight.visibility = View.GONE;
        //////
        etImperialUnitWeight.text!!.clear();
        etUsUnitHeightFeet.text!!.clear();
        etUsUnitHeightInch.text!!.clear();
        /////
        etImperialUnitWeight.visibility = View.VISIBLE;
        llImperialUnitsHeight.visibility = View.VISIBLE;

        llDisplayBMIResult.visibility = View.GONE;

    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true;

        if(etMetricUnitHeight.text.toString().isEmpty() || etMetricUnitWeight.text.toString().isEmpty() )
            isValid = false;

        return isValid;
    }

    private fun validateImperialUnits(): Boolean{
        var isValid = true;

        if(etImperialUnitWeight.text.toString().isEmpty() || etUsUnitHeightFeet.text.toString().isEmpty() || etUsUnitHeightInch.text.toString().isEmpty() )
            isValid = false;

        return isValid;
    }

}