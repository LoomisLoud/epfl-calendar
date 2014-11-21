package ch.epfl.calendar;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author fouchepi
 *
 * @param <T>
 */
public class MySpinnerAdapter<T> extends ArrayAdapter<T> {

    /**
     * @param context
     * @param resource
     * @param objects
     */
    public MySpinnerAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text = (TextView) view
                .findViewById(android.R.id.text1);
        text.setTextColor(Color.WHITE);
        return view;
    }
    
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text = (TextView) view
                .findViewById(android.R.id.text1);
        text.setTextColor(Color.WHITE);
        return view;
    }
}
