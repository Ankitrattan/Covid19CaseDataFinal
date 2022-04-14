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
import com.app.covid19casedata.data.CaseData;
import com.app.covid19casedata.databinding.FragmentCasedataListBinding;
import com.app.covid19casedata.db.DatabaseHelper;
import com.app.covid19casedata.ui.activity.CaseDataListActivity;
import com.app.covid19casedata.utils.IRecyclerviewItemSelectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// this is work of dilawar singh sangha and ankit rattan
public class CaseDataListFragment extends Fragment implements IRecyclerviewItemSelectListener {

    private FragmentCasedataListBinding binding;
    private DatabaseHelper mDatabaseHelper;
    public String mFlag;
    public ArrayList<CaseData> mFetchedCaseDataList;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentCasedataListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatabaseHelper = new DatabaseHelper(getContext());
        mFlag = getActivity().getIntent().getExtras().getString("flag");
        new GetCovid19CaseDataFromDB().execute();

    }

    @Override
    public void onItemSelect(Bundle bundle) {
        String country = bundle.getString("country");
        String date = bundle.getString("date");
        NavHostFragment.findNavController(CaseDataListFragment.this)
                .navigate(CaseDataListFragmentDirections.actionListFragmentToDetailFragment(country,date));
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
            if(mFlag.equalsIgnoreCase("search")){
                String country = getActivity().getIntent().getExtras().getString("country");
                String fromDate = getActivity().getIntent().getExtras().getString("fromDate");
                String toDate = getActivity().getIntent().getExtras().getString("toDate");
                return (ArrayList<CaseData>) mDatabaseHelper.getSearchedCountryDateList(country,fromDate,toDate);
            }else{
                return (ArrayList<CaseData>) mDatabaseHelper.getAllCountryDateList();
            }
        }

        protected void onPostExecute(ArrayList<CaseData> result) {
            super.onPostExecute(result);
            binding.progressbar.setVisibility(View.GONE);
            if(result != null && result.size() > 0){
                binding.list.setAdapter(new CaseDataListAdapter(getContext(),CaseDataListFragment.this,mFlag,result));
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}