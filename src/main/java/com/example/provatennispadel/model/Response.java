package com.example.provatennispadel.model;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<Reservation> reserv = new ArrayList<Reservation>();

    public List<Reservation> getReserv() {
        return reserv;
    }

    public void setReserv(List<Reservation> res)
    {
        reserv= res;
    }

}
