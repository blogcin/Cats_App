package kr.co.alteration.cat.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.co.alteration.cat.R;

/**
 * Created by blogc on 2016-09-24.
 */
public class ListAdapter extends BaseAdapter {
    private ArrayList<ListItem> listViewItemList = new ArrayList<ListItem>() ;

    public void addItem(ListItem item) {
        listViewItemList.add(item);
    }

    public void removeItem(int index) {
        listViewItemList.remove(index);
    }

    public void setListViewItemList(ArrayList<ListItem> listViewItemList) {
        this.listViewItemList = listViewItemList;
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i) ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list_view, viewGroup, false);
        }

        TextView tvTimes = (TextView) view.findViewById(R.id.tv_times);
        ListItem listViewItem = listViewItemList.get(i);

        tvTimes.setText(listViewItem.getTimes() + " 초");
        return view;
    }
}
