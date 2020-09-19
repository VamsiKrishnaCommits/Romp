package com.app.romp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.romp.ui.doubleclick;
import com.app.romp.ui.home.bill;

import static com.app.romp.Auth.handle;
import static com.app.romp.MainActivity.send;


public class summary extends Fragment {

    public interface iniot{
        void brook();
    }
    iniot in;
    Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }

        try {
            in = (iniot) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.issummary=true;
        View root= inflater.inflate(R.layout.fragment_summary, container, false);
        TextView textView=root.findViewById(R.id.orderid);
        textView.setText(ViewCart.id.substring(16,20));
        textView.setTextSize(15);
        TextView textView1=root.findViewById(R.id.ratee2);
        textView1.setText(bill.totalcost+"");
        RecyclerView recyclerView=root.findViewById(R.id.recyc2);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        custo custo=new custo();
        recyclerView.setAdapter(custo);
        root.findViewById(R.id.butt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(12).getTime() < 1000){
                    return;
                }
                handle.set(12,new doubleclick( SystemClock.elapsedRealtime()));
               in.brook();
            }
        });
        return root;
    }
}

class custo extends RecyclerView.Adapter{

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row2, viewGroup, false);
        My1 my = new My1(view);

        return my;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        My1 my1=(My1)viewHolder;
        my1.name.setText(send.get(i).name);
        my1.qty.setText(send.get(i).counter + "");
        my1.rate.setText(send.get(i).getCost()+"");
    }

    @Override
    public int getItemCount() {
        return send.size();
    }
}