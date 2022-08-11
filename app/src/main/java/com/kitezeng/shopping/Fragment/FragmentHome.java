package com.kitezeng.shopping.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kitezeng.shopping.Adapter.OrderAdapter;
import com.kitezeng.shopping.Adapter.PageAdapter;
import com.kitezeng.shopping.BannerIndicator;
import com.kitezeng.shopping.Callback3;
import com.kitezeng.shopping.ListViewModel;
import com.kitezeng.shopping.ListViewModelForOrder;
import com.kitezeng.shopping.Login;
import com.kitezeng.shopping.Manager.HomeManager;
import com.kitezeng.shopping.Manager.OrderManager;
import com.kitezeng.shopping.Model.Order;
import com.kitezeng.shopping.Model.OrderItem;
import com.kitezeng.shopping.Model.Page;
import com.kitezeng.shopping.Model.Product;
import com.kitezeng.shopping.Model.User;
import com.kitezeng.shopping.R;
import com.kitezeng.shopping.Search;
import com.kitezeng.shopping.apiHelper.ApiHelper;
import com.kitezeng.shopping.util.EndlessRecyclerOnScrollListener;

import org.json.JSONObject;

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
    private SwipeRefreshLayout swipe_refresh;
    private ImageView member_card;
    private User user;
    private EditText edit_search;
    //    private String apiUrl = host + limit + offset;
    private View backTop;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ArrayList<Product> productArrayListTotal = new ArrayList<>();
    private Page<Product> productPage = new Page<>();
    private int userId;
    private PageAdapter pageAdapter;
    private OrderAdapter orderAdapter = new OrderAdapter();
    private int count = 5;
    private int count1 = 5;
    private ListViewModel model;
    private ListViewModelForOrder modelForOrder;
    private Handler handler = new Handler();
    private NestedScrollView nestedScrollView;
    private ActivityResultLauncher<Intent> launcher;
    private ActivityResultLauncher<Intent> launcher1;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();

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
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        modelForOrder = new ViewModelProvider(requireActivity()).get(ListViewModelForOrder.class);

        if (user != null) {
//            user = mAuth.getCurrentUser();
//            model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
            model.setUserMutableLiveData(user);
            member_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), user.getEmail() + "已經登入", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            set_member_card();
        }

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    user = (User) result.getData().getSerializableExtra("data");
                    Toast.makeText(getContext(), "登入成功", Toast.LENGTH_LONG).show();
//                    model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
                    model.setUserMutableLiveData(user);
                    userId = user.getUserId();
                    count = 5;
                    swipe_refresh.setRefreshing(true);
                    pageAdapter.clearUI();
                    HomeManager.getInstance().syncDataFromRemote1("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", new Callback3() {
                        @Override
                        public void success() {
                            productArrayList = HomeManager.getInstance().getProductArrayList();
                            for (int i = 0; i < productArrayList.size(); i++) {
                                productArrayList.get(i).setUserId(userId);
                            }
                            productArrayListTotal.addAll(productArrayList);
                            pageAdapter.refreshUI(productArrayList);
                            productPage = HomeManager.getInstance().getProductPage();
                            Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                            swipe_refresh.setRefreshing(false);
                        }

                        @Override
                        public void fail(Exception e) {
                            swipe_refresh.setRefreshing(true);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "請檢查網路連線", Toast.LENGTH_LONG).show();
                                }
                            }, 4000);
                        }
                    });

                    swipe_refresh.setRefreshing(false);


//                    model.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
//                        @Override
//                        public void onChanged(User user) {
//                            member_card.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Toast.makeText(getContext(), user.getEmail()+"已經登入", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    });
                }
            }
        });


        launcher1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_FIRST_USER) {
                    Log.e("userIdNew", userId + "");
                    OrderManager.getInstance().syncDataFromRemote("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/users/" + userId + "/orders", new Runnable() {
                        @Override
                        public void run() {
//                        Order order = OrderManager.getInstance().getOrder();
                            ArrayList<OrderItem> orderItems = OrderManager.getInstance().getOrderItems();
                            Order order = OrderManager.getInstance().getOrder();
                            for(int i = 0;i<order.getOrderItemList().size();i++){
                                order.getOrderItemList().get(i).setUserId(userId);
                            }
                            modelForOrder.setOrderMutableLiveData(order);
                        }
                    });
                    count = 5;
                    swipe_refresh.setRefreshing(true);
                    pageAdapter.clearUI();
                    HomeManager.getInstance().syncDataFromRemote1("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", new Callback3() {
                        @Override
                        public void success() {
                            productArrayList = HomeManager.getInstance().getProductArrayList();
                            for (int i = 0; i < productArrayList.size(); i++) {
                                productArrayList.get(i).setUserId(userId);
                            }
                            productArrayListTotal.addAll(productArrayList);
                            pageAdapter.refreshUI(productArrayList);
                            productPage = HomeManager.getInstance().getProductPage();
                            Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                            swipe_refresh.setRefreshing(false);
                        }

                        @Override
                        public void fail(Exception e) {
                            swipe_refresh.setRefreshing(true);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "請檢查網路連線", Toast.LENGTH_LONG).show();
                                }
                            }, 4000);
                        }
                    });

                    swipe_refresh.setRefreshing(false);
                }

            }
        });

        model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        model.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userId = user.getUserId();
                count = 5;
                swipe_refresh.setRefreshing(true);
                pageAdapter.clearUI();
                HomeManager.getInstance().syncDataFromRemote1("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", new Callback3() {
                    @Override
                    public void success() {
                        productArrayList = HomeManager.getInstance().getProductArrayList();
                        for (int i = 0; i < productArrayList.size(); i++) {
                            productArrayList.get(i).setUserId(userId);
                        }
                        productArrayListTotal.addAll(productArrayList);
                        pageAdapter.refreshUI(productArrayList);
                        productPage = HomeManager.getInstance().getProductPage();
                        Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                        swipe_refresh.setRefreshing(false);
                    }

                    @Override
                    public void fail(Exception e) {
                        swipe_refresh.setRefreshing(true);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "請檢查網路連線", Toast.LENGTH_LONG).show();
                            }
                        }, 4000);
                    }
                });

                swipe_refresh.setRefreshing(false);

                member_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), user.getUserId() + "已經登入", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        model.getIsTrue().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    set_member_card();
                    userId = 0;
                    count = 5;
                    swipe_refresh.setRefreshing(true);
                    pageAdapter.clearUI();
                    HomeManager.getInstance().syncDataFromRemote1("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", new Callback3() {
                        @Override
                        public void success() {
                            productArrayList = HomeManager.getInstance().getProductArrayList();
                            for (int i = 0; i < productArrayList.size(); i++) {
                                productArrayList.get(i).setUserId(userId);
                            }
                            productArrayListTotal.addAll(productArrayList);
                            pageAdapter.refreshUI(productArrayList);
                            productPage = HomeManager.getInstance().getProductPage();
                            Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                            swipe_refresh.setRefreshing(false);
                        }

                        @Override
                        public void fail(Exception e) {
                            swipe_refresh.setRefreshing(true);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "請檢查網路連線", Toast.LENGTH_LONG).show();
                                }
                            }, 4000);
                        }
                    });

                    swipe_refresh.setRefreshing(false);
                }
            }
        });

//        launcher1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if (result.getResultCode() == Activity.RESULT_FIRST_USER) {
//                    Log.e("userIdNew",userId+"");
//                    OrderManager.getInstance().syncDataFromRemote("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/users/"+userId+"/orders", new Runnable() {
//                        @Override
//                        public void run() {
////                        Order order = OrderManager.getInstance().getOrder();
//                            ArrayList<OrderItem> orderItems = OrderManager.getInstance().getOrderItems();
//                            pageAdapter.setLan(launcher1);
//                            modelForOrder.setOrderMutableLiveData(orderItems);
//
//                        }
//                    });
//                }
//            }
//        });

        HomeManager.getInstance().homeView(list, getContext(), recyclerView, bannerIndicator);

        setEdit_search();

        setNestedScrollViewToTop();


        pageAdapter = new PageAdapter(productArrayList, FragmentHome.this);
        HomeManager.getInstance().pictureView(pictureRecycleView, getContext(), pageAdapter);
        pictureRecycleView.setAdapter(pageAdapter);

        HomeManager.getInstance().syncDataFromRemote("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", new Runnable() {
            @Override
            public void run() {
                productArrayList = HomeManager.getInstance().getProductArrayList();
                for (int i = 0; i < productArrayList.size(); i++) {
                    productArrayList.get(i).setUserId(userId);
                }
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
                    HomeManager.getInstance().syncDataFromRemote("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products?limit=5&offset=" + count, new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Product> productArrayList1 = HomeManager.getInstance().getProductArrayList();
                            for (int i = 0; i < productArrayList1.size(); i++) {
                                productArrayList1.get(i).setUserId(userId);
                            }
                            productArrayList.addAll(productArrayList1);
                            productArrayListTotal.addAll(productArrayList1);

                            Log.e("length1", productArrayList.size() + "");
                            Log.e("length2", productArrayList1.size() + "");

                            pageAdapter.refreshUI(productArrayList);
                            pageAdapter.setLoadState(pageAdapter.LOADING_COMPLETE);
                            count += 5;

                        }
                    });
                } else {
                    // 显示加载到底的提示
                    pageAdapter.setLoadState(pageAdapter.LOADING_END);
                }
            }
        });
        updateDataBySwipe();
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

    private void set_member_card() {
        member_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FragmentHome.this.getActivity(), Login.class);
                launcher.launch(intent);
            }
        });
    }

    private void updateDataBySwipe() {
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh.setRefreshing(true);
                pageAdapter.clearUI();
                count = 5;
                HomeManager.getInstance().syncDataFromRemote1("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products", new Callback3() {
                    @Override
                    public void success() {
                        productArrayList = HomeManager.getInstance().getProductArrayList();
                        for (int i = 0; i < productArrayList.size(); i++) {
                            productArrayList.get(i).setUserId(userId);
                        }
                        productArrayListTotal.addAll(productArrayList);
                        pageAdapter.refreshUI(productArrayList);
                        productPage = HomeManager.getInstance().getProductPage();
                        Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                        swipe_refresh.setRefreshing(false);
                    }

                    @Override
                    public void fail(Exception e) {
                        swipe_refresh.setRefreshing(true);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "請檢查網路連線", Toast.LENGTH_LONG).show();
                            }
                        }, 4000);
                    }
                });

                swipe_refresh.setRefreshing(false);
            }
        });
    }

    public void hello(Intent intent) {
        launcher1.launch(intent);
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}