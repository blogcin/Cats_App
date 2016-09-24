package kr.co.alteration.cat.ui.grid;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import kr.co.alteration.cat.R;
import kr.co.alteration.cat.client.ClientConstants;
import kr.co.alteration.cat.ui.AccessListActivity;

/**
 * Created by blogc on 2016-09-24.
 */
public class GridAdapter extends BaseAdapter {
    private final List<String> list;
    private final LayoutInflater inflater;
    private Calendar mCal;
    private Context context;
    private String date;

    public GridAdapter(Context context, List<String> list, String date) {
        this.list = list;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.date = date;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.item_calendar_gridview, viewGroup, false);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AccessListActivity.class);
                    intent.putExtra(ClientConstants.DATA_DATE, date + "/" + getItem(i));
                    context.startActivity(intent);
                }
            });
            viewHolder = new ViewHolder();

            viewHolder.tvItemGridView = (TextView) view.findViewById(R.id.tv_item_gridview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvItemGridView.setText("" + getItem(i));

        mCal = Calendar.getInstance();

        int today = mCal.get(Calendar.DAY_OF_MONTH);
        String sToday = String.valueOf(today);

        if (sToday.equals(getItem(i))) {
            if (Build.VERSION.SDK_INT >= 23) {
                viewHolder.tvItemGridView.setTextColor(ContextCompat.getColor(context, R.color.color_000000));
            } else {
                viewHolder.tvItemGridView.setTextColor(context.getResources().getColor(R.color.color_000000));
            }
        }

        return view;
    }
}
