package com.djylrz.xzpt.fragment.company;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.provider.DocumentsProvider;
import com.djylrz.xzpt.utils.FileUtils;
import com.djylrz.xzpt.utils.PermissionHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static android.content.Intent.EXTRA_MIME_TYPES;
/**
  *@Description: TODO
  *@Author: mingjun
  *@Date: 2019/5/20 下午 2:44
  */
public class CsvPickerFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO = 3;
    private static final int REQUEST_CODE_PICK_CSV = 2;
    private static final int START_CHARACTER = 16;
    private static final int END_CHARACTER = 20;
    private TextView tvPickFile;

    public static CsvPickerFragment newInstance() {
        Bundle args = new Bundle();
        CsvPickerFragment fragment = new CsvPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_csv_picker, container, false);
        view.findViewById(R.id.bPickFile).setOnClickListener(this);
        tvPickFile = view.findViewById(R.id.tvPickFile);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SpannableString ss = new SpannableString(getString(R.string.pick_csv_or_demo_file));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (PermissionHelper.checkOrRequest(CsvPickerFragment.this, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    createDemoFile();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, START_CHARACTER, END_CHARACTER, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvPickFile.setText(ss);
        tvPickFile.setMovementMethod(LinkMovementMethod.getInstance());
        tvPickFile.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                    pickCsvFile();
                    break;
                case REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO:
                    createDemoFile();
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DocumentsProvider documentsProvider = new DocumentsProvider();
        if (requestCode == REQUEST_CODE_PICK_CSV && data != null && resultCode == Activity.RESULT_OK) {
            Activity activity = getActivity();
            if (activity instanceof OnCsvFileSelectedListener) {
                ((OnCsvFileSelectedListener) activity).onCsvFileSelected(documentsProvider.getType(Objects.requireNonNull(data.getData())));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (PermissionHelper.checkOrRequest(this, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            pickCsvFile();
        }
    }

    private void pickCsvFile() {
        String[] mimeTypes = {"text/comma-separated-values", "text/csv"};
        Intent intent;

        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(EXTRA_MIME_TYPES, mimeTypes);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_file)), REQUEST_CODE_PICK_CSV);
    }

    private void createDemoFile() {
        File file = createDemoTempFile();
        try {
            if (!file.exists() && file.createNewFile()) {
                InputStream inputStream = null;
                inputStream = Objects.requireNonNull(getContext()).getAssets().open("example.csv");
                FileUtils.copy(inputStream, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        }

        String path = file == null ? "" : file.getPath();

        if (!TextUtils.isEmpty(path)) {
            Activity activity = getActivity();
            if (activity instanceof OnCsvFileSelectedListener) {
                ((OnCsvFileSelectedListener) activity).onCsvFileSelected(path);
            }
        }
    }

    public File createDemoTempFile() {
        String tempFileName = "DEMO_xzpt_recruitment_list.csv";
        return new File(Environment.getExternalStorageDirectory(), tempFileName);
    }

    interface OnCsvFileSelectedListener {
        void onCsvFileSelected(String file);
    }
}
