package com.example.retrofidpelanbismillah.view.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofidpelanbismillah.R;
import com.example.retrofidpelanbismillah.api.BaseApiService;
import com.example.retrofidpelanbismillah.api.UtilsAPI;
import com.example.retrofidpelanbismillah.modul.ReadDataResponse;
import com.example.retrofidpelanbismillah.modul.Record;
import com.example.retrofidpelanbismillah.view.activity.AddActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadFragment extends Fragment {


    private List<ReadDataResponse> mCategoryDatalist = new ArrayList<>();
    private List<Record> listCategory = new ArrayList<>();
    private RecyclerView rc_list_Category;

    private BaseApiService mApiService;
    private CategoryAdapter CategoryAdapter;



    public ReadFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_read, container, false);

        mApiService= UtilsAPI.getApiService();

        rc_list_Category = (RecyclerView) rootView.findViewById(R.id.rc_list_category);

        @SuppressLint("WrongConstant") LinearLayoutManager layoutManagerCategory
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());


        CategoryAdapter = new CategoryAdapter(listCategory, getActivity());
        rc_list_Category.setLayoutManager(layoutManagerCategory);
        rc_list_Category.setItemAnimator(new DefaultItemAnimator());
        rc_list_Category.setAdapter(CategoryAdapter);

        rootView.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goAdd = new Intent(getActivity(), AddActivity.class);
                goAdd.putExtra("intent_id", "");
                goAdd.putExtra("intent_tgl", "");
                goAdd.putExtra("intent_masuk", "");
                goAdd.putExtra("intent_keluar", "");
                goAdd.putExtra("intent_ketmasuk", "");
                goAdd.putExtra("intent_ketkeluar", "");
                goAdd.putExtra("intent_action", "insert");
                startActivity(goAdd);
            }
        });

        return rootView;
    }

    private void dataAttachmentCategory(){
        rc_list_Category.setVisibility(View.GONE);


       mCategoryDatalist.clear();
        listCategory.clear();
        mApiService.readData(
                "read",
                "BCA" )
                .enqueue(new Callback<ReadDataResponse>() {
                    @Override
                    public void onResponse(Call<ReadDataResponse> call, Response<ReadDataResponse> response) {


                        if (response.isSuccessful()) {
                            try {
                                int total = response.body().getRecords().size();

                                for (int a = 0; a < total; a++) {
                                    Record modelSeatGroup = new Record(
                                            response.body().getRecords().get(a).getID(),
                                            response.body().getRecords().get(a).getTGL(),
                                            response.body().getRecords().get(a).getMASUK(),
                                            response.body().getRecords().get(a).getKELUAR(),
                                            response.body().getRecords().get(a).getKETERANGANMASUK(),
                                            response.body().getRecords().get(a).getKETERANGANKELUAR());
                                    listCategory.add(modelSeatGroup);

                                }

                                ReadDataResponse item = new ReadDataResponse(
                                        listCategory
                                );
                                mCategoryDatalist.add(item);

                                CategoryAdapter = new CategoryAdapter(listCategory, getActivity());
                                rc_list_Category.setAdapter(CategoryAdapter);

                                if (listCategory.isEmpty()) {
                                    rc_list_Category.setVisibility(View.GONE);

                                    Toast.makeText(getActivity(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                                } else {
                                    rc_list_Category.setVisibility(View.VISIBLE);
                                }


                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Please try again, server is down", Toast.LENGTH_SHORT).show();
                        }
                    }


                        @Override
                        public void onFailure(Call<ReadDataResponse> call, Throwable t){

                            Toast.makeText(getActivity(), "Please try again, server is down onfail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

        @Override
        public void onResume() {
            super.onResume();



            dataAttachmentCategory();
        }
    }




