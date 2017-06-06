package kr.ac.kaist.launduler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kr.ac.kaist.launduler.models.Machine;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by woonggyu on 2017. 5. 24..
 */
public class MachineListAdapter extends ArrayAdapter<Machine> {
    private static final String TAG = MachineListAdapter.class.getSimpleName();

    private final Context context;
    private List<Machine> machines;

    public MachineListAdapter(Context context, int resource, List<Machine> objects) {
        super(context, resource, objects);
        this.context = context;
        this.machines = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.item_machine, null, true);

        TextView machineName = (TextView) rowView.findViewById(R.id.machine_name);
        ImageView machineImage = (ImageView) rowView.findViewById(R.id.machine_image);
        TextView machineStatus = (TextView) rowView.findViewById(R.id.machine_status);

        Machine machine = machines.get(position);
        machineName.setText(machine.getSerialNum());
        machineImage.setImageResource(R.mipmap.ic_launcher);
        CharSequence statusText;
        if (machine.isBroken()) {
            statusText = context.getText(R.string.machine_out_of_order);
        } else if (machine.getInUse()) {
            statusText = context.getText(R.string.machine_in_use);
        } else {
            statusText = context.getText(R.string.machine_available);
        }
        machineStatus.setText(statusText);

        Log.d(TAG, machine.getSerialNum());

        return rowView;
    }


}
