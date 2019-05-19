package com.djylrz.xzpt.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.djylrz.xzpt.R;

import java.io.File;

public class PickCSVActivity extends AppCompatActivity implements
        CsvPickerFragment.OnCsvFileSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_csv);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, CsvPickerFragment.newInstance(), CsvPickerFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onCsvFileSelected(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(fileName);
            if (file.exists() && fileName.endsWith(".csv")) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, TableLayoutFragment.newInstance(fileName), CsvPickerFragment.class.getSimpleName())
                        .addToBackStack(CsvPickerFragment.class.getSimpleName())
                        .commit();
            } else {
                Toast.makeText(this, R.string.not_csv_file_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.no_such_file_error, Toast.LENGTH_SHORT).show();
        }
    }
}