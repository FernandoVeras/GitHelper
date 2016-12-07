package com.asobrab.thirdretrofit.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asobrab.thirdretrofit.R;
import com.asobrab.thirdretrofit.model.Contributor;
import com.asobrab.thirdretrofit.retrofit.GitHubServiceUsers;
import com.asobrab.thirdretrofit.sql.DbEvent;
import com.asobrab.thirdretrofit.sql.RepositorioContributor;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.asobrab.thirdretrofit.R.id.fab;

/**
 * A simple {@link Fragment} subclas    s.
 */
public class DetailFragment extends Fragment {

    Contributor contributor;
    TextView mName, mCompany, mBlog, mLocation, mEmail;
    ImageView mAvatar;
    FloatingActionButton mFButton;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ProgressDialog progress;

    public static DetailFragment newInstace(Contributor contributor){
        Bundle bundle = new Bundle();
        bundle.putParcelable("contributor", Parcels.wrap(contributor));
        DetailFragment fd = new DetailFragment();
        fd.setArguments(bundle);
        return fd;
    }

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_detail, container, false);

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Carregando os detalhes do seu amado!");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        /****************************************************************************/
        mCompany = (TextView) view.findViewById(R.id.detail_company_name);
        mBlog = (TextView) view.findViewById(R.id.detail_blog);
        mLocation = (TextView) view.findViewById(R.id.detail_location);
        mEmail = (TextView) view.findViewById(R.id.detail_email);
        mAvatar = (ImageView) view.findViewById(R.id.detail_avatar);
        mFButton = (FloatingActionButton) view.findViewById(fab);
        /****************************************************************************/

        contributor =  Parcels.unwrap(getArguments().getParcelable("contributor"));

        if (contributor.getName() == null) {
            GitHubServiceUsers gitHubServiceUsers = GitHubServiceUsers.retrofit.create(GitHubServiceUsers.class);
            final Call<Contributor> call = gitHubServiceUsers.repoDetailContributors(contributor.getLogin());
            call.enqueue(new Callback<Contributor>() {
                @Override
                public void onResponse(Call<Contributor> call, Response<Contributor> response) {
                    Picasso.with(getActivity()).load(contributor.getAvatar_url()).into(mAvatar);
                    contributor = response.body();
                    mCompany.setText(contributor.getCompany());
                    mBlog.setText(contributor.getBlog());
                    mLocation.setText(contributor.getLocation());
                    mEmail.setText(contributor.getEmail());

                    if(getResources().getBoolean(R.bool.phone)) {
                        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
                        collapsingToolbarLayout.setTitle(contributor.getName());
                    }

                    RepositorioContributor rc = new RepositorioContributor(getActivity());
                    Contributor tmpContributor = rc.selecionaEscalar(contributor.getLogin());
                    if (contributor.getLogin().equals(tmpContributor.getLogin())) {
                        changeFloatButton(true);
                    } else {
                        changeFloatButton(false);
                    }

                    progress.hide();
                }

                @Override
                public void onFailure(Call<Contributor> call, Throwable t) {
                    progress.hide();
                }
            });
        }else{
            Picasso.with(getActivity()).load(contributor.getAvatar_url()).into(mAvatar);
            mCompany.setText(contributor.getCompany());
            mBlog.setText(contributor.getBlog());
            mLocation.setText(contributor.getLocation());
            mEmail.setText(contributor.getEmail());

            if(getResources().getBoolean(R.bool.phone)) {
                collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
                collapsingToolbarLayout.setTitle(contributor.getName());
            }

            RepositorioContributor rc = new RepositorioContributor(getActivity());
            Contributor tmpContributor = rc.selecionaEscalar(contributor.getLogin());
            if (contributor.getLogin().equals(tmpContributor.getLogin())) {
                changeFloatButton(true);
            } else {
                changeFloatButton(false);
            }

            progress.hide();

        }

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvaOuDeletaContributor();
                Snackbar.make(view, "Alterado com sucesso!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }

    public void changeFloatButton(boolean favor){
        int resource = favor ? R.drawable.ic_close_black_24dp : R.drawable.ic_favorite_border_black_24dp;
        mFButton.setImageResource(resource);
    }

    public void salvaOuDeletaContributor(){
        RepositorioContributor rc = new RepositorioContributor(getActivity());
        Contributor tmpContributor = rc.selecionaEscalar(contributor.getLogin());

        if(tmpContributor.getId() != 0){
            rc.deletaContributor(contributor.getLogin());
            changeFloatButton(false);
        }else{
            rc.inserir(contributor);
            changeFloatButton(true);
        }
        org.greenrobot.eventbus.EventBus.getDefault().post(new DbEvent());
    }
}
