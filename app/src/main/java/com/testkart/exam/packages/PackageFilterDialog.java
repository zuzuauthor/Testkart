package com.testkart.exam.packages;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.register.GroupModel;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.packages.filter.GroupAdapter;
import com.testkart.exam.packages.filter.PackageAdapter;
import com.testkart.exam.packages.model.PackageDataModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by elfemo on 6/9/17.
 */

public class PackageFilterDialog extends DialogFragment /*implements View.OnClickListener*/{

    private TextView sort, packages, price, expiryDate, group;

    private TextView clear, apply;

    private FrameLayout close;

    private int currentFilterOption;

    private LinearLayout filterSort, filterPackages, filterPrice, filterExpiry;
    private FrameLayout  filterGroup;

    private ListView filterPackList, groupListView;
    private ProgressBar progressBar;

    //date filter
    private TextView startDate, endDate;

    //price filter
    private RadioGroup myRadioGroup;

    //sorting filter
    private RadioGroup myRadioGroupSort;


    //group filter
    private String groupVal = "";

    //package filter
    private String packageVal = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Fullscreen);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter_packages, null);

        initViews(rootView);

        builder.setView(rootView);

        return builder.create();
    }

    private void initViews(View view) {

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        sort = (TextView) view.findViewById(R.id.sort);
        packages = (TextView) view.findViewById(R.id.packages);
        price = (TextView) view.findViewById(R.id.price);
        expiryDate = (TextView) view.findViewById(R.id.expiryDate);
        group = (TextView) view.findViewById(R.id.group);

        sortListener();
        packagesListener();
        priceListener();
        expiryDateListener();
        groupListener();

        filterSort = (LinearLayout) view.findViewById(R.id.filterSort);
        filterSort.setVisibility(View.VISIBLE);
        filterPackages = (LinearLayout) view.findViewById(R.id.filterPackages);
        filterPackages.setVisibility(View.GONE);
        filterPrice = (LinearLayout) view.findViewById(R.id.filterPrice);
        filterPrice.setVisibility(View.GONE);
        filterExpiry = (LinearLayout) view.findViewById(R.id.filterExpiry);
        filterExpiry.setVisibility(View.GONE);
        filterGroup = (FrameLayout) view.findViewById(R.id.filterGroup);
        filterGroup.setVisibility(View.GONE);

        filterPackList = (ListView) view.findViewById(R.id.filterPackList);
        groupListView = (ListView)view.findViewById(R.id.groupListView);
       // filterPackList.setItemsCanFocus(true);
      //  groupListView.setItemsCanFocus(true);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        updateViews(1);

        close = (FrameLayout)view.findViewById(R.id.close);
        closeListener();

        clear = (TextView)view.findViewById(R.id.clear);
        apply = (TextView)view.findViewById(R.id.apply);

        clearListener();
        applyListener();

        updatePackage();
        updateGroup();


        //date filter view
        startDate = (TextView)view.findViewById(R.id.startDate);
        endDate = (TextView)view.findViewById(R.id.endDate);
        startDateListener();
        endDateListener();


        //price filter
        myRadioGroup = (RadioGroup)view.findViewById(R.id.myRadioGroup);
        myRadioGroupListener();

        //sort filter
        myRadioGroupSort = (RadioGroup)view.findViewById(R.id.myRadioGroupSort);
        myRadioGroupSortListener();


        //group filter
       // groupListViewListener();

        //package filter
       // filterPackListListener();
    }


    private void updateGroup() {

        if(ApiClient.isNetworkAvailable(getContext())){

            //manual login
            getGroups();

        }else{

            Toast.makeText(getContext(), Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }


    ArrayList<PackageDataModel> packDataSet = new ArrayList<>();
    private void updatePackage() {

       // packDataSet.clear();

        ArrayList<PackageDataModel> packDataSet = (ArrayList<PackageDataModel>) getArguments().getSerializable("PL");

        this.packDataSet.clear();
        this.packDataSet = packDataSet;

        PackageAdapter packageAdapter = new PackageAdapter(getContext(), packDataSet);

        filterPackList.setAdapter(packageAdapter);

    }

    private void applyListener() {

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applyFilter();

            }
        });
    }

    private void applyFilter() {

        switch (currentFilterOption){

            case 1:

                String groupIds = "";

                for (GroupModel pm:
                        groupDataSet) {

                    if(pm.isGroupChecked()){

                        groupIds += pm.getGroupId()+",";

                    }

                }

                Log.v("Group Ids", groupIds);

                if(mListener != null){

                    mListener.onFilter(1, groupIds.replaceAll(",$", ""));

                    //mListener.onFilter(1, groupVal);

                    dismiss();
                }

                break;

            case 2:


                String packIds = "";

                for (PackageDataModel pm:
                        packDataSet) {

                    if(pm.isPackageChecked()){

                        packIds += pm.getPackageId()+",";

                    }

                }

                Log.v("Pack Ids", packIds);


                if(mListener != null){

                    mListener.onFilter(2, packIds.replaceAll(",$", ""));
                  //  mListener.onFilter(2, packageVal);

                    dismiss();
                }

                break;

            case 3:

                if(mListener != null){

                    Log.v("Price Range", priceVal);
                   // mListener.onFilter(3, "0,400");
                    mListener.onFilter(3, priceVal.replaceAll(",$", ""));

                    dismiss();
                }

                break;

            case 4:

               // if(mListener != null){

                    if(startDate1 != null && endDate1 != null){

                        if(endDate1.after(startDate1)||endDate1.equals(startDate1)){

                            if(mListener != null){

                                Log.v("Date Range", startDate.getText().toString().trim()+","+
                                        endDate.getText().toString().trim());

                                mListener.onFilter(4, startDate.getText().toString().trim()+","+
                                endDate.getText().toString().trim());

                                dismiss();
                            }

                        }else{

                            Toast.makeText(getContext(), "End date must be greater than start date.", Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        Toast.makeText(getContext(), "Start Date and End Date cannot be empty.", Toast.LENGTH_SHORT).show();
                    }

               // }

                break;

            case 5:

                Log.v("Sort Val", sortVal);

                if(mListener != null){

                    mListener.onFilter(5, sortVal);

                    dismiss();
                }

                break;
        }

    }

    private void clearListener() {

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateViews(1);

                updatePackage();
                updateGroup();

                myRadioGroup.clearCheck();
                myRadioGroupSort.clearCheck();

                //reset date
                startDate1 = null;
                endDate1 = null;
                startDate.setText("Enter Start Date");
                endDate.setText("Enter End Date");

            }
        });

    }

    private void closeListener() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
    }

    private void groupListener() {

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateViews(1);

            }
        });
    }

    private void expiryDateListener() {

        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateViews(4);

            }
        });
    }

    private void priceListener() {

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateViews(3);

            }
        });
    }

    private void packagesListener() {

        packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateViews(2);

            }
        });
    }

    private void sortListener() {

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateViews(5);

            }
        });
    }


    private void updateViews(int i) {

        // 1 group, 2 packages, 3 price, 4 expiry, 5 sort

        switch (i){

            case 1:

                //highlight tab
                sort.setBackgroundColor(0);
                packages.setBackgroundColor(0);
                price.setBackgroundColor(0);
                expiryDate.setBackgroundColor(0);
                group.setBackgroundColor(Color.parseColor("#68C6EC"));


                //visible container sort
                filterSort.setVisibility(View.GONE);
                filterPackages.setVisibility(View.GONE);
                filterPrice.setVisibility(View.GONE);
                filterExpiry.setVisibility(View.GONE);
                filterGroup.setVisibility(View.VISIBLE);

                currentFilterOption = 1;

                break;

            case 2:

                //highlight tab
                sort.setBackgroundColor(0);
                packages.setBackgroundColor(Color.parseColor("#68C6EC"));
                price.setBackgroundColor(0);
                expiryDate.setBackgroundColor(0);
                group.setBackgroundColor(0);


                //visible container sort
                filterSort.setVisibility(View.GONE);
                filterPackages.setVisibility(View.VISIBLE);
                filterPrice.setVisibility(View.GONE);
                filterExpiry.setVisibility(View.GONE);
                filterGroup.setVisibility(View.GONE);

                currentFilterOption = 2;

                break;

            case 3:

                //highlight tab
                sort.setBackgroundColor(0);
                packages.setBackgroundColor(0);
                price.setBackgroundColor(Color.parseColor("#68C6EC" ));
                expiryDate.setBackgroundColor(0);
                group.setBackgroundColor(0);


                //visible container sort
                filterSort.setVisibility(View.GONE);
                filterPackages.setVisibility(View.GONE);
                filterPrice.setVisibility(View.VISIBLE);
                filterExpiry.setVisibility(View.GONE);
                filterGroup.setVisibility(View.GONE);

                currentFilterOption = 3;

                break;

            case 4:

                //highlight tab
                sort.setBackgroundColor(0);
                packages.setBackgroundColor(0);
                price.setBackgroundColor(0);
                expiryDate.setBackgroundColor(Color.parseColor("#68C6EC"));
                group.setBackgroundColor(0);


                //visible container sort
                filterSort.setVisibility(View.GONE);
                filterPackages.setVisibility(View.GONE);
                filterPrice.setVisibility(View.GONE);
                filterExpiry.setVisibility(View.VISIBLE);
                filterGroup.setVisibility(View.GONE);

                currentFilterOption = 4;

                break;

            case 5:

                //highlight tab
                sort.setBackgroundColor(Color.parseColor("#68C6EC"));
                packages.setBackgroundColor(0);
                price.setBackgroundColor(0);
                expiryDate.setBackgroundColor(0);
                group.setBackgroundColor(0);


                //visible container sort
                filterSort.setVisibility(View.VISIBLE);
                filterPackages.setVisibility(View.GONE);
                filterPrice.setVisibility(View.GONE);
                filterExpiry.setVisibility(View.GONE);
                filterGroup.setVisibility(View.GONE);

                currentFilterOption = 5;

                break;
        }

    }

/*    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.sort:

                updateViews(1);

                break;


            case R.id.packages:

                updateViews(2);

                break;

            case R.id.price:

                updateViews(3);

                break;

            case R.id.expiryDate:

                updateViews(4);

                break;

            case R.id.group:

                updateViews(5);

                break;

        }
    }*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (FilterListener)context;

        }catch (Exception e){

            e.printStackTrace();
        }
    }


    private FilterListener mListener;

    public interface FilterListener{

        void onFilter(int filterType, String filterValue);
    }




    ArrayList<GroupModel> groupDataSet = new ArrayList<>();
    private void getGroups() {

        new AsyncTask<String, String, String>(){


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressBar.setVisibility(View.VISIBLE);
                groupListView.setVisibility(View.GONE);

            }

            @Override
            protected String doInBackground(String... strings) {

                try{

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);



                    Call<String> call = apiService.getGroupScalar();

                    Response<String> response = call.execute();

                    final int code = response.code();

                    if(code == 200){

                        return response.body();

                    }else{

                        return null;
                    }

                }catch (Exception e){

                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null) {

                    try{

                        //hide progress

                        JSONObject jObj = new JSONObject(s);

                        boolean status = jObj.getBoolean("status");
                        String message = jObj.getString("message");

                        if (status) {

                            JSONObject groups = jObj.getJSONObject("group");

                            Iterator keys = groups.keys();

                            groupDataSet.clear();

                            for (int i = 0; keys.hasNext(); i++) {

                                GroupModel gpm = new GroupModel();

                                String currentDynamicKey = (String) keys.next();
                                String keyValue = groups.getString(currentDynamicKey);

                                gpm.setGroupId(currentDynamicKey);
                                gpm.setGroupName(keyValue);


                                groupDataSet.add(gpm);

                            }

                            groupDataSet.remove(0);

                            GroupAdapter groupAdapter = new GroupAdapter(getContext(), groupDataSet);
                            groupListView.setAdapter(groupAdapter);

                            progressBar.setVisibility(View.GONE);
                            groupListView.setVisibility(View.VISIBLE);

                        }else{

                            Toast.makeText(getContext(), "No Groups Found!", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {

                        e.printStackTrace();
                    }

                }else{

                    Toast.makeText(getContext(), "RESPONSE BODY NULL", Toast.LENGTH_SHORT).show();
                }

            }


        }.execute(null, null, null);

    }


    /*

    Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        endDate.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


     */


    private SimpleDateFormat dateFormatter;
    private Calendar endDate1;
    private void endDateListener() {

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate1 = Calendar.getInstance();
                        endDate1.set(year, monthOfYear, dayOfMonth);
                        endDate.setText(dateFormatter.format(endDate1.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });

    }


    Calendar startDate1;
    private void startDateListener(){

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate1 = Calendar.getInstance();
                        startDate1.set(year, monthOfYear, dayOfMonth);
                        startDate.setText(dateFormatter.format(startDate1.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });

    }


    //price filter
    private String priceVal = "";
    private void myRadioGroupListener() {

        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

              //  Toast.makeText(getContext(), "Prise id: "+i, Toast.LENGTH_SHORT).show();

                if(R.id.below == i){

                    priceVal = "0,500";

                }else if(R.id.above == i){

                    priceVal = "500,999";

                }
            }
        });

    }


    //filter sort
    private String sortVal = "";
    private void myRadioGroupSortListener() {

        myRadioGroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

               // Toast.makeText(getContext(), "Sort id: "+i, Toast.LENGTH_SHORT).show();

                if(R.id.whatNew == i){

                    sortVal = "1";

                }else if(R.id.popularity == i){

                    sortVal = "2";

                }else if(R.id.priceHighLow == i){

                    sortVal = "3";

                }else if(R.id.priceLowHigh == i){

                    sortVal = "4";

                }
            }
        });

    }




    //group filter
    private void groupListViewListener() {

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GroupModel groupModel = (GroupModel) groupListView.getItemAtPosition(i);

              //  Toast.makeText(getContext(), "Group Id: "+groupModel.getGroupId(), Toast.LENGTH_SHORT).show();

                groupVal += groupModel.getGroupId()+",";
            }
        });

    }





    //package filter
    private void filterPackListListener() {

        filterPackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                PackageDataModel groupModel = (PackageDataModel) filterPackList.getItemAtPosition(i);


             //   Toast.makeText(getContext(), "Pack Id: "+groupModel.getPackageId(), Toast.LENGTH_SHORT).show();

                packageVal += groupModel.getPackageId()+",";

                Log.v("Filter_data", "Filter Type: 2"+" Filter Value: "+packageVal);
            }
        });

    }

}


