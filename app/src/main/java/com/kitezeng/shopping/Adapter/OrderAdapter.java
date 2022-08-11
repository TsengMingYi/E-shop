package com.kitezeng.shopping.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kitezeng.shopping.Fragment.FragmentHome;
import com.kitezeng.shopping.Fragment.FragmentShoppingCar;
import com.kitezeng.shopping.ListViewModelForOrder;
import com.kitezeng.shopping.Model.Order;
import com.kitezeng.shopping.Model.OrderItem;
import com.kitezeng.shopping.Model.Product;
import com.kitezeng.shopping.R;
import com.kitezeng.shopping.apiHelper.ApiHelper;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();
    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private FragmentShoppingCar fragment;

    public OrderAdapter(FragmentShoppingCar fragment) {
        this.fragment = fragment;
    }

    public OrderAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderItem orderItem = orderItemArrayList.get(position);
        Glide.with(holder.itemView.getContext()).load(orderItem.getImageUrl()).centerCrop().into(holder.order_product_image);
        holder.order_product_name.setText(orderItem.getProductName());
        holder.order_product_amount.setText(orderItem.getAmount()+"");
        holder.order_product_stock.setText(orderItem.getQuantity()+"");
        holder.order_user_id.setText(orderItem.getUserId()+"");
        holder.delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiHelper.deleteHttpData("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/orders/" + orderItem.getOrderId(), new ApiHelper.Callback2() {
                    @Override
                    public void success() {
                        Toast.makeText(fragment.getContext(), "成功", Toast.LENGTH_LONG).show();
                        orderItemArrayList.remove(orderItem);
                        fragment.hello2(orderItemArrayList);
                    }

                    @Override
                    public void fail(Exception exception) {
//                        Toast.makeText(getContext(), "失敗", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public void refreshUI(ArrayList<OrderItem> orderItemArrayList){
        this.orderItemArrayList = orderItemArrayList;
        notifyDataSetChanged();
    }

    public void clearUI(){
        orderItemArrayList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView order_product_image;
        TextView order_product_name;
        TextView order_product_amount;
        TextView order_product_stock;
        TextView order_user_id;
        Button delete_order;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_product_image = itemView.findViewById(R.id.order_product_image);
            order_product_name = itemView.findViewById(R.id.order_product_name);
            order_product_amount = itemView.findViewById(R.id.order_product_amount);
            order_product_stock = itemView.findViewById(R.id.order_product_stock);
            delete_order = itemView.findViewById(R.id.delete_order);
            order_user_id = itemView.findViewById(R.id.order_user_id);
        }
    }
}
