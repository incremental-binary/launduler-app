package kr.ac.kaist.launduler;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final TextInputEditText et_id = (TextInputEditText) findViewById(R.id.email);
        final TextInputEditText et_pw = (TextInputEditText) findViewById(R.id.password);

        // Set on-click event to "SIGN IN" button.
        Button signin = (Button)findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_id.getText()) ) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "ID is empty", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if( TextUtils.isEmpty(et_pw.getText()) ) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "PW is empty", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else{
                    Intent intent=new Intent(SigninActivity.this, ExploreActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
