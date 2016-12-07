package com.asobrab.thirdretrofit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.asobrab.thirdretrofit.R;
import com.asobrab.thirdretrofit.adapter.ContributorAdapter;
import com.asobrab.thirdretrofit.model.Contributor;
import com.asobrab.thirdretrofit.retrofit.GitHubService;
import com.asobrab.thirdretrofit.view.ContributorClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements SearchView.OnQueryTextListener {

    ListView listView;
    Contributor contributor;
    View view;
    ArrayList<Contributor> contributors;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        EventBus.getDefault().register(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contributor = (Contributor) listView.getItemAtPosition(position);
                if(getContext() instanceof ContributorClick){
                    ((ContributorClick)getContext()).Click(contributor);
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setAdapter(Context context, List<Contributor> contributors){
        ArrayAdapter<Contributor> adapter = new ContributorAdapter(context, contributors);
        listView.setAdapter(adapter);
    }

    public void callService(String sProjeto){
        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        final Call<List<Contributor>> call =
                gitHubService.repoContributors("square", sProjeto);

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                contributors = (ArrayList<Contributor>) response.body();
                EventBus.getDefault().postSticky(contributors);
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                //Toast.makeText(MainActivity.class, "Something went wrong:", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        callService(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Subscribe(sticky = true ,threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<Contributor> contributors) {setAdapter(getActivity(), contributors);}
}
