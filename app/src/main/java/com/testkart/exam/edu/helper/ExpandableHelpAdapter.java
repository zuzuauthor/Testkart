package com.testkart.exam.edu.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.testkart.exam.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by testkart on 18/5/17.
 */

public class ExpandableHelpAdapter  extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater inflater;
    private HashMap<String, String> childDataSet;
    private ArrayList<String> headerDataSet;

    public ExpandableHelpAdapter(Context context, ArrayList<String> headerDataSet, HashMap<String, String> childDdataSet){

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.childDataSet = childDdataSet;
        this.headerDataSet = headerDataSet;

    }

    @Override
    public int getGroupCount() {
        return headerDataSet.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerDataSet.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return childDataSet.get(headerDataSet.get(groupPosition));
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_header_help, parent, false);

        }

        TextView header = (TextView)convertView.findViewById(R.id.header);

        String h = (String) getGroup(groupPosition);

        if(h != null){

            header.setText(h);

        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_child_help, parent, false);

        }


        String c = (String) getChild(groupPosition,childPosition);

        WebView web = (WebView)convertView.findViewById(R.id.child);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webSettings.setTextZoom(webSettings.getTextZoom() + 10);

       // web.setWebChromeClient(new WebChromeClient());

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return false;
            }
        });

        String q = MknUtils.changedHeaderHtml(c);

        web.loadData(q, "text/html", "UTF-8");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
