package com.kitezeng.shopping.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kitezeng.shopping.ListViewModel;
import com.kitezeng.shopping.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentShoppingCar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentShoppingCar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textView1;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    public FragmentShoppingCar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankShoppingCar.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentShoppingCar newInstance(String param1, String param2) {
        FragmentShoppingCar fragment = new FragmentShoppingCar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        Log.e("testShopping","testShopping");

        View view = inflater.inflate(R.layout.fragment_shopping_car, container, false);
        textView1 = view.findViewById(R.id.textView_shopping_car);

        if(mAuth.getCurrentUser() == null){
            textView1.setText("這是購物車");
        }else{
            user = mAuth.getCurrentUser();
            textView1.setText(user.getEmail()+" 成功登入");
        }


        ListViewModel model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        model.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {

                    textView1.setText(firebaseUser.getEmail()+" 成功登入");
//                Log.e("ShoppingCarData",firebaseUser.getEmail());
            }
        });

        model.getIsTrue().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    textView1.setText("這是購物車");
                }
            }
        });

        return view;
    }
}