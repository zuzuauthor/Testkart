package com.testkart.exam.testkart.study_material;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.testkart.exam.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by elfemo on 8/8/17.
 */

public class StudyMaterialAdapter extends BaseAdapter {

    private List<StudyMaterial> dataSet;
    private LayoutInflater inflater;

    private HashMap<String, Integer> fileTypes = new HashMap<>();

    /*
    'pdf','zip','rar','doc','docx','xls','xlsx','ppt','pptx','txt','rtf','jpg','png','bmp'


     */

    private String KEY_TYPE_PDF = "pdf";
    private String KEY_TYPE_ZIP = "zip";
    private String KEY_TYPE_RAR = "rar";
    private String KEY_TYPE_DOC = "doc";
    private String KEY_TYPE_DOCX = "docx";
    private String KEY_TYPE_XLS = "xls";
    private String KEY_TYPE_XLSX = "xlsx";
    private String KEY_TYPE_PPT = "ppt";
    private String KEY_TYPE_PPTX = "pptx";
    private String KEY_TYPE_TXT = "txt";
    private String KEY_TYPE_RTF = "rtf";
    private String KEY_TYPE_JPG = "jpg";
    private String KEY_TYPE_PNG = "png";
    private String KEY_TYPE_BMP = "bmp";


    private void buildFileTypes() {

        fileTypes.put(KEY_TYPE_PDF, new Integer(R.drawable.pdf));
        fileTypes.put(KEY_TYPE_ZIP, new Integer(R.drawable.zip));
        fileTypes.put(KEY_TYPE_RAR, new Integer(R.drawable.rar));
        fileTypes.put(KEY_TYPE_DOC, new Integer(R.drawable.doc));
        fileTypes.put(KEY_TYPE_DOCX, new Integer(R.drawable.doc));
        fileTypes.put(KEY_TYPE_XLS, new Integer(R.drawable.xls));
        fileTypes.put(KEY_TYPE_XLSX, new Integer(R.drawable.xls));
        fileTypes.put(KEY_TYPE_PPT, new Integer(R.drawable.ppt));
        fileTypes.put(KEY_TYPE_PPTX, new Integer(R.drawable.ppt));
        fileTypes.put(KEY_TYPE_TXT, new Integer(R.drawable.txt));
        fileTypes.put(KEY_TYPE_RTF, new Integer(R.drawable.rtf));
        fileTypes.put(KEY_TYPE_JPG, new Integer(R.drawable.jpg));
        fileTypes.put(KEY_TYPE_PNG, new Integer(R.drawable.png));
        fileTypes.put(KEY_TYPE_BMP, new Integer(R.drawable.jpg));
    }

    private int getFileType(String type){

        Integer i = fileTypes.get(type);

        return i.intValue();
    }


    public StudyMaterialAdapter(Context context, List<StudyMaterial> dataSet){

        buildFileTypes();

        this.dataSet = dataSet;

        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataSet.indexOf(dataSet.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder mHolder;

        if(view == null){

            view = inflater.inflate(R.layout.item_study_material, viewGroup, false);

            mHolder = new ViewHolder();

            mHolder.fileName = (TextView)view.findViewById(R.id.fileName);
            mHolder.fileType = (ImageView)view.findViewById(R.id.fileType);


            view.setTag(mHolder);


        }else{


            mHolder = (ViewHolder) view.getTag();

        }


        StudyMaterial studyMaterial = (StudyMaterial) getItem(i);

        if(studyMaterial != null){

            mHolder.fileName.setText(studyMaterial.getFileName());
            mHolder.fileType.setImageResource(getFileType(studyMaterial.getFileType()));
        }


        return view;
    }


    class ViewHolder{

        private TextView fileName;
        private ImageView fileType;

    }

}
