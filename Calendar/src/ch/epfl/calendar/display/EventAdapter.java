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
 * An adapter for the planning view.
 * @author MatthiasLeroyEPFL
 * source: http://www.codelearn.org/android-tutorial/android-listview
 * The source helps me to create this adapter
 * 
 */
public class EventAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int HEIGHT_VIEW = 150;
    private static final int SEPARATOR_ID = -2;
    private static int colorBlue;
    // private static final String COLOR_MAGENTA = "#AA66CC";
    private int colorGreen;
    private int colorRed;
    private int colorOrange;

    private ArrayList<ListViewItem> mData = new ArrayList<ListViewItem>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    /**
     * Creates a custom adapter.
     * @param context the context of the {@link Activity} using this adapter.
     */
    public EventAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        colorBlue = context.getResources().getColor(R.color.blue10);
        colorGreen = context.getResources().getColor(R.color.green10);
        colorRed = context.getResources().getColor(R.color.red10);
        colorOrange = context.getResources().getColor(R.color.orange10);
    }

    /**
     * Add an item to the list on screen
     * @param item the item to add
     */
    public void addItem(final ListViewItem item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    /**
     * Adds an header to separate the list in multiple sections on screen
     * @param item
     */
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

    /**
     * returns a view converted to be used by this adapter.
     */
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
        if (mData.get(position).getId() != SEPARATOR_ID) {
            switch (mData.get(position).getType()) {
                case DEFAULT:
                    holder.textView.setBackgroundColor(colorOrange);
                    break;
                case EXERCISES:
                    holder.textView.setBackgroundColor(colorGreen);
                    break;
                case LECTURE:
                    holder.textView.setBackgroundColor(colorBlue);
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
     * A holder for a {@link TextView}
     * @author MatthiasLeroyEPFL
     * 
     */
    public static class ViewHolder {
        private TextView textView;
    }
}
