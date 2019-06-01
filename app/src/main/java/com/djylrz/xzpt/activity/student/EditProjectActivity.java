package com.djylrz.xzpt.activity.student;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.djylrz.xzpt.R;

public class EditProjectActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText project;
    private Button save;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);
        project = (EditText) findViewById(R.id.project_edittext);
        save = (Button) findViewById(R.id.save_button);
        toolbar = (Toolbar)findViewById(R.id.edit_project_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(this);
        initPage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                SharedPreferences sharedPreferences = getSharedPreferences("user",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("project",project.getText().toString());
                editor.commit();
                finish();
                break;
            default:
                break;
        }
    }

    private void initPage(){
        SharedPreferences sharedPreferences = getSharedPreferences("user",0);
        String detail = sharedPreferences.getString("project",null);
        project.setText(detail!=null?detail:"");
    }
}
