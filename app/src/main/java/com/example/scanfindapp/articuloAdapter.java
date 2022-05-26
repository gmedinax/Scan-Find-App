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

public class articuloAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;
        private Activity mActivity;
        private ArrayList<articulo> mContentList;


    public articuloAdapter(Context mContext, Activity mActivity, ArrayList<articulo> mContentList) {
            this.mContext = mContext;
            this.mActivity = mActivity;
            this.mContentList = mContentList;
        }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articulo, parent, false);
        return new ViewHolder(view, viewType);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardArticulo;
        private ImageView imagenArticulo;
        private TextView nombreArticulo;
        private TextView descripcionArticulo;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            // Find all views ids
            cardArticulo = (CardView) itemView.findViewById(R.id.cardarticulo);
            imagenArticulo = (ImageView) itemView.findViewById(R.id.ivArticulo);
            nombreArticulo = (TextView) itemView.findViewById(R.id.txtarticulo);
            descripcionArticulo = (TextView) itemView.findViewById(R.id.txtdescripcion);
        }
    }
    @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mainHolder, int position) {
            ViewHolder holder = (ViewHolder) mainHolder;
            final articulo model = mContentList.get(position);
            // setting data over views
            String imgUrl = model.getImagen();
            if (imgUrl != null && !imgUrl.isEmpty()) {
                Glide.with(mContext)
                        .load(imgUrl)
                        .into(holder.imagenArticulo);
            }
            holder.nombreArticulo.setText(model.getNombre());
            holder.descripcionArticulo.setText(model.getDescripcion());
    }
    @Override
    public int getItemCount() {
        return mContentList.size();
    }
}