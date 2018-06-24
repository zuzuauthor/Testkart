package com.testkart.exam.edu.leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.testkart.exam.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by testkart on 18/5/17.
 */

public class LeaderboardAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Leaderboard> dataSet;
    private Context context;

    public LeaderboardAdapter(Context context, List<Leaderboard> dataSet) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Leaderboard getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.indexOf(dataSet.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_leader, parent, false);

        }

        de.hdodenhof.circleimageview.CircleImageView studentImage = (CircleImageView) convertView.findViewById(R.id.viewProfilePic);
        TextView viewSName = (TextView) convertView.findViewById(R.id.studentName1);
        TextView examName = (TextView) convertView.findViewById(R.id.examName1);
        ImageView trophy = (ImageView) convertView.findViewById(R.id.trophy1);
        TextView percentage = (TextView) convertView.findViewById(R.id.percentage);
        ImageView viewRank = (ImageView) convertView.findViewById(R.id.viewRank1);

        TextView groupName = (TextView) convertView.findViewById(R.id.groupName);

        // TextView viewAvgPercentage = (TextView)convertView.findViewById(R.id.viewAvgPercentage);
        //TextView viewTotalExam = (TextView)convertView.findViewById(R.id.viewTotalExam);

        //get obj
        Leaderboard leader = getItem(position);


        if (leader != null) {

            int p = position;

            TextDrawable drawable4 = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.parseColor("#ffffff"))
                    .useFont(Typeface.DEFAULT)
                    .endConfig()
                    .buildRound((p+1)+"", Color.parseColor("#05c1ff"));

            viewRank.setImageDrawable(drawable4);


            viewSName.setText(leader.getStudentName());
            examName.setText(leader.getExamName());
            percentage.setText(leader.getResultPercent()+"%");
            groupName.setText(leader.getGroup());

            //load profile image
            Glide.with(context)
                    .load(leader.getStudentPhoto())
                    // .placeholder(R.raw.default_profile)
                    .into(studentImage);

            if (position == 0) {

                trophy.setImageResource(R.drawable.trophy1);

            } else if (position == 1) {

                trophy.setImageResource(R.drawable.trophy2);

            } else if (position == 2) {

                trophy.setImageResource(R.drawable.trophy3);

            } else {

                trophy.setImageResource(R.drawable.trophy4);
            }

        }
            return convertView;
        }
    }

