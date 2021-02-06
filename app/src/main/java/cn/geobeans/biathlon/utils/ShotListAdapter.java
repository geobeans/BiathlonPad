package cn.geobeans.biathlon.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.geobeans.biathlon.R;
import cn.geobeans.biathlon.entity.Shooting;
import cn.geobeans.biathlon.target.SimpleTargetView;

public class ShotListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Shooting> mDatas;

    public ShotListAdapter(Context mContext, List<Shooting> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Shooting getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shooting_list, null);
            holder = new vHolder();
            holder.item_shooting_time = (TextView) convertView.findViewById(R.id.item_shooting_time);
            holder.item_shooting_model = (TextView) convertView.findViewById(R.id.item_shooting_model);
            holder.item_shooting_prone = (TextView) convertView.findViewById(R.id.item_shooting_prone);
            holder.item_shooting_zone = (TextView) convertView.findViewById(R.id.item_shooting_zone);
            holder.item_shooting_target = (SimpleTargetView) convertView.findViewById(R.id.item_shooting_target);
            convertView.setTag(holder);
        } else holder = (vHolder) convertView.getTag();



        return null;
    }

    class vHolder {
        TextView item_shooting_time;
        TextView item_shooting_model;
        TextView item_shooting_prone;
        TextView item_shooting_zone;
        SimpleTargetView item_shooting_target;
    }
}
