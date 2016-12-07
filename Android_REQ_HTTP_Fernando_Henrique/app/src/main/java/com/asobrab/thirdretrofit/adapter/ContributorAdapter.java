package com.asobrab.thirdretrofit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asobrab.thirdretrofit.R;
import com.asobrab.thirdretrofit.model.Contributor;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by asobrab on 27/10/16.
 */

public class ContributorAdapter extends ArrayAdapter<Contributor>  {
    Contributor contributor;
    ViewHolder vh;
    ListView listv;
    Context mContext;

    public ContributorAdapter(Context context, List<Contributor> objects){
        super(context, 0, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         contributor = getItem(position);

        if (convertView != null){
            vh = (ViewHolder)convertView.getTag();
        }
        else
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contributor, parent, false);
            vh = new ViewHolder();
            vh.txtContributions = (TextView)convertView.findViewById(R.id.contributor_contributions);
            vh.txtId = (TextView)convertView.findViewById(R.id.contributor_id);
            vh.txtLogin = (TextView)convertView.findViewById(R.id.contributor_login);
            vh.imageView = (ImageView)convertView.findViewById(R.id.contributor_avatar);
            convertView.setTag(vh);
        }

        vh.txtContributions.setText(Integer.toString(contributor.getContributions()));
        vh.txtId.setText(Integer.toString(contributor.getId()));
        vh.txtLogin.setText(contributor.getLogin());
        Picasso.with(getContext()).load(contributor.getAvatar_url()).into(vh.imageView);
        return convertView;
    }

    static class ViewHolder {
        TextView txtContributions;
        TextView txtId;
        TextView txtLogin;
        ImageView imageView;
    }
}
