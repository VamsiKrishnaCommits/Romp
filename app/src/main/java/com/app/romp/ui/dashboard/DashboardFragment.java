package com.app.romp.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.app.romp.MainActivity;
import com.app.romp.R;
import com.app.romp.king;
import com.app.romp.ui.Main8Activity;
import com.app.romp.ui.home.HomeFragment;
import com.app.romp.ui.home.item;
import com.app.romp.ui.whatothersordered;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DashboardFragment extends Fragment  {

   static View root=null;
    static ArrayList<String> orderid;
    static Context context;
    static RecyclerView recyclerView;
    static int countForOuter=0;
    static    View view;
    static DatabaseReference details;
    FirebaseDatabase database;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        view = root.findViewById(R.id.rel);
        database= FirebaseDatabase.getInstance();
        orderid=new ArrayList<>();
        context=getContext();
        DatabaseReference history=database.getReference(MainActivity.ordernode);
        details = database.getReference(MainActivity.ordernode);

        history.limitToLast(30).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String s = snapshot.getKey();
                    orderid.add(s);
                    root.findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                }
                Collections.reverse(orderid);
                countForOuter = orderid.size();
                recyclerView = root.findViewById(R.id.recyclerviewouter);
                recyclerView.setVisibility(View.INVISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                custom1 custom = new custom1();
                recyclerView.setAdapter(custom);
                if (orderid.size() == 0) {
                    root.findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.nohis).setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "NO HISTORY!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return root;
    }
}
class custom1 extends RecyclerView.Adapter{
    king his;
    ArrayList<item> history=new ArrayList<>();
    Myview1 myview;
    int size=0;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowre,viewGroup,false);
        Myview1 myview=new Myview1(view);
        return myview;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        DashboardFragment.details.child(DashboardFragment.orderid.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                his = dataSnapshot.getValue(king.class);
                history = his.a;
                String time = his.getTime();
                size = his.a.size();
                Log.i("hey", history.get(0).name + size);
                myview = (Myview1) viewHolder;
                myview.textView.setText(his.getTime());
                myview.bill.setText(his.getTotal() + "");
                myview.Hotel.setText(his.getHotelname()+"");
                myview.orderid.setVisibility(View.GONE);
                myview.ot.setVisibility(View.GONE);
                myview.orderid.setText(DashboardFragment.orderid.get(i).substring(16, 20));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardFragment.context);
                myview.recyclerView.setLayoutManager(linearLayoutManager);
                DashboardFragment.recyclerView.setVisibility(View.VISIBLE);
                DashboardFragment.view.findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                cust1 cust1 = new cust1(DashboardFragment.orderid.get(i), size, history);
                myview.recyclerView.setAdapter(cust1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return DashboardFragment.countForOuter;
    }
}
class Myview1 extends RecyclerView.ViewHolder{
    RecyclerView recyclerView;
    TextView textView;
    TextView bill;
    TextView orderid;
    TextView Hotel;
    TextView ot;
    public Myview1(@NonNull View itemView) {
        super(itemView);
        recyclerView=itemView.findViewById(R.id.recyclerinner);
        textView=itemView.findViewById(R.id.timee);
        bill=itemView.findViewById(R.id.rateeforhis);
        orderid=itemView.findViewById(R.id.orderidd);
        Hotel=itemView.findViewById(R.id.hotelname);
        ot=itemView.findViewById(R.id.orderr);
    }
}
class cust1 extends RecyclerView.Adapter {
    String order;
    king his;
    ArrayList<item> history;
    int size=0;

    public cust1(String s, int size, ArrayList<item> history) {
        order=s;
        this.size=size;
        this.history=history;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row2, viewGroup, false);
        My12 my = new My12(view);
        return my;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        My12 my1 = (My12) viewHolder;
        Log.i("hey second","second" + viewHolder.getAdapterPosition());
        my1.name.setText(history.get(i).name);
        my1.qty.setText(history.get(i).counter +"");
        my1.rate.setText(history.get(i).getCost()+"");

    }
    @Override
    public int getItemCount() {
        return size;
    }
}
class My12 extends RecyclerView.ViewHolder {

    TextView name;
    TextView qty;
    TextView rate;

    public My12(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textname);
        qty = itemView.findViewById(R.id.textqty);
        rate = itemView.findViewById(R.id.textrate);
    }
}

