package com.example.ballnet.tournament;

import com.example.ballnet.business.UserInfo;
import com.example.ballnet.facility.FacilityService;
import com.example.ballnet.facility.models.SportInfo;
import com.example.ballnet.services.HttpService;
import com.example.ballnet.tournament.models.Tournament;
import com.example.ballnet.tournament.models.TournamentBundle;
import com.example.ballnet.users.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TournamentService {

    private static final TournamentService INSTANCE = new TournamentService();

    public static TournamentService getInstance() {
        return INSTANCE;
    }

    private final HttpService httpService = HttpService.getInstance();

    private TournamentService() {
    }

    private <T> T tournamentCall(String method, Long id, Class<T> res) throws IOException {
        String url = String.format("http://ball.net:8080/api/v1/tournaments/%s", method);

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        return httpService.jsonGet(url, map, res);
    }

    private Tournament tournamentCall(String method, Long id) throws IOException {
        return tournamentCall(method, id, Tournament.class);
    }

    public Tournament getTournament(Long tournamentID) throws IOException {
        Tournament tournament = tournamentCall("get", tournamentID, Tournament.class);

        return tournament;
    }

    public TournamentBundle advanceStatus(TournamentBundle tournamentBundle) throws IOException {

        Long id = tournamentBundle.getTournament().getId();
        Tournament tournament;

        switch (tournamentBundle.getTournament().getStatus()) {
            case DRAFT:
                tournament = tournamentCall("confirm", id);
                break;
            case CONFIRMED:
                tournament = tournamentCall("close-registrations", id);
                break;
            default:
                tournament = null;
        }

        return new TournamentBundle(tournament, tournamentBundle);
    }

    public TournamentBundle cancel(TournamentBundle tournamentBundle) throws IOException {
        Tournament tournament = tournamentCall("cancel", tournamentBundle.getTournament().getId());
        return new TournamentBundle(tournament, tournamentBundle);
    }

    public TournamentBundle addTeam(TournamentBundle tournamentBundle, List<Long> playersIDs)
            throws IOException, InterruptedException {
        //http://ball.net:8080/api/v1/tournaments/register-players?id=1409&players=5

        HashMap<String, Object> params = new HashMap<>();

        params.put("id", tournamentBundle.getTournament().getId());
        params.put("players", playersIDs);

        Tournament tournament = httpService.jsonGet(
                "http://ball.net:8080/api/v1/tournaments/register-players",
                params,
                Tournament.class
        );

        return completeTournament(tournament);
    }

    public List<TournamentBundle> myTournaments(int limit, int offset)
            throws IOException, InterruptedException {
        HashMap<String, Object> params = new HashMap<>();

        params.put("limit", limit);
        params.put("offset", offset);

        List<Tournament> tournaments = httpService.jsonGetList(
                "http://ball.net:8080/api/v1/tournaments/my-tournaments",
                params,
                Tournament.class
        );

        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<TournamentBundle> tournamentBundles = new ArrayList<>();

        for (Tournament tournament : tournaments) {
            TournamentBundle tournamentBundle = new TournamentBundle(tournament);
            List<Thread> _threads = completeTournament(tournamentBundle);
            threads.addAll(_threads);
            tournamentBundles.add(tournamentBundle);
        }

        for (Thread thread : threads)
            thread.join();

        return tournamentBundles;
    }

    private List<Thread> completeTournament(TournamentBundle tournamentBundle) {

        Long sportID = tournamentBundle.getTournament().getSport();
        Thread usersThread = new Thread(() -> {
            try {
                Set<Long> userIDs = tournamentBundle.getUserIDs();
                List<UserInfo> users = UserService.getInstance().getUsers(userIDs);
                tournamentBundle.usersFound(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        usersThread.start();

        Thread sportThread = new Thread(() -> {
            try {
                SportInfo sport = FacilityService.getInstance().getSportInfo(sportID);
                tournamentBundle.setSportInfo(sport);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sportThread.start();

        return Arrays.asList(sportThread, usersThread);
    }

    private TournamentBundle completeTournament(Tournament tournament) throws InterruptedException {

        TournamentBundle tournamentBundle;
        tournamentBundle = new TournamentBundle(tournament);

        for (Thread thread : completeTournament(tournamentBundle))
            thread.join();

//        Long sportID = tournament.getSport();
//        Thread usersThread = new Thread(() -> {
//            try {
//                Set<Long> userIDs = tournamentBundle.getUserIDs();
//                List<UserInfo> users = UserService.getInstance().getUsers(userIDs);
//                tournamentBundle.usersFound(users);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        usersThread.start();
//
//        Thread sportThread = new Thread(() -> {
//            try {
//                SportInfo sport = FacilityService.getInstance().getSportInfo(sportID);
//                tournamentBundle.setSportInfo(sport);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        sportThread.start();
//
//        usersThread.join();
//        sportThread.join();

        return tournamentBundle;
    }

    public TournamentBundle getCompleteTournament(Long id)
            throws IOException, InterruptedException {

        Tournament tournament;

        tournament = TournamentService.getInstance().getTournament(id);

        return completeTournament(tournament);
    }
}
