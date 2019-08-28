package com.guestlogix.views.fragments;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.guestlogix.R;
import com.guestlogix.views.activities.MainActivity;
import com.guestlogix.views.adapters.EpisodesAdapter;
import com.guestlogix.controllers.EpisodesController;
import com.guestlogix.listeners.IClickListener;
import com.guestlogix.listeners.IParserListener;
import com.guestlogix.models.EpisodeModel;
import com.guestlogix.utils.Constants;
import com.guestlogix.utils.StaticUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EpisodesFragment extends BaseFragment implements IParserListener, IClickListener {
    private View rootView;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private ProgressBar progressBar, progressBarPagination;
    private AppCompatTextView txtNoData;
    private ArrayList<EpisodeModel> episodeModelArrayList;
    private RelativeLayout relBody;
    private SwipeRefreshLayout swipeToRefresh;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    private EpisodesAdapter episodesAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String urlToLoad;
    private EpisodesController episodesController;

    public EpisodesFragment() {
    }

    public static EpisodesFragment newInstance() {
        return new EpisodesFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainActivity != null) mainActivity.showTopBar(getString(R.string.episodes), false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_general_list, container, false);
        initComponents();
        return rootView;
    }

    @Override
    protected void initComponents() {
        urlToLoad = Constants.WS_GET_EPISODES;
        episodeModelArrayList = new ArrayList<>();
        setReferences();
        setListeners();
        episodesController = new EpisodesController(this);
        requestForEpisodesAPI();
    }

    private void requestForEpisodesAPI() {
        if (StaticUtils.checkInternetConnection(mainActivity)) {
            episodesController.fetchEpisodesFromServer(urlToLoad);
        } else noInternetConnection(Constants.WS_REQUEST_GET_EPISODES);
    }

    private void setListeners() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            progressBarPagination.setVisibility(View.VISIBLE);
                            requestForEpisodesAPI();
                        }
                    }
                }
            }
        });

        swipeToRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivity, R.color.colorRed), ContextCompat.getColor(mainActivity, R.color.colorYellow), ContextCompat.getColor(mainActivity, R.color.colorGreen));
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                episodeModelArrayList.clear();
                urlToLoad = Constants.WS_GET_EPISODES;
                requestForEpisodesAPI();
            }
        });
    }

    private void setReferences() {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBarPagination = rootView.findViewById(R.id.progressBarPagination);
        txtNoData = rootView.findViewById(R.id.txtNoData);
        swipeToRefresh = rootView.findViewById(R.id.swipeToRefresh);
        relBody = rootView.findViewById(R.id.relBody);
    }

    private void parseGetEpisodeWSResponse(JSONObject jsonObject) {
        try {
            JSONObject info = jsonObject.getJSONObject("info");
            String next = info.getString("next");
            progressBarPagination.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(next)) {
                loading = true;
                urlToLoad = next;
            } else {
                loading = false;
            }

            JSONArray resultsArray = jsonObject.getJSONArray("results");
            if (resultsArray.length() > 0) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    episodeModelArrayList.add(new EpisodeModel(resultsArray.getJSONObject(i)));
                }
                setEpisodesAdapter();

                progressBar.setVisibility(View.GONE);
                relBody.setVisibility(View.VISIBLE);
                txtNoData.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                relBody.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (swipeToRefresh.isRefreshing()) swipeToRefresh.setRefreshing(false);
        }
    }

    private void setEpisodesAdapter() {
        linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (recyclerView.getAdapter() == null) {
            episodesAdapter = new EpisodesAdapter(mainActivity, episodeModelArrayList, this);
            recyclerView.setAdapter(episodesAdapter);
        } else {
            episodesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void successResponse(int requestCode, Object response) {
        try {
            String data = (String) response;
            if (requestCode == Constants.WS_REQUEST_GET_EPISODES) {
                parseGetEpisodeWSResponse(new JSONObject(data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorResponse(int requestCode, final String error) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                relBody.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
                if (swipeToRefresh.isRefreshing()) swipeToRefresh.setRefreshing(false);
                StaticUtils.showToast(mainActivity, TextUtils.isEmpty(error) ? "Something went wrong. Please try again later." : error);
            }
        });
    }

    @Override
    public void noInternetConnection(int requestCode) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                relBody.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
                if (swipeToRefresh.isRefreshing()) swipeToRefresh.setRefreshing(false);
                StaticUtils.showIndefiniteToast(rootView, getString(R.string.no_internet_connection_please_try_later), getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestForEpisodesAPI();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
//        switch (view.getId()){}
        EpisodeModel episodeModel = episodeModelArrayList.get(position);
        mainActivity.addFragment(CharactersFragment.newInstance(episodeModel.name, episodeModel.charactersArrayList), true);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

}
