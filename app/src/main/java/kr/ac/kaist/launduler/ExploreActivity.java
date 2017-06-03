package kr.ac.kaist.launduler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import kr.ac.kaist.launduler.machine.MachineStatusFragment;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        //Fragment fragment = ExploreFragment.newInstance();
        if (savedInstanceState == null) {
            String selectedMachineId = getSelectedMachineId();
            Fragment fragment = MachineStatusFragment.Companion.newInstance(null, selectedMachineId);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment, "EXPLORE")
                    .commit();
        }
    }

    protected String getSelectedMachineId() {
        return "selectedMachine1";
    }
}
