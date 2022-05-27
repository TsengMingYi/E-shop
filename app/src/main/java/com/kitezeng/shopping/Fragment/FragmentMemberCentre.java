package com.kitezeng.shopping.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kitezeng.shopping.ListViewModel;
import com.kitezeng.shopping.Login;
import com.kitezeng.shopping.MainActivity;
import com.kitezeng.shopping.R;
import com.kitezeng.shopping.ShoppingMaster;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMemberCentre#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMemberCentre extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseUser user;
    private int count = 0;
    private Button isButtonNull;
    private Button masterButton;
    private ListViewModel model;
    private FirebaseAuth mAuth;
    private TextView textView123;
    private LinearLayout rootView;
    private LinearLayout second_linearlayout;
    //    private TextView textView = new TextView(getContext());
    private ActivityResultLauncher<Intent> launcher;
    private LinearLayout linearLayout;

    public FragmentMemberCentre() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment FragmentMemberCentre.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMemberCentre newInstance(String userEmail) {
        FragmentMemberCentre fragment = new FragmentMemberCentre();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        args.putString("userEmail", userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
//        FirebaseAuth.getInstance().signOut();

        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//            email = getArguments().getParcelable("parcelable");
//            userEmail = getArguments().getString("userEmail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("testFavorite", "testMember");

        View view = inflater.inflate(R.layout.fragment_member_centre, container, false);

        linearLayout = view.findViewById(R.id.linearLayout);
        second_linearlayout = view.findViewById(R.id.second_linearlayout);
        model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        user = mAuth.getCurrentUser();
        if (user == null) {
//            model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
            model.setIsTrue(false);

        } else {
//            model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
            model.setUserMutableLiveData(user);


        }
//        model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        model.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                textView123 = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                textView123.setLayoutParams(layoutParams);
                textView123.setText(user.getEmail());
                Log.e("data:", user.getEmail());
                textView123.setBackgroundColor(0xff66ff66);
                second_linearlayout.addView(textView123);
                linearLayout.removeView(rootView);
                isButtonNull = new Button(getContext());
                isButtonNull.setText("登出");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 20, 0, 0);
                isButtonNull.setLayoutParams(params);
                isButtonNull.setPadding(10, 10, 10, 10);
                linearLayout.addView(isButtonNull);

                isButtonNull.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getContext(), "登出成功", Toast.LENGTH_SHORT).show();
                        model.setIsTrue(false);
                        second_linearlayout.removeView(textView123);
                        linearLayout.removeView(masterButton);
                        linearLayout.removeView(isButtonNull);
                    }
                });

            }
        });

        model.getIsTrue().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
//                linearLayout.removeView(rootView);
                rootView = new LinearLayout(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 0);
                rootView.setLayoutParams(params);
                rootView.setOrientation(LinearLayout.VERTICAL);
                rootView.setPadding(10, 10, 10, 10);
                rootView.setBackgroundColor(0xFFAAAAAA);
                //
                LinearLayout secondView = new LinearLayout(getContext());
                secondView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                secondView.setOrientation(LinearLayout.HORIZONTAL);
                View view1 = new View(getContext());
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, 1);
                params1.weight = 1.0f;
                params1.gravity = Gravity.CENTER_VERTICAL;
                view1.setLayoutParams(params1);
                view1.setBackgroundColor(0xFFdfdfdf);
                secondView.addView(view1);
                TextView textView = new TextView(getContext());
                LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(0, 50);
                params4.weight = 1.0f;
                params4.gravity = Gravity.CENTER;
                textView.setLayoutParams(params4);
                textView.setText("您尚未登入喔");
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                secondView.addView(textView);
                View view2 = new View(getContext());
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, 1);
                params2.weight = 1.0f;
                params2.gravity = Gravity.CENTER_VERTICAL;
                view2.setLayoutParams(params2);
                view2.setBackgroundColor(0xFFdfdfdf);
                secondView.addView(view2);
                Button button = new Button(getContext());
                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params3.setMargins(0, 20, 0, 0);
                button.setLayoutParams(params3);
                button.setPadding(10, 10, 10, 10);
                button.setText("立即登入 / 申辦會員");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Drawable drawable = getResources().getDrawable(R.drawable.button_shape, getContext().getTheme());
                    button.setBackground(getContext().getDrawable(R.drawable.button_shape));
                }
                rootView.addView(secondView);
                rootView.addView(button);
                linearLayout.addView(rootView);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FragmentMemberCentre.this.getActivity(), Login.class);
                        launcher.launch(intent);
                    }
                });
            }
        });


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            user = result.getData().getParcelableExtra("data");
                            model.setUserMutableLiveData(user);
                        }
                    }
                });

        return view;
    }
}