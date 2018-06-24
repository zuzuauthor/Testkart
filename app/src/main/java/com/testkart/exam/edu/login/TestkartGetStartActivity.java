package com.testkart.exam.edu.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.testkart.exam.R;
import com.testkart.exam.edu.SignupActivity;
import com.testkart.exam.edu.login.model.TestkartFeature;

import java.util.ArrayList;

/**
 * Created by elfemo on 24/8/17.
 */

public class TestkartGetStartActivity extends AppCompatActivity {

    private Context context = this;

    private ListView features;

    private Button loginButton, signupButton;

    private ImageView backButton;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_testkart_get_start);

        initViews();
    }

    private void initViews() {

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        backButton = (ImageView) findViewById(R.id.backButton);

        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        frameLayoutListener();

        loginButtonListener();
        signupButtonListener();
        backButtonListener();

        features = (ListView)findViewById(R.id.features);
        prepareAndLoad();

    }

    private void frameLayoutListener() {

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void prepareAndLoad() {

        ArrayList<TestkartFeature> dataSet = new ArrayList<>();

        TestkartFeature tf1 = new TestkartFeature();
        tf1.setFeature("Always stay ahead of your exam and competition with #1 Test Prep App.");
        tf1.setImageResource(R.drawable.ic_onboarding_slide00);

        TestkartFeature tf2 = new TestkartFeature();
        tf2.setFeature("Practice unlimited questions and improve your speed and accuracy.");
        tf2.setImageResource(R.drawable.ic_onboarding_slide01);


        TestkartFeature tf3 = new TestkartFeature();
        tf3.setFeature("Analyse your performance, know your strengths and weaknesses and much more.");
        tf3.setImageResource(R.drawable.ic_onboarding_slide02);


        TestkartFeature tf4 = new TestkartFeature();
        tf4.setFeature("Take exam-like mock tests on best mobile test platform.");
        tf4.setImageResource(R.drawable.ic_onboarding_slide03);


        TestkartFeature tf5 = new TestkartFeature();
        tf5.setFeature("Read and learn from latest study material - anytime, anywhere.");
        tf5.setImageResource(R.drawable.ic_onboarding_slide04);


        dataSet.add(tf1);
        dataSet.add(tf2);
        dataSet.add(tf3);
        dataSet.add(tf4);
        dataSet.add(tf5);


        TestkartFeatureAdapter adapter = new TestkartFeatureAdapter(this, dataSet);
        features.setAdapter(adapter);

    }

    private void loginButtonListener() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent loginIntent = new Intent(context, LoginActivity1.class);
                loginIntent.putExtra(LoginActivity1.KEY_RETAIN_CART, false);
                startActivity(loginIntent);
            }
        });

    }

    private void signupButtonListener() {

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signupIntent = new Intent(context, SignupActivity.class);
                startActivity(signupIntent);

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
