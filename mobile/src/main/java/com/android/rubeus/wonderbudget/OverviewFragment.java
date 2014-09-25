package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;

public class OverviewFragment extends Fragment {
    private static final String TAG = "OverviewFragment";
    private static final int NEW_TRANSACTION = 1;
    private DatabaseHandler db;

    public static OverviewFragment newInstance() {
//        OverviewFragment fragment = ;
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);

        return new OverviewFragment();
    }
    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        TextView totalAmount = (TextView) view.findViewById(R.id.totalAmount);
        TextView realAmount = (TextView) view.findViewById(R.id.realAmount);
        ImageButton addButton = (ImageButton) view.findViewById(R.id.addButton);

        totalAmount.setText(db.getTotalAmount()+"");
        realAmount.setText(db.getRealAmount()+"");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTransaction();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void createNewTransaction(){
        Intent intent = new Intent(getActivity(), TransactionActionActivity.class);
        intent.putExtra("typeOfDialog", TransactionActionActivity.ADD_NEW_TRANSACTIION);
        startActivityForResult(intent, 1);
    }
}
