package com.omrobbie.cataloguemovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.omrobbie.cataloguemovie.api.APIClient;
import com.omrobbie.cataloguemovie.mvp.model.detail.DetailModel;
import com.omrobbie.cataloguemovie.utils.DateTime;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_ID = "movie_id";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_backdrop)
    ImageView img_backdrop;

    @BindView(R.id.img_poster)
    ImageView img_poster;

    @BindView(R.id.tv_release_date)
    TextView tv_release_date;

    @BindView(R.id.tv_vote)
    TextView tv_vote;

    @BindViews({
            R.id.img_star1,
            R.id.img_star2,
            R.id.img_star3,
            R.id.img_star4,
            R.id.img_star5
    })
    List<ImageView> img_vote;

    @BindView(R.id.tv_genres)
    TextView tv_genres;

    @BindView(R.id.tv_overview)
    TextView tv_overview;

    @BindView(R.id.img_poster_belongs)
    ImageView img_poster_belongs;

    @BindView(R.id.tv_title_belongs)
    TextView tv_title_belongs;

    @BindView(R.id.tv_budget)
    TextView tv_budget;

    @BindView(R.id.tv_revenue)
    TextView tv_revenue;

    @BindView(R.id.tv_companies)
    TextView tv_companies;

    @BindView(R.id.tv_countries)
    TextView tv_countries;

    private Call<DetailModel> apiCall;
    private APIClient apiClient = new APIClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String movie_id = getIntent().getStringExtra(MOVIE_ID);
        loadData(movie_id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apiCall != null) apiCall.cancel();
    }

    private void loadData(String movie_id) {
        apiCall = apiClient.getService().getDetailMovie(movie_id);
        apiCall.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                if (response.isSuccessful()) {
                    DetailModel item = response.body();

                    Glide.with(DetailActivity.this)
                            .load(BuildConfig.BASE_URL_IMG + "w185" + item.getBackdropPath())
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.placeholder)
                                    .centerCrop()
                            )
                            .into(img_backdrop);

                    Glide.with(DetailActivity.this)
                            .load(BuildConfig.BASE_URL_IMG + "w154" + item.getPosterPath())
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.placeholder)
                                    .centerCrop()
                            )
                            .into(img_poster);

                    tv_release_date.setText(DateTime.getLongDate(item.getReleaseDate()));
                    tv_vote.setText(String.valueOf(item.getVoteAverage()));

                    int size = 0;

                    String genres = "";
                    size = item.getGenres().size();
                    for (int i = 0; i < size; i++) {
                        genres += "√ " + item.getGenres().get(i).getName() + (i + 1 < size ? "\n" : "");
                    }
                    tv_genres.setText(genres);

                    tv_overview.setText(item.getOverview());

                    if (item.getBelongsToCollection() != null) {
                        Glide.with(DetailActivity.this)
                                .load(BuildConfig.BASE_URL_IMG + "w92" + item.getBelongsToCollection().getPosterPath())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.placeholder)
                                        .centerCrop()
                                )
                                .into(img_poster_belongs);

                        tv_title_belongs.setText(item.getBelongsToCollection().getName());
                    }

                    tv_budget.setText("$ " + NumberFormat.getIntegerInstance().format(item.getBudget()));
                    tv_revenue.setText("$ " + NumberFormat.getIntegerInstance().format(item.getRevenue()));

                    String companies = "";
                    size = item.getProductionCompanies().size();
                    for (int i = 0; i < size; i++) {
                        companies += "√ " + item.getProductionCompanies().get(i).getName() + (i + 1 < size ? "\n" : "");
                    }
                    tv_companies.setText(companies);

                    String countries = "";
                    size = item.getProductionCountries().size();
                    for (int i = 0; i < size; i++) {
                        countries += "√ " + item.getProductionCountries().get(i).getName() + (i + 1 < size ? "\n" : "");
                    }
                    tv_countries.setText(countries);

                    double userRating = item.getVoteAverage() / 2;
                    int integerPart = (int) userRating;

                    // Fill stars
                    for (int i = 0; i < integerPart; i++) {
                        img_vote.get(i).setImageResource(R.drawable.ic_star_black_24dp);
                    }

                    // Fill half star
                    if (Math.round(userRating) > integerPart) {
                        img_vote.get(integerPart).setImageResource(R.drawable.ic_star_half_black_24dp);
                    }
                } else loadFailed();
            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {
                loadFailed();
            }
        });
    }

    private void loadFailed() {
        Toast.makeText(DetailActivity.this, "Cannot fetch detail movie.\nPlease check your Internet connections!", Toast.LENGTH_SHORT).show();
    }
}
