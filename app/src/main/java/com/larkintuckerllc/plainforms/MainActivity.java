package com.larkintuckerllc.plainforms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
    private EditText mFirstField;
    private EditText mLastField;
    private EditText mEmailField;
    private Button mSubmitButton;
    private Boolean mFirstFieldDirty = false;
    private Boolean mLastFieldDirty = false;
    private Boolean mEmailFieldDirty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirstField = findViewById(R.id.activity_main__txt_first);
        mFirstField.setOnFocusChangeListener(new FieldFocusChangeListener());
        mFirstField.addTextChangedListener(new FieldValidator(mFirstField));
        mLastField = findViewById(R.id.activity_main__txt_last);
        mLastField.setOnFocusChangeListener(new FieldFocusChangeListener());
        mLastField.addTextChangedListener(new FieldValidator(mLastField));
        mEmailField = findViewById(R.id.activity_main__txt_email);
        mEmailField.setOnFocusChangeListener(new FieldFocusChangeListener());
        mEmailField.addTextChangedListener(new FieldValidator(mEmailField));
        mSubmitButton = findViewById(R.id.activity_main__btn_submit);
        mSubmitButton.setEnabled(false);
    }

    public void sendFeedback(View view) {
        final EditText firstField = findViewById(R.id.activity_main__txt_first);
        String first = firstField.getText().toString();
        final EditText lastField = findViewById(R.id.activity_main__txt_last);
        String last = lastField.getText().toString();
        final EditText emailField = findViewById(R.id.activity_main__txt_email);
        String email = emailField.getText().toString();
        Log.d("DEBUG", first);
        Log.d("DEBUG", last);
        Log.d("DEBUG", email);
    }

    private class FieldFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) return;
            String fieldValue = ((EditText)view).getText().toString();
            String fieldRequiredStr = getString(R.string.field_required);
            String fieldEmailRequiredStr = getString(R.string.field_email_required);
            switch(view.getId()) {
                case R.id.activity_main__txt_first:
                    if (fieldValue.trim().equals(""))
                        mFirstField.setError(fieldRequiredStr);
                    mFirstFieldDirty = true;
                    break;
                case R.id.activity_main__txt_last:
                    if (fieldValue.trim().equals(""))
                        mLastField.setError(fieldRequiredStr);
                    mLastFieldDirty = true;
                    break;
                case R.id.activity_main__txt_email:
                    if (!EMAIL_PATTERN.matcher(fieldValue).matches())
                        mEmailField.setError(fieldEmailRequiredStr);
                    mEmailFieldDirty = true;
                    break;
            }
        }
    }

    private class FieldValidator implements TextWatcher {
        private WeakReference<EditText> mEditText;

        public FieldValidator(EditText editText) {
            mEditText = new WeakReference<>(editText);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final EditText editText = mEditText.get();
            if (editText != null) {
                String fieldRequiredStr = getString(R.string.field_required);
                String fieldEmailRequiredStr = getString(R.string.field_email_required);
                switch(editText.getId()) {
                    case R.id.activity_main__txt_first:
                        if (mFirstFieldDirty && s.toString().trim().equals("")) {
                            editText.setError(fieldRequiredStr);
                        } else {
                            editText.setError(null);
                        }
                        break;
                    case R.id.activity_main__txt_last:
                        if (mLastFieldDirty && s.toString().trim().equals("")) {
                            editText.setError(fieldRequiredStr);
                        } else {
                            editText.setError(null);
                        }
                        break;
                    case R.id.activity_main__txt_email:
                        if (mEmailFieldDirty && !EMAIL_PATTERN.matcher(s.toString()).matches()) {
                            editText.setError(fieldEmailRequiredStr);
                        } else {
                            editText.setError(null);
                        }
                        break;
                }
            }
            if (
                mFirstField.getText().toString().trim().equals("") ||
                mLastField.getText().toString().trim().equals("") ||
                !EMAIL_PATTERN.matcher(mEmailField.getText().toString()).matches()
            ) {
                mSubmitButton.setEnabled(false);
            } else {
                mSubmitButton.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

}
