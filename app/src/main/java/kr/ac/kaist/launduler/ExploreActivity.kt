package kr.ac.kaist.launduler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kaist.launduler.machine.ExploreMachineStatusFragment
import kr.ac.kaist.launduler.machine.MachineStatusFragment

class ExploreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        //Fragment fragment = ExploreFragment.newInstance();
        if (savedInstanceState == null) {
            val selectedMachineId = selectedMachineId
            val fragment = MachineStatusFragment.newInstance<ExploreMachineStatusFragment>(null, selectedMachineId)

            val fragmentManager = supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment, "EXPLORE")
                    .commit()
        }
    }

    protected val selectedMachineId: String
        get() = "selectedMachine1"
}
