package com.kitezeng.shopping.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kitezeng.shopping.MainActivity2;
import com.kitezeng.shopping.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder>{

    private Context context;
    private List<Integer> list;


    public BannerAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position%list.size())).centerCrop().into(holder.imageView);
//        imageArrayList = initial(imageArrayList);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity2.class);
                intent.putExtra("Image",list.get(position%list.size()));
                Log.e("position:",position+" ");
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
//    private List<Image> initial(List<Image> imageArrayList){
//        imageArrayList.add(new Image(R.drawable.picture1,"第一張風景"));
//        imageArrayList.add(new Image(R.drawable.picture2,"第二張風景"));
//        imageArrayList.add(new Image(R.drawable.picture3,"第三張風景"));
//        imageArrayList.add(new Image(R.drawable.picture4,"第四張風景"));
//        return imageArrayList;
//    }
}
