package com.app.covid19casedata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.covid19casedata.R;
import com.app.covid19casedata.data.CaseData;

import java.util.ArrayList;

// this is work of ayushi patel
public class ProvinceListAdapter extends RecyclerView.Adapter<ProvinceListAdapter.ViewHolder>{

    private ArrayList<CaseData> mCountryCaseDataList;

    public ProvinceListAdapter(ArrayList<CaseData> mCountryCaseDataList) {
        this.mCountryCaseDataList = mCountryCaseDataList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.listrow_province, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CaseData mCountryCaseData = mCountryCaseDataList.get(position);
        holder.provinceTxt.setText("Province: "+ (mCountryCaseData.getProvince() != null ? mCountryCaseData.getProvince() : ""));
        holder.countTxt.setText("Count: "+String.valueOf(mCountryCaseData.getCount()));
    }
    @Override
    public int getItemCount() {
        return mCountryCaseDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView provinceTxt,countTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            this.provinceTxt = (TextView) itemView.findViewById(R.id.txt_province);
            this.countTxt = (TextView) itemView.findViewById(R.id.txt_count);
        }
    }
} 
