package com.kitezeng.shopping.Manager;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kitezeng.shopping.Adapter.BannerAdapter;
import com.kitezeng.shopping.Adapter.PageAdapter;
import com.kitezeng.shopping.BannerIndicator;
import com.kitezeng.shopping.Callback3;
import com.kitezeng.shopping.Model.Page;
import com.kitezeng.shopping.Model.Product;
import com.kitezeng.shopping.R;
import com.kitezeng.shopping.apiHelper.ApiHelper;
import com.kitezeng.shopping.util.GridSpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeManager {

    private static HomeManager instance;
//    private String apiUrl = "http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products";
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private Page<Product>  productPage = new Page<>();


    public static HomeManager getInstance(){
        if(instance == null){
            instance = new HomeManager();
        }
        return instance;
    }

    private HomeManager(){}

    public ArrayList<Product> getProductArrayList(){
        return productArrayList;
    }

    public Page<Product> getProductPage(){
        return productPage;
    }

    public void homeView(List<Integer> list , Context context , RecyclerView recyclerView , BannerIndicator bannerIndicator){
        list.add(R.drawable.picture1);
        list.add(R.drawable.picture2);
        list.add(R.drawable.picture3);
        list.add(R.drawable.picture4);

        BannerAdapter adapter = new BannerAdapter(context, list);

        final SmoothLinearLayoutManager layoutManager = new SmoothLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(list.size()*10);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        bannerIndicator.setNumber(list.size());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int i = layoutManager.findFirstVisibleItemPosition() % list.size();
                    //得到指示器紅點的位置
                    bannerIndicator.setPosition(i);
                }
            }
        });

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
            }
        }, 3000, 3000, TimeUnit.MILLISECONDS);
    }

    public void syncDataFromRemote(String apiUrl,Runnable doSomething){
        ApiHelper.requestApi(apiUrl, null, new ApiHelper.Callback() {
            @Override
            public void success(String rawString) {
                HomeManager.getInstance().loadDataFromRawData(rawString);
                if (doSomething != null) {
                    doSomething.run();
                }
            }

            @Override
            public void fail(Exception exception) {

            }
        });
    }

    public void syncDataFromRemote1(String apiUrl, Callback3 callback3){
        ApiHelper.requestApi(apiUrl, null, new ApiHelper.Callback() {
            @Override
            public void success(String rawString) {
                HomeManager.getInstance().loadDataFromRawData(rawString);
                callback3.success();
            }

            @Override
            public void fail(Exception exception) {
                callback3.fail(exception);
            }
        });
    }

//    public void postDataFromRemote(String apiUrl,Product product,Runnable doSomething){
//        ApiHelper.postRequestApi(apiUrl,product, new ApiHelper.Callback2() {
//            @Override
//            public void success() {
//                if (doSomething != null) {
//                    doSomething.run();
//                }
//            }
//
//            @Override
//            public void fail(Exception exception) {
//
//            }
//        });
//    }

    public void loadDataFromRawData(String rawData){
        Log.e("response",rawData);
//        productArrayList.clear();
        try{
            productArrayList = new ArrayList<>();
            productPage = new Page<>();
            JSONObject jsonObject = new JSONObject(rawData);
            Integer limit = jsonObject.getInt("limit");
            productPage.setLimit(limit);
            Integer offset = jsonObject.getInt("offset");
            productPage.setOffset(offset);
            Integer total = jsonObject.getInt("total");
            productPage.setTotal(total);
            JSONArray results = jsonObject.getJSONArray("results");
            for(int i = 0;i<results.length();i++){
                Product product = new Product();
                JSONObject jsonObject1 = results.getJSONObject(i);
                product.setProductId(jsonObject1.getInt("productId"));
                product.setProductName(jsonObject1.getString("productName"));
                product.setCategory(jsonObject1.getString("category"));
                product.setImageUrl(jsonObject1.getString("imageUrl"));
                product.setPrice(jsonObject1.getInt("price"));
                product.setStock(jsonObject1.getInt("stock"));
                if(jsonObject1.getString("description") != null){
                    product.setDescription(jsonObject1.getString("description"));
                }else{
                    product.setDescription(" ");
                }
                product.setCreateDate(jsonObject1.getString("createdDate"));
                product.setLastModifiedDate(jsonObject1.getString("lastModifiedDate"));
                productArrayList.add(product);
            }
            productPage.setResults(productArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public void pictureView(RecyclerView recyclerView , Context context,PageAdapter pageAdapter){
        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,
                context.getResources().getDimensionPixelSize(R.dimen.recycler_view_item_width)));

    }

}


