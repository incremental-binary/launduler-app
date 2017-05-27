package kr.ac.kaist.launduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovementMethod mm = LinkMovementMethod.getInstance();
        ((TextView) findViewById(R.id.signup)).setMovementMethod(mm);
        ((TextView) findViewById(R.id.signin_email)).setMovementMethod(mm);

        Button button = (Button) findViewById(R.id.signin_google);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectPlace();
            }
        });

    }

    protected void startSelectPlace() {
        Intent intent = new Intent(this, ExploreSelectPlaceActivity.class);
        startActivity(intent);
    }
}
