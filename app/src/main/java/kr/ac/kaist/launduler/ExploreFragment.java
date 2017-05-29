package kr.ac.kaist.launduler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {
    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        String list_machines_json = "{\n" +
                "\"machines\": [\n" +
                "{\n" +
                "\"SerialNum\": \"012345678\",\n" +
                "\"location\": \"N1 525\",\n" +
                "\"isUse\": \"using\"\n" +
                "},\n" +
                "{\n" +
                "\"SerialNum\": \"012345678\",\n" +
                "\"location\": \"N1 525\",\n" +
                "\"isUse\": \"using\"\n" +
                "}\n" +
                "]\n" +
                "}";

        JSONArray list_machines;
        try {
            list_machines = new JSONObject(list_machines_json).getJSONArray("machines");
        } catch (JSONException e) {
            list_machines = null;
            Log.d("ExploreActivity", "Failed to load list");

        }

        if (list_machines != null) {
            int i;
            List<JSONObject> arraylist_machines = new ArrayList<>();
            for (i = 0; i < list_machines.length(); i++) {
                try {
                    arraylist_machines.add(list_machines.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ExploreActivity", "Failed to get JSONObject");

                }
            }
            View fragmentView = getView();
            if (fragmentView == null) {
                return;
            }
            ListView list = (ListView) fragmentView.findViewById(R.id.list_machines);
            MachineListAdapter adapter = new MachineListAdapter(getActivity(), R.layout.item_machine, arraylist_machines);
            list.setAdapter(adapter);
        }
    }
}
