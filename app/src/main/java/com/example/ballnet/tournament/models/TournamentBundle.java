package com.example.ballnet.tournament.models;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.ballnet.business.UserInfo;
import com.example.ballnet.facility.models.SportInfo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TournamentBundle implements Serializable {

    private static final long serialVersionUID = 42L;

    private static final String BUNDLE_KEY = "_tournament_bundle_obj_";

    private static final String pattern = "dd/MM/yyyy";
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

    private Tournament tournament;
    private Map<Long, UserInfo> usersMap;
    private SportInfo sportInfo;

    public TournamentBundle(Tournament tournament) {
        this.tournament = tournament;
    }

    public TournamentBundle(Tournament tournament, TournamentBundle tournamentBundle) {
        this.tournament = tournament;
        this.sportInfo = tournamentBundle.sportInfo;
        this.usersMap = tournamentBundle.usersMap;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Map<Long, UserInfo> getUsersMap() {
        return usersMap;
    }

    public void setUsersMap(Map<Long, UserInfo> usersMap) {
        this.usersMap = usersMap;
    }

    public SportInfo getSportInfo() {
        return sportInfo;
    }

    public void setSportInfo(SportInfo sportInfo) {
        this.sportInfo = sportInfo;
    }

    public Set<Long> getUserIDs() {
        HashSet<Long> playerIDs = new HashSet<>();

        for (Team team : tournament.getTeams())
            playerIDs.addAll(team.getPlayers());

        return playerIDs;
    }

    public void usersFound(Collection<UserInfo> users) {
        Map<Long, UserInfo> map = new HashMap<>();

        for (UserInfo _user : users)
            map.put(_user.getId(), _user);

        usersMap = map;
    }

    public String getDatesLabel() {
        Set<Date> datesSet = new HashSet<>();

        for (Round round : tournament.getRounds())
            for (Match match : round.getMatches()) {
                try {
                    datesSet.add(dateFormat.parse(match.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        List<Date> datesList = new ArrayList<>(datesSet);
        Collections.sort(datesList);

        StringBuilder datesBuilder = new StringBuilder();

        for (Date date : datesList) {
            if (datesBuilder.length() > 0)
                datesBuilder.append('\n');
            datesBuilder.append(dateFormat.format(date));
        }

        return datesBuilder.toString();
    }

    public static void store(Bundle bundle, TournamentBundle tournamentBundle) {
        bundle.putSerializable(BUNDLE_KEY, tournamentBundle);
    }

    public static TournamentBundle retrieve(Bundle bundle) {
        return (TournamentBundle) bundle.getSerializable(BUNDLE_KEY);
    }
}
