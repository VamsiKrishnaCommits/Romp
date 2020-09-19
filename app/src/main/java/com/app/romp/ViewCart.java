package com.app.romp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.romp.ui.Required;
import com.app.romp.ui.doubleclick;
import com.app.romp.ui.home.HomeFragment;
import com.app.romp.ui.home.bill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.app.romp.Auth.handle;
import static com.app.romp.MainActivity.send;


public class ViewCart extends Fragment {
    public static String upi;
    static boolean allowback = true;
    static  final int PAYMENT_REQUEST=5555;
    static final String AMAZON_PAY = "in.amazon.mShop.android.shopping";
    static final String BHIM_UPI = "in.org.npci.upiapp";
    static final String GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user";
    static final String PHONE_PE = "com.phonepe.app";
    static final String PAYTM = "net.one97.paytm";
    String gpay = MainActivity.upi.getGpay();
    String phonepe = MainActivity.upi.getPhonepe();
    String amazon = MainActivity.upi.getAmazonpay();
    String paytm = MainActivity.upi.getPaytm();
    public static String phone;

    public interface inio {
        void broo();
    }

    inio in;
    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }

        try {
            in = (inio) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }

    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mre1 = database.getReference("counter");
    NotificationChannel notificationChannel;
    DatabaseReference mrec = database.getReference(MainActivity.ordernode);
    Long a;
    Long b;
    static String id;
    String CHANNEL_ID = "to client";
    View root;
    String CHANNEL_ORDER = "ordered";
    String CHANNEL_ITEM = "Sit back and relax...we will notify you when your food is ready!";

    class item {


        public String getName() {
            return name;
        }

        public int getCost() {
            return cost;
        }

        public int getCounter() {
            return counter;
        }


        int cost;
        String name;
        int counter = 0;

        item(int imageid, int cost, String name) {

            this.cost = cost;
            this.name = name;

        }

        item() {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_cart, container, false);
        final EditText editText=root.findViewById(R.id.phonenumber);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("phone",0);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        phone=sharedPreferences.getString("phonenumber","");
if(phone.length()>=10){
    editText.setText(phone);
    Log.i("hey",phone);
    Button b = root.findViewById(R.id.save);
b.setText("Edit");
editText.setFocusableInTouchMode(false);
}
if(MainActivity.hotelname.equals("IIITG Canteen"))
    root.findViewById(R.id.cardpho).setVisibility(View.GONE);

if(gpay.equals("-"))
    root.findViewById(R.id.gpaybutton).setVisibility(View.GONE);

 if(phonepe.equals("-"))
    root.findViewById(R.id.phonepe).setVisibility(View.GONE);

if(amazon.equals("-"))
    root.findViewById(R.id.amazonpay).setVisibility(View.GONE);

 if(paytm.equals("-"))
    root.findViewById(R.id.paytm).setVisibility(View.GONE);

        root.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(7).getTime() < 1000){
                    return;
                }
                handle.set(7,new doubleclick( SystemClock.elapsedRealtime()));
                Button b = root.findViewById(R.id.save);
                 phone=editText.getText().toString();
                phone=phone.trim();
if(b.getText().equals("Edit")){
    editText.setFocusableInTouchMode(true);
    Log.i("hey","hey");
    b.setText("save");
}
              else  if(phone.length()>=10) {
                    editor.putString("phonenumber", phone);
                    editor.apply();
                    Toast.makeText(getContext(),"Saved",Toast.LENGTH_LONG).show();
                    editText.clearFocus();
                    editText.setFocusable(false);
                    b.setText("Edit");
                }
                else{
                    Toast.makeText(getContext(),"Please enter a valid number to receive updates",Toast.LENGTH_SHORT).show();
                    editText.setError("invalid");
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final EditText editText=root.findViewById(R.id.phonenumber);
        RecyclerView recyclerView = root.findViewById(R.id.recyc);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        TextView textView = root.findViewById(R.id.ratee);
        textView.setText("" + bill.totalcost);
        cust cust = new cust();
        recyclerView.setAdapter(cust);
        mrec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        root.findViewById(R.id.gpaybutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(8).getTime() < 1000 || ( editText.getText().toString().length()<10 && !MainActivity.hotelname.equals("IIITG Canteen"))){
                    if(editText.getText().toString().length()<=10)
                        editText.setError("Provide us a valid phone number!");
                    return;
                }
                else {
                    ViewCart.upi="gpay";
                    handle.set(8, new doubleclick(SystemClock.elapsedRealtime()));
                    startpayment(GOOGLE_PAY, gpay);
                }
            }
        });
        root.findViewById(R.id.phonepe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(9).getTime() < 1000 ||( editText.getText().toString().length()<10 && !MainActivity.hotelname.equals("IIITG Canteen"))){
                    if(editText.getText().toString().length()<=10)
                        Log.i("hey","here");
                    if(MainActivity.hotelname.equals("IIITG Canteen"))
                    Log.i("hey","ehy");
                        editText.setError("Provide us a valid phone number!");
                    return;
                }
                else {
                    ViewCart.upi="phonepe";
                    handle.set(9, new doubleclick(SystemClock.elapsedRealtime()));
                    startpayment(PHONE_PE, phonepe);
                }
            }
        });
        root.findViewById(R.id.paytm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(10).getTime() < 1000 || ( editText.getText().toString().length()<10 && !MainActivity.hotelname.equals("IIITG Canteen"))){
                    if(editText.getText().toString().length()<=10)
                        editText.setError("Provide us a valid phone number!");

                }
                else {
                    ViewCart.upi="paytm";
                    handle.set(10, new doubleclick(SystemClock.elapsedRealtime()));
                    startpayment(PAYTM, paytm);
                }
            }
        });
        root.findViewById(R.id.amazonpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(11).getTime() < 1000 || ( editText.getText().toString().length()<10 && !MainActivity.hotelname.equals("IIITG Canteen"))){
if(editText.getText().toString().length()<=10)
                    editText.setError("Provide us a valid phone number!");
                }
                else {
                    ViewCart.upi="amazonpay";
                    handle.set(11, new doubleclick(SystemClock.elapsedRealtime()));
                    startpayment(AMAZON_PAY, amazon);
                }
            }
        });
    }
    private void startpayment(String deeplink, String upi) {
        Uri uri = new Uri.Builder().scheme("upi").authority("pay").appendQueryParameter("pa", upi)
                .appendQueryParameter("pn", "Kamal").appendQueryParameter("tr","1234556")
                .appendQueryParameter("am", bill.totalcost + "")
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("url", "https://test.merchant.website").build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        try {
            intent.setPackage(deeplink);
            startActivityForResult(intent,PAYMENT_REQUEST);
        } catch (Exception E) {
            Toast.makeText(getContext(), "App not Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("heyy",requestCode+"");
        if (requestCode == PAYMENT_REQUEST) {
            if (data != null) {
                // Get Response from activity intent
                String response = data.getStringExtra("response");

                if (response == null) {
                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        TransactionDetails transactionDetails = getTransactionDetails(response);

                        if (transactionDetails.getStatus().toLowerCase().equals("success") ) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            switchit();

                        } else if (transactionDetails.getStatus().toLowerCase().equals("submitted")) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            switchit();
                        } else {
                            Toast.makeText(getContext(),"Cancelled", Toast.LENGTH_SHORT).show();
                   switchit();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    switchit();
                    }
                }
            } else {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            switchit();
            }

        } else {
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
        switchit();
        }


    }
    private Map<String, String> getQueryString(String url) {
        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    // Make TransactionDetails object from response string
    private TransactionDetails getTransactionDetails(String response) {
        Map<String, String> map = getQueryString(response);

        String transactionId = map.get("txnId");
        String responseCode = map.get("responseCode");
        String approvalRefNo = map.get("ApprovalRefNo");
        String status = map.get("Status");
        String transactionRefId = map.get("txnRef");

        return new TransactionDetails(
                transactionId,
                responseCode,
                approvalRefNo,
                status,
                transactionRefId
        );
    }

        private void switchit(){
            MainActivity.allow = true;
            root.findViewById(R.id.lin).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.pro).setVisibility(View.VISIBLE);
            id = mrec.push().getKey();
            mre1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    a = dataSnapshot.getValue(Long.class);
                    a++;
                    mre1.setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference mrecc = database.getReference(Required.userid);
                            mrecc.child(id).setValue(id);
                            DatabaseReference mre = database.getReference(MainActivity.ordernode).child(id);
                            if (new king().a != null) {
                                mre.setValue(new king()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Something is wrong,any debited money will be refunded shortly", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();
                                        }
                                        DatabaseReference mre = database.getReference("total orders").child(id);
                                        mre.setValue(new king()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                MainActivity.allow = false;
                                                in.broo();
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    //create notification channel if the api level is greater than oreo
                                                    notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ITEM, NotificationManager.IMPORTANCE_HIGH);
                                                    notificationChannel.setDescription(CHANNEL_ORDER);
                                                    NotificationManager manager = null;

                                                    try {
                                                        manager = getActivity().getSystemService(NotificationManager.class);
                                                        manager.createNotificationChannel(notificationChannel);

                                                    } catch (Exception e) {

                                                    }
                                                }
                                                try {
                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID).setStyle(new NotificationCompat.BigTextStyle().bigText(CHANNEL_ITEM)).setSmallIcon(R.mipmap.rompforeground).setContentTitle(CHANNEL_ORDER).setPriority(NotificationCompat.PRIORITY_MAX).setDefaults(NotificationCompat.DEFAULT_ALL);
                                                    NotificationManagerCompat manager = NotificationManagerCompat.from(getContext());
                                                    manager.notify(0, builder.build());
                                                } catch (Exception e) {

                                                }

                                            }
                                        });


                                    }
                                });

                            }
                            else{
                                Toast.makeText(getContext(),"something went wrong,please restart the app",Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

class My1 extends RecyclerView.ViewHolder {

    TextView name;
    TextView qty;
    TextView rate;
    public My1(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.textname);
        qty=itemView.findViewById(R.id.textqty);
        rate=itemView.findViewById(R.id.textrate);
    }
}

class cust extends RecyclerView.Adapter {

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
        my1.name.setText(send.get(i).name);
        my1.qty.setText(send.get(i).counter + "");
        my1.rate.setText(send.get(i).getCost() +"");
    }

    @Override
    public int getItemCount() {
        return send.size();
    }
}

