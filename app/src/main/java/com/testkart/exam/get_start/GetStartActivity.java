package com.testkart.exam.get_start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.testkart.exam.R;
import com.testkart.exam.aboutus.AboutUsActivity;
import com.testkart.exam.edu.login.LoginActivity1;
import com.testkart.exam.packages.PackageListActivity;

/**
 * Created by testkart on 7/6/17.
 */

public class GetStartActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout actBtnPackage, actBtnLogin, actBtnRegister, actBtnAboutUs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_start1);

        //initialization
        initialization();
    }

    private void initialization() {

        actBtnPackage = (LinearLayout)findViewById(R.id.actBtnPackage);
        actBtnLogin = (LinearLayout)findViewById(R.id.actBtnLogin);
        actBtnRegister = (LinearLayout)findViewById(R.id.actBtnRegister);
        actBtnAboutUs= (LinearLayout)findViewById(R.id.actBtnAboutUs);

        actBtnPackage.setOnClickListener(this);
        actBtnLogin.setOnClickListener(this);
        actBtnRegister.setOnClickListener(this);
        actBtnAboutUs.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.actBtnPackage:

                Intent packageIntent = new Intent(this, PackageListActivity.class);
                startActivity(packageIntent);

                break;

            case R.id.actBtnLogin:

                Intent loginIntent = new Intent(this, LoginActivity1.class);
                loginIntent.putExtra(LoginActivity1.KEY_RETAIN_CART, false);
                startActivity(loginIntent);

                break;

            case R.id.actBtnRegister:

                Intent registerIntent = new Intent(this, com.testkart.exam.edu.SignupActivity.class);
                startActivity(registerIntent);

                break;

            case R.id.actBtnAboutUs:

                Intent aboutIntent = new Intent(this, AboutUsActivity.class);
                startActivity(aboutIntent);

                break;
        }

    }
}
