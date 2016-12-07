package com.asobrab.thirdretrofit.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.asobrab.thirdretrofit.R;
import com.asobrab.thirdretrofit.adapter.ContributorAdapter;
import com.asobrab.thirdretrofit.model.Contributor;
import com.asobrab.thirdretrofit.sql.DbEvent;
import com.asobrab.thirdretrofit.sql.RepositorioContributor;
import com.asobrab.thirdretrofit.view.ContributorClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    View view;
    ListView listView;
    List<Contributor> contributors = new ArrayList<>();
    Contributor contributor;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_favorite);
        atualizaActvty();
        EventBus.getDefault().register(this);

        /**
         * Clique unico abre outra activity
         * **/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contributor = (Contributor) listView.getItemAtPosition(position);
                if (getContext() instanceof ContributorClick) {
                    ((ContributorClick) getContext()).Click(contributor);
                }
            }

            /*
            public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                contributor = (Contributor) listView.getItemAtPosition(position);
                RepositorioContributor rc = new RepositorioContributor(getActivity());
                rc.deletaContributor(contributor.getLogin());
                atualizaActvty();
            }
            */
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void atualizaActvty(){
        contributors = new RepositorioContributor(getActivity()).listaContributors();
        setAdapter(getActivity(), contributors);
    }

    public void setAdapter(Context context, List<Contributor> contributors){
        ArrayAdapter<Contributor> adapter = new ContributorAdapter(context, contributors);
        listView.setAdapter(adapter);
    }

    @Subscribe(sticky = true ,threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DbEvent event) {atualizaActvty();}
}
