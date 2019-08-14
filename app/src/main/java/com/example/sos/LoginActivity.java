package com.example.sos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sos.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;

    private Button appCompatButtonLogin;
    private Button appCompatButtonGoSignUp;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        textInputEditTextEmail = (EditText) findViewById(R.id.enterEmail);
        textInputEditTextPassword = (EditText) findViewById(R.id.enterPassword);

        appCompatButtonLogin = (Button) findViewById(R.id.buttonLogin);

        appCompatButtonGoSignUp = (Button) findViewById(R.id.buttonGoSignUp);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        appCompatButtonGoSignUp.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                verifyFromSQLite();
                break;
            case R.id.buttonGoSignUp:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {
            String email= textInputEditTextEmail.getText().toString();
             String password=textInputEditTextPassword.getText().toString();
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);


        } else {
            // Snack Bar to show success message that record is wrong
            Toast.makeText(LoginActivity.this,getString(R.string.email_or_password_is_invalid), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }


}
