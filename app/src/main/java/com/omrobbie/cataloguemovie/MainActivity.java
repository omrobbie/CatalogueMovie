package com.omrobbie.cataloguemovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.omrobbie.cataloguemovie.mvp.MainPresenter;
import com.omrobbie.cataloguemovie.mvp.MainView;

public class MainActivity extends AppCompatActivity implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainPresenter presenter = new MainPresenter(this);
    }
}
