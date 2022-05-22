package com.kitezeng.shopping.Fragment.manager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kitezeng.shopping.R;
import com.kitezeng.shopping.apiHelper.ApiHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDelete#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDelete extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText delete_edit_id;
    private Button delete_request_btn;

    public FragmentDelete() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDelete.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDelete newInstance(String param1, String param2) {
        FragmentDelete fragment = new FragmentDelete();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete, container, false);
        delete_edit_id = view.findViewById(R.id.delete_edit_id);
        delete_request_btn = view.findViewById(R.id.delete_request_btn);

        delete_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete_edit_id != null) {
                    ApiHelper.deleteHttpData("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com/products/" + delete_edit_id.getText().toString(), new ApiHelper.Callback2() {
                        @Override
                        public void success() {
                            Toast.makeText(getContext(), "成功", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void fail(Exception exception) {
                            Toast.makeText(getContext(), "失敗", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "不可為空值", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}