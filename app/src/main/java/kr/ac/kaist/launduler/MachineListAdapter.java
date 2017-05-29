package kr.ac.kaist.launduler;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by woonggyu on 2017. 5. 24..
 */
public class MachineListAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;
    private List<JSONObject> objects;

    public MachineListAdapter(Context context, int resource, List<JSONObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.item_machine, null, true);

        TextView machineName = (TextView) rowView.findViewById(R.id.machine_name);
        ImageView machineImage = (ImageView) rowView.findViewById(R.id.machine_image);
        TextView machineStatus = (TextView) rowView.findViewById(R.id.machine_status);
        try {
            JSONObject machine = objects.get(position);
            machineName.setText(machine.getString("SerialNum"));
            machineImage.setImageResource(R.mipmap.ic_launcher);
            machineStatus.setText(machine.getString("isUse"));
            Log.d("asdf",machine.getString("SerialNum"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("asdf", "Fail");
        }


        return rowView;
    }


}
