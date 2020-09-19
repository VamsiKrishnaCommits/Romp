package com.app.romp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BlankFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_blank, container, false);
       root. findViewById(R.id.cant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(), MainActivity.class);
                in.putExtra("1","realiiitgmenu");
                in.putExtra("2","ordersforcanteen");
                in.putExtra("gpay","upiid");
                in.putExtra("phonepe","upiiid");
                in.putExtra("amazonpay","upiid");
                in.putExtra("paytm","upiid");
                startActivity(in);
            }
        });
       root. findViewById(R.id.food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(), MainActivity.class);
                in.putExtra("1","foodtruckrealtime");
                in.putExtra("2","ordersforfoodtruck");
                in.putExtra("gpay","upiid");
                in.putExtra("phonepe","upiiid");
                in.putExtra("amazonpay","upiid");
                in.putExtra("paytm","upiid");
                startActivity(in);
            }
        });
        root.findViewById(R.id.ranj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(), MainActivity.class);
                in.putExtra("1","ranjeetharealtime");
                in.putExtra("2","ordersforranjeetha");
                in.putExtra("gpay","upiid");
                in.putExtra("phonepe","upiiid");
                in.putExtra("amazonpay","upiid");
                in.putExtra("paytm","upiid");
                startActivity(in);
            }
        });
return  root;

    }


}
