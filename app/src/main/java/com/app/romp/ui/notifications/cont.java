package com.app.romp.ui.notifications;

import android.support.annotation.NonNull;

public class cont {
   public String name;
cont(String name){
    this.name=name;
}

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
