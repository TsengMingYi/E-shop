package com.kitezeng.shopping.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.kitezeng.shopping.Adapter.PageAdapter;
import com.kitezeng.shopping.BannerIndicator;
import com.kitezeng.shopping.Login;
import com.kitezeng.shopping.Manager.HomeManager;
import com.kitezeng.shopping.Model.Page;
import com.kitezeng.shopping.Model.Product;
import com.kitezeng.shopping.R;
import com.kitezeng.shopping.Search;
import com.kitezeng.shopping.util.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Integer> list = new ArrayList<>(4);
    //    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private RecyclerView pictureRecycleView;
    private BannerIndicator bannerIndicator;
    private ImageView member_card;
    private EditText edit_search;
    private String host = "http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products";
    private String limit = "?limit=5";
    private String offset = "&offset=";
//    private String apiUrl = host + limit + offset;
    private View backTop;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ArrayList<Product> productArrayListTotal = new ArrayList<>();
    private Page<Product> productPage = new Page<>();
    private PageAdapter pageAdapter;
    private int count = 5;
    private Handler handler = new Handler();
    private NestedScrollView nestedScrollView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.e("testHome", "testHome");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        bannerIndicator = view.findViewById(R.id.indicator);
        recyclerView = view.findViewById(R.id.recycleView);
        pictureRecycleView = view.findViewById(R.id.pictureRecycleView);
        edit_search = view.findViewById(R.id.edit_search);
        backTop = view.findViewById(R.id.back_top);
        nestedScrollView = view.findViewById(R.id.nested_scrollview);
        member_card = view.findViewById(R.id.member_card);


        HomeManager.getInstance().homeView(list, getContext(), recyclerView, bannerIndicator);

        setEdit_search();

        setNestedScrollViewToTop();

        set_member_card();


        pageAdapter = new PageAdapter(productArrayList);
        HomeManager.getInstance().pictureView(pictureRecycleView, getContext(), pageAdapter);
        pictureRecycleView.setAdapter(pageAdapter);

        HomeManager.getInstance().syncDataFromRemote("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", new Runnable() {
            @Override
            public void run() {
                productArrayList = HomeManager.getInstance().getProductArrayList();
                productArrayListTotal.addAll(productArrayList);
                pageAdapter.refreshUI(productArrayList);
                productPage = HomeManager.getInstance().getProductPage();
            }
        });

        pictureRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                Log.e("length3", productArrayList.size() + "");
                if (productArrayList.size() == count) {
                    pageAdapter.setLoadState(pageAdapter.LOADING);
                    HomeManager.getInstance().syncDataFromRemote("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products?limit=5&offset="+count, new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Product> productArrayList1 = HomeManager.getInstance().getProductArrayList();

                            productArrayList.addAll(productArrayList1);
                            productArrayListTotal.addAll(productArrayList1);

                            Log.e("length1", productArrayList.size() + "");
                            Log.e("length2", productArrayList1.size() + "");

                            pageAdapter.refreshUI(productArrayList);
                            pageAdapter.setLoadState(pageAdapter.LOADING_COMPLETE);
                            count+=5;

                        }
                    });
                }
                else {
                    // 显示加载到底的提示
                    pageAdapter.setLoadState(pageAdapter.LOADING_END);
                }
            }
        });
        return view;

    }

    private void setEdit_search() {

        edit_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FragmentHome.this.getActivity(), Search.class));
            }
        });
    }

    private void setNestedScrollViewToTop() {

        backTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("nihao", "nihao");
                nestedScrollView.fullScroll(View.FOCUS_UP);
            }
        });
    }

    private void set_member_card(){
        member_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FragmentHome.this.getActivity(), Login.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();



    }
}