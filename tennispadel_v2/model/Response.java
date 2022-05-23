package com.example.tennispadel_v2.model;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<Reservation> reserv = new ArrayList<Reservation>();

    private int retCode ;

    public List<Reservation> getReserv() {
        return reserv;
    }

    public void setReserv(List<Reservation> res)
    {
        reserv= res;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
}
