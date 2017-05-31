package kr.ac.kaist.launduler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import kr.ac.kaist.launduler.machine.MachineStatusFragment;
import kr.ac.kaist.launduler.machine.WeekFragment;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        //Fragment fragment = ExploreFragment.newInstance();
        if (savedInstanceState == null) {
            Fragment fragment = MachineStatusFragment.Companion.newInstance(null);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment, "EXPLORE")
                    .commit();
        }
    }
}
