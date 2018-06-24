package com.testkart.exam.edu.exam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by testkart on 27/4/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater inflater;

    private ArrayList<ExpandableHeader> headerData;
    private HashMap<ExpandableHeader, ArrayList<QuestionGridModel>> childData;

    ArrayList<QuestionGridModel> ql = new ArrayList<QuestionGridModel>();

    private OnQuestionPickListener mListener;
    public interface OnQuestionPickListener{

        void onQuestionLoad(int qNo);
    }

    public ExpandableListAdapter(Context context,
                                 ArrayList<ExpandableHeader> headerData,
                                 HashMap<ExpandableHeader, ArrayList<QuestionGridModel>> childData){

        mListener = (OnQuestionPickListener)context;

        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.headerData = headerData;
        this.childData = childData;
    }


    @Override
    public int getGroupCount() {
        return this.headerData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1; //this.childData.get(this.headerData.get(groupPosition)).size()
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childData.get(this.headerData.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_group, parent, false);
        }

        ExpandableHeader h = (ExpandableHeader) getGroup(groupPosition);

        TextView tv = (TextView) convertView.findViewById(R.id.viewSubjectName);
        TextView tv1 = (TextView) convertView.findViewById(R.id.viewQuestionCount);
        tv.setText(h.getSubjectName());
        tv1.setText("Questions: "+h.getQuestionCount());


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        ArrayList<QuestionGridModel> ql = childData.get(headerData.get(groupPosition));

        GridView gridView = (GridView) convertView.findViewById(R.id.gridview);
        gridView.setNumColumns(4);
        gridView.setHorizontalSpacing(5);
        gridView.setHorizontalSpacing(5);
        QuestionGridAdapter adapter = new QuestionGridAdapter(mContext, ql);
        gridView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int gp = groupPosition;
                int previousChildCount = 0;

                if(groupPosition != 0){

                    for (int i = groupPosition; i > 0; i--) {

                        previousChildCount += childData.get(headerData.get(--gp)).size();
                    }


                    int currentChildCount = previousChildCount + position+1; //(childData.get(headerData.get(groupPosition)).size() - (position));

                    if(mListener != null){

                        mListener.onQuestionLoad(currentChildCount);

                    }

                    Toast.makeText(mContext, currentChildCount+"", Toast.LENGTH_SHORT).show();

                }else{

                    if(mListener != null){

                        mListener.onQuestionLoad(position+1);

                    }

                    Toast.makeText(mContext, position+1+"", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void setData() {

        notifyDataSetChanged();

    }

}
