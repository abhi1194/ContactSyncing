package com.example.admin1.contentprovidercontact.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin1.contentprovidercontact.R;


public class CountDownActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mCountTv;
    private LinearLayout mLinearLayout;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        mLinearLayout = findViewById(R.id.ll_main);
        Button mStartBtn = findViewById(R.id.btn_start);
        mCountTv = findViewById(R.id.tv_count_down);

        mStartBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                if((mCountDownTimer == null)) {
                    timerCount();
                }else {
                    Toast.makeText(this,"Timer is On",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Countdown timer function
     */
    private void timerCount(){
        mCountDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                StringBuilder value = new StringBuilder();
                value.append(String.valueOf(millisUntilFinished / 1000)).append(" ").append(getString(R.string.seconds_remaining));
                mCountTv.setText(value);
                mCountTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                mLinearLayout.setBackgroundResource(R.color.colorWhite);
            }

            @Override
            public void onFinish() {
                mCountTv.setTextColor(getResources().getColor(R.color.colorWhite));
                mCountTv.setText(R.string.done);
                mLinearLayout.setBackgroundResource(R.color.colorBackground);
                Toast.makeText(CountDownActivity.this,"Success",Toast.LENGTH_LONG).show();
                mCountDownTimer = null;
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }
}
