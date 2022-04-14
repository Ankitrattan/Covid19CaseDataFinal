package com.app.covid19casedata.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.covid19casedata.adapter.CaseDataListAdapter;
import com.app.covid19casedata.adapter.ProvinceListAdapter;
import com.app.covid19casedata.data.CaseData;
import com.app.covid19casedata.databinding.FragmentCasedataDetailBinding;
import com.app.covid19casedata.db.DatabaseHelper;

import java.util.ArrayList;
// this is work of ankit rattan and dilawar singh sangha
public class CaseDataDetailFragment extends Fragment {

    private FragmentCasedataDetailBinding binding;
    private DatabaseHelper mDatabaseHelper;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCasedataDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatabaseHelper = new DatabaseHelper(getContext());
        new GetCovid19CaseDataFromDB().execute();
    }


    public class GetCovid19CaseDataFromDB extends AsyncTask<String, Void, ArrayList<CaseData>> {


        public GetCovid19CaseDataFromDB() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<CaseData> doInBackground(String... params) {
            return (ArrayList<CaseData>) mDatabaseHelper.getAllProvince(getArguments().getString("country"),getArguments().getString("date"));
        }

        protected void onPostExecute(ArrayList<CaseData> result) {
            super.onPostExecute(result);
            binding.progressbar.setVisibility(View.GONE);
            if(result != null && result.size() > 0){
                binding.list.setAdapter(new ProvinceListAdapter(result));
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}