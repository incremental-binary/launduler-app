package kr.ac.kaist.launduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovementMethod mm = LinkMovementMethod.getInstance();
        ((TextView) findViewById(R.id.signup)).setMovementMethod(mm);
        ((TextView) findViewById(R.id.signin_email)).setMovementMethod(mm);

    }
}
