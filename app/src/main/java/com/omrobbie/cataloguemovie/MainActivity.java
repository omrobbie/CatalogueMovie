package com.omrobbie.cataloguemovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.omrobbie.cataloguemovie.adapter.SearchAdapter;
import com.omrobbie.cataloguemovie.api.APIClient;
import com.omrobbie.cataloguemovie.mvp.MainPresenter;
import com.omrobbie.cataloguemovie.mvp.MainView;
import com.omrobbie.cataloguemovie.mvp.model.search.ResultsItem;
import com.omrobbie.cataloguemovie.mvp.model.search.SearchModel;
import com.omrobbie.cataloguemovie.utils.AlarmReceiver;
import com.omrobbie.cataloguemovie.utils.DateTime;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.format;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity
        implements MainView,
        MaterialSearchBar.OnSearchActionListener,
        SwipeRefreshLayout.OnRefreshListener,
        PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;

    @BindView(R.id.search_bar)
    MaterialSearchBar search_bar;

    @BindView(R.id.rv_movielist)
    RecyclerView rv_movielist;

    private SearchAdapter adapter;
    private List<ResultsItem> list = new ArrayList<>();

    private Call<SearchModel> apiCall;
    private APIClient apiClient = new APIClient();

    private String movie_title = "";
    private int currentPage = 1;
    private int totalPages = 1;

    private AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmReceiver.setRepeatingAlarm(this, alarmReceiver.TYPE_REPEATING, "07:00", "Good morning! Ready to pick your new movies today?");

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        search_bar.setOnSearchActionListener(this);
        swipe_refresh.setOnRefreshListener(this);

        search_bar.inflateMenu(R.menu.main);
        search_bar.getMenu().setOnMenuItemClickListener(this);

        MainPresenter presenter = new MainPresenter(this);

        setupList();
        setupListScrollListener();
        startRefreshing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apiCall != null) apiCall.cancel();
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
        movie_title = String.valueOf(text);
        onRefresh();
    }

    /**
     * Invoked when "speech" or "navigation" buttons clicked.
     *
     * @param buttonCode {@link #BUTTON_NAVIGATION} or {@link #BUTTON_SPEECH} will be passed
     */
    @Override
    public void onButtonClicked(int buttonCode) {

    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        currentPage = 1;
        totalPages = 1;

        stopRefrehing();
        startRefreshing();
    }

    /**
     * This method will be invoked when a menu item is clicked if the item
     * itself did not already handle the event.
     *
     * @param item the menu item that was clicked
     * @return {@code true} if the event was handled, {@code false}
     * otherwise
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_refresh:
                onRefresh();
                break;
        }

        return false;
    }

    private void setupList() {
        adapter = new SearchAdapter();
        rv_movielist.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        rv_movielist.setLayoutManager(new LinearLayoutManager(this));
        rv_movielist.setAdapter(adapter);
    }

    private void setupListScrollListener() {
        rv_movielist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int totalItems = layoutManager.getItemCount();
                int visibleItems = layoutManager.getChildCount();
                int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();

                if (pastVisibleItems + visibleItems >= totalItems) {
                    if (currentPage < totalPages) currentPage++;
                    startRefreshing();
                }
            }
        });
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

    private void loadData(final String movie_title) {
        getSupportActionBar().setSubtitle("");

        if (movie_title.isEmpty()) apiCall = apiClient.getService().getPopularMovie(currentPage);
        else apiCall = apiClient.getService().getSearchMovie(currentPage, movie_title);

        apiCall.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if (response.isSuccessful()) {
                    totalPages = response.body().getTotalPages();
                    List<ResultsItem> items = response.body().getResults();
                    showResults(response.body().getTotalResults());

                    if (currentPage > 1) adapter.updateData(items);
                    else adapter.replaceAll(items);

                    stopRefrehing();
                } else loadFailed();
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                loadFailed();
            }
        });
    }

    private void loadFailed() {
        stopRefrehing();
        Toast.makeText(MainActivity.this, "Failed to load data.\nPlease check your Internet connections!", Toast.LENGTH_SHORT).show();
    }

    private void startRefreshing() {
        if (swipe_refresh.isRefreshing()) return;
        swipe_refresh.setRefreshing(true);

        loadData(movie_title);
    }

    private void stopRefrehing() {
        if (swipe_refresh.isRefreshing()) swipe_refresh.setRefreshing(false);
    }

    private void showResults(int totalResults) {
        String results;

        String formatResults = NumberFormat.getIntegerInstance().format(totalResults);

        if (totalResults > 0) {
            results = "I found " + formatResults + " movie" + (totalResults > 1 ? "s" : "") + " for you :)";
        } else results = "Sorry! I can't find " + movie_title + " everywhere :(";

        getSupportActionBar().setSubtitle(results);
    }
}
