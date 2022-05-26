package com.example.scanfindapp;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class productoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private Activity mActivity;
    private ArrayList<producto> mContentList;

    public productoAdapter(Context mContext, Activity mActivity, ArrayList<producto> mContentList) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mContentList = mContentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto, parent, false);
        return new ViewHolder(view, viewType);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imgPost;
        private TextView tvTitle;
        private TextView tvDesc;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            // Find all views ids
            cardView = (CardView) itemView.findViewById(R.id.card);
            imgPost = (ImageView) itemView.findViewById(R.id.iViewimagen);
            tvTitle = (TextView) itemView.findViewById(R.id.tvnombre);
            tvDesc = (TextView) itemView.findViewById(R.id.tvdescripcion);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mainHolder, int position) {
        ViewHolder holder = (ViewHolder) mainHolder;
        final producto model = mContentList.get(position);
        // setting data over views
        String imgUrl = model.getImagArt();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .into(holder.imgPost);
        }
        holder.tvTitle.setText(model.getNombreArt());
        holder.tvDesc.setText(model.getDescArt());
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }
}