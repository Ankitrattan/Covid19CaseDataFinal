package com.app.covid19casedata.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.covid19casedata.R;
import com.app.covid19casedata.data.CaseData;
import com.app.covid19casedata.db.DatabaseHelper;
import com.app.covid19casedata.ui.fragment.CaseDataListFragment;
import com.app.covid19casedata.utils.IRecyclerviewItemSelectListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CaseDataListAdapter extends RecyclerView.Adapter<CaseDataListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<CaseData> mCountryCaseDataList;
    private IRecyclerviewItemSelectListener mListener;
    private String mFlag;

    public CaseDataListAdapter(Context context,CaseDataListFragment caseDataListFragment, String flag,ArrayList<CaseData> mCountryCaseDataList) {
        mContext = context;
        this.mCountryCaseDataList = mCountryCaseDataList;
        mListener = caseDataListFragment;
        mFlag = flag;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.listrow_casedata, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CaseData mCountryCaseData = mCountryCaseDataList.get(holder.getAdapterPosition());
        holder.countryTxt.setText(mCountryCaseData.getCountry());
        holder.dateTxt.setText(mCountryCaseData.getDate());
        holder.deleteImg.setVisibility(mFlag.equalsIgnoreCase("search") ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("country",mCountryCaseData.getCountry());
                bundle.putString("date",mCountryCaseData.getDate());
                mListener.onItemSelect(bundle); // item select listener
            }
        });

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(mContext);
                confirmDialog.setTitle(R.string.confirm);
                confirmDialog.setMessage(R.string.delete_msg);
                confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                        int status = databaseHelper.deleteCountryDate(mCountryCaseData);
                        Log.i("delete",status+ " ");
                        mCountryCaseDataList.remove(mCountryCaseData);
                        notifyItemRemoved(holder.getAdapterPosition());
                        dialogInterface.dismiss();
                        try {
                            Snackbar.make(holder.itemView,"Item deleted", Snackbar.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                confirmDialog.setNegativeButton("No",null);
                confirmDialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mCountryCaseDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView countryTxt,dateTxt;
        public ImageView deleteImg;

        public ViewHolder(View itemView) {
            super(itemView);
            this.countryTxt = (TextView) itemView.findViewById(R.id.txt_country);
            this.dateTxt = (TextView) itemView.findViewById(R.id.txt_date);
            this.deleteImg = itemView.findViewById(R.id.img_delete);
        }
    }
} 
