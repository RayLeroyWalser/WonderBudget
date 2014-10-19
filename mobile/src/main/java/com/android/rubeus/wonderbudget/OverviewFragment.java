package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;

public class OverviewFragment extends Fragment {
    private static final String TAG = "OverviewFragment";
    private static final int NEW_TRANSACTION = 1;
    private DatabaseHandler db;
    private TextView totalAmount, realAmount;
    private ProgressBar progressBar1, progressBar2;

    public static OverviewFragment newInstance() {
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
        totalAmount = (TextView) view.findViewById(R.id.totalAmount);
        realAmount = (TextView) view.findViewById(R.id.realAmount);
        ImageButton addButton = (ImageButton) view.findViewById(R.id.addButton);
        ((GradientDrawable)addButton.getBackground()).setColor(getResources().getColor(R.color.green));

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

    private void createNewTransaction(){
        Intent intent = new Intent(getActivity(), TransactionActionActivity.class);
        intent.putExtra("typeOfDialog", TransactionActionActivity.ADD_NEW_TRANSACTIION);
        startActivityForResult(intent, TransactionActionActivity.ADD_NEW_TRANSACTIION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case TransactionActionActivity.ADD_NEW_TRANSACTIION:
                totalAmount.setText(db.getTotalAmount()+"");
                realAmount.setText(db.getRealAmount()+"");
                break;
        }
    }
}
