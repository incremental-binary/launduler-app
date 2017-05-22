package kr.ac.kaist.launduler;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    Button signup;
    List<TextField> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.signup);
        fields = new ArrayList<>(3);

        TextView name = (TextView) findViewById(R.id.name);
        TextInputLayout nameLayout = (TextInputLayout) findViewById(R.id.name_layout);
        TextField nameField = new TextField(this, name, nameLayout, R.string.name_empty) {
            @Override
            boolean test(CharSequence s) {
                return !TextUtils.isEmpty(s);
            }
        };
        nameField.addTextChangedListener();
        fields.add(nameField);

        TextView email = (TextView) findViewById(R.id.email);
        TextInputLayout emailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        TextField emailField = new TextField(this, email, emailLayout, R.string.email_invalid) {
            @Override
            boolean test(CharSequence s) {
                return !TextUtils.isEmpty(s) &&
                        android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
            }
        };
        emailField.addTextChangedListener();
        fields.add(emailField);

        TextView password = (TextView) findViewById(R.id.password);
        TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        TextField passwordField = new TextField(this, password, passwordLayout, R.string.password_empty) {
            @Override
            boolean test(CharSequence s) {
                return !TextUtils.isEmpty(s);
            }
        };
        passwordField.addTextChangedListener();
        fields.add(passwordField);
    }

    private void onFormUpdate() {
        for (TextField field : fields) {
            if (!field.test(field.view.getEditableText())) {
                signup.setEnabled(false);
                return;
            }
        }
        signup.setEnabled(true);
    }

    private abstract class TextField {
        SignupActivity activity;
        TextView view;
        TextInputLayout layout;
        CharSequence error;

        TextField(SignupActivity activity, TextView view, TextInputLayout layout, int errorResId) {
            this.activity = activity;
            this.view = view;
            this.layout = layout;
            this.error = getString(errorResId);
        }

        abstract boolean test(CharSequence s);

        void addTextChangedListener() {
            this.view.addTextChangedListener(new TextFieldWatcher(this));
        }
    }

    private class TextFieldWatcher implements TextWatcher {
        TextField field;

        TextFieldWatcher(TextField field) {
            this.field = field;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            TextInputLayout layout = this.field.layout;
            if (this.field.test(editable)) {
                layout.setErrorEnabled(false);
                layout.setError(null);
            } else {
                layout.setErrorEnabled(true);
                layout.setError(this.field.error);
            }
            this.field.activity.onFormUpdate();
        }
    }
}
