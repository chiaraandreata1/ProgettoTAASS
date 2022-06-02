package com.example.ballnet.tournament.models;

import com.example.ballnet.business.UserInfo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Team implements Serializable {

    private static final long serialVersionUID = 42L;

    private List<Long> players;

    public Team() {
    }

    public Team(Long... players) {
        this(Arrays.asList(players));
    }

    public Team(List<Long> players) {
        this.players = players;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public void setPlayers(List<Long> players) {
        this.players = players;
    }

    public String toPrettyString(Map<Long, UserInfo> users) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Long userID : players) {
            if (stringBuilder.length() > 0)
                stringBuilder.append(" - ");
            UserInfo userInfo = users.get(userID);
            if (userInfo != null)
                stringBuilder.append(userInfo.getDisplayName());
            else
                stringBuilder.append("Unknown");
        }
        return stringBuilder.toString();
    }
}
