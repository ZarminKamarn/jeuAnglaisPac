package p1803242.jeuxanglais.AdminManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import p1803242.jeuxanglais.R;

public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> list;
    ViewHolder viewHolder;

    public MyAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout layoutItem;
        LayoutInflater minInflater = LayoutInflater.from(context);
        //(1) : Réutilisation des layouts
        if (view == null)
            layoutItem = (LinearLayout) minInflater.inflate(R.layout.item_layout, viewGroup, false);
        else layoutItem = (LinearLayout) view;


        viewHolder = (ViewHolder)layoutItem.getTag();
        if(viewHolder == null){
            viewHolder = new ViewHolder();

            viewHolder.tv = layoutItem.findViewById(R.id.itemText);
            layoutItem.setTag(viewHolder);
        }

        String currItem = (String)getItem(i);
        viewHolder.tv.setText(currItem);

        return layoutItem;
    }

    private class ViewHolder{
        TextView tv;
    }
}
