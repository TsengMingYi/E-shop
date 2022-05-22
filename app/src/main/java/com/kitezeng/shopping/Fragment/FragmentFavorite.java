package com.kitezeng.shopping.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitezeng.shopping.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFavorite#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFavorite extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textView2;


    public FragmentFavorite() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFavorite.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFavorite newInstance(String param1, String param2) {
        FragmentFavorite fragment = new FragmentFavorite();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//                Presenter,
//        textView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textView3.setText('')
//            }
//        });
//        UI <-> Logic
//        mvc,
//                mvp,
//                mvvm,
//                others
//        queue(a_request, b_request),
//                mvvm,
//
//        8590 -> 8591,
//
//        Firebase,
//                AWS,
//                主機1(api server), multi-thread-18, 100, api,
//                database-


        Log.e("testFavorite","testFavorite");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        textView2 = view.findViewById(R.id.textView2);
        textView2.setText("這是我的最愛");
        return view;
    }


}