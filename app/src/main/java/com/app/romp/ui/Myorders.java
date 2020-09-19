package com.app.romp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.romp.Auth;
import com.app.romp.R;
import com.app.romp.king;

import com.app.romp.ui.home.HomeFragment;
import com.app.romp.ui.home.item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Myorders extends AppCompatActivity {FirebaseDatabase database;
    static ArrayList<String> orderid;
    static Context context;
    static RecyclerView recyclerView;
    static int countForOuter=0;
    static    View view;
    static DatabaseReference details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);
        view = findViewById(R.id.rel);
        database= FirebaseDatabase.getInstance();
        orderid=new ArrayList<>();
        context=getApplicationContext();
        String userid=Required.userid;
        DatabaseReference history=database.getReference(userid);
        details = database.getReference("total orders");

        try {
            history.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String s = snapshot.getValue(String.class);
                        orderid.add(s);
                        findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                    }
                    Collections.reverse(orderid);
                    countForOuter = orderid.size();
                    recyclerView = findViewById(R.id.recyclerviewouter);
                    recyclerView.setVisibility(View.INVISIBLE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    custom custom = new custom();
                    recyclerView.setAdapter(custom);
                    if(orderid.size()==0){
                        findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                        findViewById(R.id.nohis).setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"NO HISTORY!!",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"NO HISTORRRYYYY!!",Toast.LENGTH_SHORT).show();
        }
    }
}
class custom extends RecyclerView.Adapter{
    king his;
    ArrayList<item> history=new ArrayList<>();
    Myview myview;
    int size=0;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowre,viewGroup,false);
        Myview myview=new Myview(view);
        return myview;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {

        Myorders.details.child(Myorders.orderid.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    his = dataSnapshot.getValue(king.class);
                    history = his.a;
                    String time = his.getTime();
                    size = his.a.size();
                    Log.i("hey", history.get(0).name + size);
                    myview = (Myview) viewHolder;
                    myview.textView.setText(his.getTime());
                    myview.bill.setText(his.getTotal() + "");
                    myview.hotel.setText(his.getHotelname()+"");
                    myview.orderid.setText(Myorders.orderid.get(i).substring(16, 20));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Myorders.context);
                    myview.recyclerView.setLayoutManager(linearLayoutManager);
                    Myorders.recyclerView.setVisibility(View.VISIBLE);
                   Myorders.view.findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                    cust cust = new cust(Myorders.orderid.get(i), size, history);
                    myview.recyclerView.setAdapter(cust);
                }
                catch (Exception e){
                    Myorders.view.findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                    Myorders.view.findViewById(R.id.nohis).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return Myorders.countForOuter;
    }
}
class Myview extends RecyclerView.ViewHolder{
    RecyclerView recyclerView;
    TextView textView;
    TextView bill;
    TextView orderid;
    TextView hotel;
    public Myview(@NonNull View itemView) {
        super(itemView);
        recyclerView=itemView.findViewById(R.id.recyclerinner);
        textView=itemView.findViewById(R.id.timee);
        bill=itemView.findViewById(R.id.rateeforhis);
        orderid=itemView.findViewById(R.id.orderidd);
        hotel=itemView.findViewById(R.id.hotelname);
    }
}
class cust extends RecyclerView.Adapter {
    String order;
    king his;
    ArrayList<item> history;
    int size=0;

    public cust(String s, int size, ArrayList<item> history) {
        order=s;
        this.size=size;
        this.history=history;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row2, viewGroup, false);
        My1 my = new My1(view);
        return my;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        My1 my1 = (My1) viewHolder;
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
class My1 extends RecyclerView.ViewHolder {

    TextView name;
    TextView qty;
    TextView rate;

    public My1(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textname);
        qty = itemView.findViewById(R.id.textqty);
        rate = itemView.findViewById(R.id.textrate);
    }
}

