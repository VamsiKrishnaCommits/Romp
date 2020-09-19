package com.app.romp;

import com.app.romp.ui.Required;
import com.app.romp.ui.home.HomeFragment;
import com.app.romp.ui.home.bill;
import com.app.romp.ui.home.item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.app.romp.MainActivity.send;

public class king{
    public  String s=ViewCart.phone;
    String tok= Required.userid;
    String time=getdate();
    int total= bill.totalcost;
   String hotelname=MainActivity.hotelname;
   String upimeth=ViewCart.upi;

    public String getUpimeth() {
        return upimeth;
    }
    public String getHotelname() {
        return hotelname;
    }

    public  String getS() {
        return s;
    }

    public int getTotal() {
        return total;
    }
    public ArrayList<item> a=send;
    public ArrayList<item> getA() {
        return a;
    }
    public String getTok() {
        return tok;
    }

    public String getTime() {
        return time;
    }

    private String getdate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss ");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

}
