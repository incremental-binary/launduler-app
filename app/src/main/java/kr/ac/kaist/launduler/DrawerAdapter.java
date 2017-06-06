package kr.ac.kaist.launduler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by woonggyu on 2017. 6. 6..
 */
public class DrawerAdapter extends ArrayAdapter {
    LayoutInflater vi = null;
    String[] string_menu = null;
    public DrawerAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
        vi = LayoutInflater.from(context);
        string_menu = (String[]) objects;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            v = vi.inflate(R.layout.drawer_list_item,null);
            TextView t = (TextView)v.findViewById(R.id.drawer_text);
            t.setText(string_menu[position]);
        }
            return v;
    }
}
