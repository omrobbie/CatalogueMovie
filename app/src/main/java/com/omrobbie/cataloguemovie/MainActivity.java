package com.omrobbie.cataloguemovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.omrobbie.cataloguemovie.adapter.SearchAdapter;
import com.omrobbie.cataloguemovie.mvp.MainPresenter;
import com.omrobbie.cataloguemovie.mvp.MainView;
import com.omrobbie.cataloguemovie.mvp.model.search.ResultsItem;
import com.omrobbie.cataloguemovie.utils.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.*;

public class MainActivity extends AppCompatActivity implements MainView, MaterialSearchBar.OnSearchActionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.searchBar)
    MaterialSearchBar searchBar;

    @BindView(R.id.rv_movielist)
    RecyclerView rv_movielist;

    private SearchAdapter adapter;
    private List<ResultsItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        searchBar.setOnSearchActionListener(this);

        MainPresenter presenter = new MainPresenter(this);

        setupList();
        loadDummyData();
    }

    /**
     * Invoked when SearchBar opened or closed
     *
     * @param enabled
     */
    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    /**
     * Invoked when search confirmed and "search" button is clicked on the soft keyboard
     *
     * @param text search input
     */
    @Override
    public void onSearchConfirmed(CharSequence text) {
        Toast.makeText(this, "Searching: " + text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Invoked when "speech" or "navigation" buttons clicked.
     *
     * @param buttonCode {@link #BUTTON_NAVIGATION} or {@link #BUTTON_SPEECH} will be passed
     */
    @Override
    public void onButtonClicked(int buttonCode) {

    }

    private void setupList() {
        adapter = new SearchAdapter();
        rv_movielist.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        rv_movielist.setLayoutManager(new LinearLayoutManager(this));
        rv_movielist.setAdapter(adapter);
    }

    private void loadDummyData() {
        list.clear();
        for (int i = 0; i <= 10; i++) {
            ResultsItem item = new ResultsItem();
            item.setPosterPath("/vSNxAJTlD0r02V9sPYpOjqDZXUK.jpg");
            item.setTitle("This is very very very long movie title that you can read " + i);
            item.setOverview("This is very very very long movie overview that you can read " + i);
            item.setReleaseDate(DateTime.getLongDate("2016-04-1" + i));
            list.add(item);
        }
        adapter.replaceAll(list);
    }
}
