package ch.epfl.calendar;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
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
    
    private static final int HEIGHTSPINNER = 100;

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
        
        /*ViewGroup.LayoutParams parameters = view.getLayoutParams();
        parameters.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(parameters);*/
        
        return view;
    }
    
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        
        TextView text = (TextView) view
                .findViewById(android.R.id.text1);
        text.setTextColor(Color.WHITE);
        text.setGravity(Gravity.CENTER_VERTICAL);
        
        ViewGroup.LayoutParams parameters = view.getLayoutParams();
        parameters.height = HEIGHTSPINNER;
        view.setLayoutParams(parameters);
        
        return view;
    }
}
