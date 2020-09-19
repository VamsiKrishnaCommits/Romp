package com.app.romp.ui.home;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.romp.R;
import com.app.romp.ui.doubleclick;
import com.app.romp.ui.measure;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.app.romp.Auth.handle;
import static com.app.romp.MainActivity.items;
import static com.app.romp.MainActivity.send;

public class HomeFragment extends Fragment {
   public interface ini{
        void bro();
    }
 static    CustomAdap customAdap;
    ini in;
    Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }

        try {
            in = (ini) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }

    }
View root;
    static  View v;
Context context;
   public static int hei;
    static int[]a=new int[15];
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }
   public  void trigger(){
       RecyclerView recyclerView=root.findViewById(R.id.recy);
       TextView t = HomeFragment.v.findViewById(R.id.summary);
       t.setText(bill.totalcost + "    | items " + bill.number);
       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
       recyclerView.setLayoutManager(linearLayoutManager);
       customAdap = new CustomAdap(items,context,send);
       recyclerView.setAdapter(customAdap);
       root.findViewById(R.id.proo).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomeFragment.hei=root.getHeight();
        Log.i("height",measure.height+"");
        if(items.size()==0){
            root.findViewById(R.id.proo).setVisibility(View.VISIBLE);
        }
        else
            root.findViewById(R.id.proo).setVisibility(View.INVISIBLE);
        context=getContext();

        if(bill.number>0){
            v=root.findViewById(R.id.pop);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "translationY", -measure.height).setDuration(500);
            objectAnimator.start();
            TextView t = HomeFragment.v.findViewById(R.id.summary);
            t.setText(bill.totalcost + "    | items " + bill.number);
        }
        v=root.findViewById(R.id.pop);
        RecyclerView recyclerView=root.findViewById(R.id.recy);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdap = new CustomAdap(items,context,send);
        recyclerView.setAdapter(customAdap);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
               root. findViewById(R.id.viewcart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (SystemClock.elapsedRealtime() - handle.get(6).getTime() < 1000){
                            return;
                        }
                        handle.set(6,new doubleclick( SystemClock.elapsedRealtime()));
                        in.bro();
                    }
                });
            }
        });
        thread.run();
    }
}
class CustomAdap extends RecyclerView.Adapter {
    ArrayList<item> itemm;
    Context context;
    ArrayList<item> sendd;
    Vibrator vibrator;
    CustomAdap(ArrayList<item> items, Context context1,ArrayList<item>send) {
        itemm = items;
        context = context1;
        vibrator=(Vibrator)context1.getSystemService(Context.VIBRATOR_SERVICE);
        sendd=send;
    }
    @NonNull
    @Override
    public My onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        My my = new My(view);
        return my;
    }

    boolean checkadd(int i) {
        int j;

        for (j = 0; j < sendd.size(); j++) {
            if (itemm.get(i).name.equals(sendd.get(j).name)){
                sendd.set(j, itemm.get(i));
                return false;
            }
        }
        return true;
    }
    int getindex(int i) {
        for (int j = 0; j < sendd.size(); j++) {
            if (itemm.get(i).name.equals(sendd.get(j).name)) {
                return j;
            }
        }
        return 0;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        final My my = (My) viewHolder;
        my.cost.setText("" + itemm.get(i).cost);
        my.name.setText(itemm.get(i).name);
        my.name.setTextSize(25);
        my.counter.setText(itemm.get(i).counter+"");
        if(itemm.get(i).availability.equals("0")){
            my.minus.setVisibility(View.INVISIBLE);
            my.plus.setVisibility(View.INVISIBLE);
            my.name.setText(itemm.get(i).name+"--- Unavailable :(");
            my.counter.setVisibility(View.INVISIBLE);
        }
        my.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                if (checkadd(i))
                    sendd.add(itemm.get(i));
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(HomeFragment.v, "translationY", -measure.height).setDuration(500);
                objectAnimator.start();
                itemm.get(i).counter++;
                bill.number++;
                bill.totalcost = bill.totalcost + itemm.get(i).cost;
                TextView t = HomeFragment.v.findViewById(R.id.summary);
                t.setText(bill.totalcost + "    | items " + bill.number);
                my.counter.setText("" + itemm.get(i).counter);

            }
        });
        my.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                TextView t = HomeFragment.v.findViewById(R.id.summary);
                itemm.get(i).counter--;
                bill.number--;
                bill.totalcost = bill.totalcost - itemm.get(i).cost;
                if (itemm.get(i).counter == 0) {
                    int j;
                    j = getindex(i);
                    Log.i("hey",j+"");
                    sendd.remove(j);
                    for(int k=0;k<sendd.size();k++)
                        Log.i("hey",sendd.get(k).name);
                }
                if (itemm.get(i).counter < 0) {
                    itemm.get(i).counter++;
                    bill.number++;
                    bill.totalcost = bill.totalcost + itemm.get(i).cost;
                    Toast.makeText(context, "Even we wish that we could order in negatives but somehow its not our model :(", Toast.LENGTH_LONG).show();
                } else {
                    int j;
                    j = getindex(i);
                    my.counter.setText("" + itemm.get(i).counter);
                    t.setText(bill.totalcost + "    | items " + bill.number);
                }
                if (bill.number <= 0) {
                    ObjectAnimator objectAnimator =  ObjectAnimator.ofFloat(HomeFragment.v, "translationY", measure.height).setDuration(1000);
                    objectAnimator.start();
                }
            }
        });
    }
    @Override
    public int getItemCount () {
        return itemm.size();
    }
    @Override
    public int getItemViewType ( int position){
        return position;
    }
    @Override
    public long getItemId ( int position){
        return position;
    }
}

class My extends RecyclerView.ViewHolder{
    TextView name;
    TextView cost;
    TextView counter;
    ImageView image;
    ImageView plus;
    ImageView minus;
    public My(@NonNull View itemView) {
        super(itemView);
        name= itemView.findViewById(R.id.textfood);
        cost=itemView.findViewById(R.id.rate);
        plus=itemView.findViewById(R.id.plus);
        minus=itemView.findViewById(R.id.minus);
        counter=itemView.findViewById(R.id.counter);
    }
}
