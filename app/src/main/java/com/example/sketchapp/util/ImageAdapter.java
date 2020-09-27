package com.example.sketchapp.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sketchapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHOlder> {

    private Context context;
    private List<ModelImage> imageList;
    public ImageAdapter(Context context, List<ModelImage> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_images,parent,false);
        return new ImageViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHOlder holder, int position) {

            //  Glide.with(context).load(imageList.get(position).getImageurl()).override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).into(holder.imageView);
             // Glide.with(context).load(imageList.get(position).getImageurl()).into(holder.imageView);
              Log.d("url",imageList.get(position).getImageurl());
              String url = imageList.get(position).getImageurl();
              Log.d("ur",url);
            Picasso.get().load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
    public static class ImageViewHOlder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ImageViewHOlder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
