package com.app.romp.ui.home;

public class item{


    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getCounter() {
        return counter;
    }

String availability;
    int cost;

    public String getAvailability() {
        return availability;
    }

    public String name;
    public int counter=0;
   public item(int cost,int counter,String name){
this.counter=counter;
        this.cost=cost;
        this.name=name;

    }
    item(){

    }
}
