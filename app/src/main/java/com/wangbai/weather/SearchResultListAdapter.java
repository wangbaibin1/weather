package com.wangbai.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binwang on 2015/11/10.
 */
public class SearchResultListAdapter extends BaseAdapter{
    private List<SearResultData> mDatas = new ArrayList<>();
    private Context mContext;
    public SearchResultListAdapter(Context context){
        mContext = context;
    }

    public void setDatas(List<SearResultData> data){
        mDatas = data;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        SearResultData data = (SearResultData) getItem(position);
        title.setText(data.mName);
        content.setText(data.mCountry +"-" +data.mName);
        return convertView;
    }
}
