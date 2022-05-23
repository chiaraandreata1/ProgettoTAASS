package com.example.tennispadel_v2.model;

import java.util.ArrayList;
import java.util.List;

public class FreeCourtsResponse {
    private List<FreeCourt> freecourts = new ArrayList<FreeCourt>();

    private int retCode ;

    public List<FreeCourt> getFreecourts() {
        return freecourts;
    }

    public void setFreecourts(List<FreeCourt> res)
    {
        freecourts= res;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
}
