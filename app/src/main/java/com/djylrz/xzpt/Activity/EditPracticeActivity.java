package com.djylrz.xzpt.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.djylrz.xzpt.R;

public class EditPracticeActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText practice;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpractice);
        practice = (EditText) findViewById(R.id.practice_edittext);
        save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                SharedPreferences sharedPreferences = getSharedPreferences("user",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("practice",practice.getText().toString());
                editor.commit();
                finish();
        }
    }
}
