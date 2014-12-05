package ch.epfl.calendar.display;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.ListViewItem;

/**
 * @author MatthiasLeroyEPFL
 * 
 */
public class CustomAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int HEIGHT_VIEW = 100;
    private static final int SEPARATOR_ID = -2;
    private static int colorBlue;
    // private static final String COLOR_MAGENTA = "#AA66CC";
    private int colorGreen;
    private int colorRed;
    private int colorOrange;

    private ArrayList<ListViewItem> mData = new ArrayList<ListViewItem>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        colorBlue = context.getResources().getColor(R.color.blue10);
        colorGreen = context.getResources().getColor(R.color.green10);
        colorRed = context.getResources().getColor(R.color.red10);
        colorOrange = context.getResources().getColor(R.color.orange10);
    }

    public void addItem(final ListViewItem item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final ListViewItem item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ListViewItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);
                    holder.textView = (TextView) convertView
                            .findViewById(R.id.text);
                    holder.textView.setHeight(HEIGHT_VIEW);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    holder.textView = (TextView) convertView
                            .findViewById(R.id.textSeparator);
                    break;
                default:
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position).toString());
        if (mData.get(position).getmId() != SEPARATOR_ID) {
            switch (mData.get(position).getmType()) {
                case DEFAULT:
                    holder.textView.setBackgroundColor(colorOrange);
                    break;
                case EXERCISES:
                    holder.textView.setBackgroundColor(colorGreen);
                    break;
                case LECTURE:
                    holder.textView
                            .setBackgroundColor(colorBlue);
                    break;
                case PROJECT:
                    holder.textView.setBackgroundColor(colorRed);
                    break;
                default:
                    break;
            }
        }

        return convertView;
    }

    /**
     * @author MatthiasLeroyEPFL
     * 
     */
    public static class ViewHolder {
        private TextView textView;
    }
}
