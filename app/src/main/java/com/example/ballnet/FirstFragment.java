package com.example.ballnet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ballnet.business.LoginManager;
import com.example.ballnet.business.UserInfo;
import com.example.ballnet.databinding.FragmentFirstBinding;
import com.example.ballnet.databinding.PieceTournamentBinding;
import com.example.ballnet.databinding.PieceUserinfoBinding;
import com.example.ballnet.services.HttpService;
import com.example.ballnet.tournament.TournamentService;
import com.example.ballnet.tournament.models.Tournament;
import com.example.ballnet.tournament.models.TournamentBundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    public static class TournamentsAdapter extends RecyclerView.Adapter<TournamentsAdapter.TournamentHolder> {

        private final List<TournamentBundle> bundles;
        private final NavController controller;
        private Context context;

        public TournamentsAdapter(Context context, List<TournamentBundle> tournamentBundles, NavController controller) {
            this.bundles = tournamentBundles;
            this.context = context;
            this.controller = controller;
        }

        @NonNull
        @Override
        public TournamentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.piece_tournament, parent, false);

            return new TournamentHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TournamentHolder holder, int position) {
            PieceTournamentBinding binding = PieceTournamentBinding.bind(holder.view);

            TournamentBundle tournamentBundle = bundles.get(position);

            Tournament tournament = tournamentBundle.getTournament();

            binding.nameLabel.setText(tournament.getName());
            binding.statusLabel.setText(tournament.getStatus().toPrettyString());
            binding.sportLabel.setText(tournamentBundle.getSportInfo().getSportName());
            binding.playersLabel.setText(String.format("%d/%d", tournament.getTeams().size(), tournament.getMaxTeamsNumber()));

            binding.getRoot().setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                TournamentBundle.store(bundle, tournamentBundle);
                controller.navigate(R.id.action_FirstFragment_to_tournamentFragment, bundle);
            });
        }

        @Override
        public int getItemCount() {
            return bundles.size();
        }

        static class TournamentHolder extends RecyclerView.ViewHolder {

            public final View view;

            public TournamentHolder(@NonNull View itemView) {
                super(itemView);

                view = itemView;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            if (arguments.containsKey("action")) {
                switch (arguments.getString("action")) {
                    case "show-tournament":

                }
            }
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        FragmentActivity activity = getActivity();

        if (LoginManager.getInstance().getToken() == null) {
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_loginFragment2);
        } else if (LoginManager.getInstance().getUser() == null) {

            if (activity != null) {
                new Thread(() -> {
                    try {
                        UserInfo userInfo = HttpService.getInstance().jsonGet("http://ball.net:8080/api/v1/user/me", UserInfo.class);
                        LoginManager.getInstance().setUser(userInfo);
                        System.out.println(userInfo.getDisplayName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    activity.runOnUiThread(() ->
                            NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(R.id.action_waitingFragment_to_FirstFragment));
                }).start();
            } else throw new RuntimeException("DF?");

            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_waitingFragment);
        } else {
//            Toast.makeText(getContext(), LoginManager.getInstance().getUser().getDisplayName(), Toast.LENGTH_SHORT).show();


            new Thread(() -> {
                try {
                    List<TournamentBundle> tournamentBundles = TournamentService.getInstance().myTournaments(5, 0);

                    activity.runOnUiThread(() -> {
                        binding.myTournaments.setAdapter(new TournamentsAdapter(activity, tournamentBundles, NavHostFragment.findNavController(FirstFragment.this)));
                        binding.myTournaments.setHasFixedSize(true);
                        binding.myTournaments.setLayoutManager(new LinearLayoutManager(activity));
                    });
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            /*if (activity != null) {

                ProgressDialog show = ProgressDialog.show(getContext(),
                        "Waiting",
                        "Required tournament loading",
                        true);

                new Thread(() -> {

                    try {

                        Bundle bundle = new Bundle();

                        TournamentBundle tournamentBundle =
                                TournamentService.getInstance().getCompleteTournament(1409L);

                        TournamentBundle.store(bundle, tournamentBundle);

                        show.dismiss();

                        activity.runOnUiThread(() ->
                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_FirstFragment_to_tournamentFragment, bundle));

                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

//                    Tournament tournament;
//
//                    try {
//
//                        tournament = TournamentService.getInstance().getTournament(828L);
//
//                        Long sportID = tournament.getSport();
//
//                        TournamentBundle tournamentBundle;
//                        tournamentBundle = new TournamentBundle(tournament);
//
//                        Thread usersThread = new Thread(() -> {
//                            try {
//                                Set<Long> userIDs = tournamentBundle.getUserIDs();
//                                List<UserInfo> users = UserService.getInstance().getUsers(userIDs);
//                                tournamentBundle.usersFound(users);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        });
//
//                        usersThread.start();
//
//                        Thread sportThread = new Thread(() -> {
//                            try {
//                                SportInfo sport = FacilityService.getInstance().getSportInfo(sportID);
//                                tournamentBundle.setSportInfo(sport);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        });
//
//                        sportThread.start();
//
//                        usersThread.join();
//                        sportThread.join();
//
//                        TournamentBundle.store(bundle, tournamentBundle);
//
////                        bundle.putSerializable("tournamentBundle", tournamentBundle);
//                    } catch (IOException | InterruptedException e) {
//                        e.printStackTrace();
//                    }


                }).start();
            } else throw new RuntimeException("DF?");*/

//            NavHostFragment.findNavController(FirstFragment.this)
//                    .navigate(R.id.action_FirstFragment_to_waitingFragment);
        }

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (LoginManager.getInstance().getUser() != null) {
            binding.textviewFirst.setText(String.format(
                    "Welcome, %s",
                    LoginManager.getInstance().getUser().getDisplayName())
            );
            binding.userBtn.setOnClickListener(v -> {
                NavHostFragment
                        .findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_userFragment);
            });
        } else {
            binding.userBtn.setEnabled(false);
        }

//        Thread thread = new Thread() {
//
//            @Override
//            public void run() {
//                super.run();
//
//                try {
////                    String s = HttpService.getInstance().plainGet("http://google.com");
//                    if (LoginManager.getInstance().getToken() != null) {
//                        UserInfo userInfo = HttpService.getInstance().jsonGet("http://ball.net:8080/api/v1/user/me", UserInfo.class);
//                        LoginManager.getInstance().setUser(userInfo);
//                        System.out.println(userInfo.getDisplayName());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        thread.start();

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
//
//        if (LoginManager.getInstance().getToken() != null){
//            AsyncTask<Void, Void, UserInfo> asyncTask = new AsyncTask<Void, Void, UserInfo>() {
//
//                @Override
//                protected UserInfo doInBackground(Void... voids) {
//                    try {
//                        UserInfo userInfo = HttpService.getInstance().jsonGet("http://ball.net:8080/user/me", UserInfo.class);
//                        return userInfo;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    return null;
//                }
//            };
//
//            try {
//                UserInfo userInfo = asyncTask.execute().get();
////                UserInfo userInfo = asyncTask.get();
//                Toast.makeText(getContext(), userInfo.getDisplayName(), Toast.LENGTH_SHORT).show();
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}