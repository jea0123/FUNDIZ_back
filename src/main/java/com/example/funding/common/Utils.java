package com.example.funding.common;

public class Utils {
    public static int getPercentNow(int currAmount, int goalAmount){
        if(goalAmount == 0) {
            return 0;
        } else {
            return (int) Math.floor((currAmount * 100.0) / goalAmount);
        }
    }
}
