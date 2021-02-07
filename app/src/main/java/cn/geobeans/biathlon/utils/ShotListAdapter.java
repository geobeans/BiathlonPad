package cn.geobeans.biathlon.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
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

        final Shooting shot = mDatas.get(position);
        Date date = shot.getTime1();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.item_shooting_time.setText(ft.format(date));
        holder.item_shooting_model.setText("zeroing-1");
        holder.item_shooting_zone.setText(shot.getLane());

        float[] hitX = new float[8];
        float[] hitY = new float[8];
        hitX[0] = shot.getX1();
        hitY[0] = shot.getY1();
        hitX[1] = shot.getX2();
        hitY[1] = shot.getY2();
        hitX[2] = shot.getX3();
        hitY[2] = shot.getY3();
        hitX[3] = shot.getX4();
        hitY[3] = shot.getY4();
        hitX[4] = shot.getX5();
        hitY[4] = shot.getY5();
        hitX[5] = shot.getX6();
        hitY[5] = shot.getY6();
        hitX[6] = shot.getX7();
        hitY[6] = shot.getY7();
        hitX[7] = shot.getX8();
        hitY[7] = shot.getY8();
        holder.item_shooting_target.setHitX(hitX);
        holder.item_shooting_target.setHitY(hitY);

        return convertView;
    }

    class vHolder {
        TextView item_shooting_time;
        TextView item_shooting_model;
        TextView item_shooting_prone;
        TextView item_shooting_zone;
        SimpleTargetView item_shooting_target;
    }
}
