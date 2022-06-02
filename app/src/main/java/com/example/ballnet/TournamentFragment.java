package com.example.ballnet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ballnet.business.LoginManager;
import com.example.ballnet.business.UserInfo;
import com.example.ballnet.databinding.FragmentTournamentBinding;
import com.example.ballnet.databinding.PieceUserinfoBinding;
import com.example.ballnet.tournament.TournamentService;
import com.example.ballnet.tournament.models.Tournament;
import com.example.ballnet.tournament.models.TournamentBundle;
import com.example.ballnet.users.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TournamentFragment extends Fragment {

    private FragmentTournamentBinding binding;

    public static class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.PlayerHolder> {

        private Context context;
        private List<UserInfo> players;

        public TeamAdapter(Context context) {
            this.players = new ArrayList<>();
            this.context = context;
        }

        public void addPlayer(UserInfo userInfo) {
//            new Handler(this.context.getMainLooper())
//                    .post(() -> {
                        players.add(userInfo);
                        notifyItemInserted(players.size() - 1);
//                    });
        }

        @NonNull
        @Override
        public PlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.piece_userinfo, parent, false);

            return new PlayerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
            PieceUserinfoBinding bind = PieceUserinfoBinding.bind(holder.view);

            UserInfo info = players.get(position);
            bind.displayName.setText(info.getDisplayName());
            bind.email.setText(info.getEmail());
        }

        @Override
        public int getItemCount() {
            return players.size();
        }

        public List<Long> getIDs() {
            ArrayList<Long> list = new ArrayList<>();
            for (UserInfo info : players)
                list.add(info.getId());
            return list;
        }

        static class PlayerHolder extends RecyclerView.ViewHolder {

            public final View view;

            public PlayerHolder(@NonNull View itemView) {
                super(itemView);

                view = itemView;
            }
        }
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.TextViewHolder> {

        private final Tournament tournament;
        private final Map<Long, UserInfo> users;

        private final int bgColor;

        public Adapter(Tournament tournament, Map<Long, UserInfo> users, Context context) {
            this.tournament = tournament;
            this.users = users;
            bgColor = context.getResources().getColor(R.color.teal_200);
        }

        @NonNull
        @Override
        public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextViewHolder textViewHolder = new TextViewHolder(new TextView(parent.getContext()));
            return textViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
            String teamString = tournament.getTeams().get(position).toPrettyString(users);

            holder.view.setText(String.format(" [%2d] %s", position + 1, teamString));
            holder.view.setPadding(10, 10, 10, 10);
            holder.view.setOnClickListener(v -> Toast.makeText(v.getContext(), teamString, Toast.LENGTH_SHORT).show());
        }

        @Override
        public int getItemCount() {
            return tournament.getTeams().size();
        }

        static class TextViewHolder extends RecyclerView.ViewHolder {

            public final TextView view;

            public TextViewHolder(@NonNull View itemView) {
                super(itemView);

                this.view = (TextView) itemView;
            }
        }
    }

    private void updateChangeStatusButton(String label, TournamentBundle tournamentBundle) {
        binding.changeState.setVisibility(View.VISIBLE);
        binding.changeState.setText(label);
        binding.changeState.setOnClickListener(v -> update(
                TournamentService.getInstance()::advanceStatus,
                tournamentBundle
        ));
    }

    private void hideChangeStatusButton() {
        binding.changeState.setVisibility(View.GONE);
    }

    private void updateView(@NonNull TournamentBundle tournamentBundle) {
        Tournament tournament = tournamentBundle.getTournament();

        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

            if (actionBar != null)
                actionBar.setTitle(String.format("Tournament: %s", tournament.getName()));
        }

        binding.tournamentStatusLabel.setText(tournament.getStatus().toPrettyString());
        binding.tournamentPriceLabel.setText(String.format(Locale.getDefault(),
                "€ %.02f",
                tournament.getPrice()));
        binding.tournamentPrizeLabel.setText(String.format(Locale.getDefault(),
                "€ %.02f",
                tournament.getPrize()));
        binding.tournamentDatesLabel.setText(tournamentBundle.getDatesLabel());
        binding.tournamentTeamsLabel.setText(String.format("%d/%d", tournament.getTeams().size(), tournament.getMaxTeamsNumber()));

        if (tournament.getDescription() != null) {
            binding.tournamentDescriptionTRow.setVisibility(View.VISIBLE);
            binding.tournamentDescriptionBRow.setVisibility(View.VISIBLE);
            binding.tournamentDescriptionLabel.setText(tournament.getDescription());
        }

        if (tournamentBundle.getUserIDs() != null) {
            Adapter adapter = new Adapter(tournament, tournamentBundle.getUsersMap(), getContext());

            RecyclerView recyclerView = binding.tournamentTeamsList;
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.GONE);

            SwitchListener listener = new SwitchListener(getContext(),
                    binding.tournamentTeamsBtn,
                    recyclerView);
            binding.tournamentTeamsTopRow.setOnClickListener(listener);
        }

        if (tournamentBundle.getSportInfo() != null)
            binding.tournamentSportLabel.setText(tournamentBundle.getSportInfo().getSportName());

        UserInfo user = LoginManager.getInstance().getUser();

        if (user != null) {
            binding.buttonsGroup.setVisibility(View.VISIBLE);

            switch (user.getType()) {
                case ADMIN:
                case TEACHER:

                    switch (tournament.getStatus()) {
                        case DRAFT:
                            updateChangeStatusButton("Confirm", tournamentBundle);
                            break;
                        case CONFIRMED:
                            if (tournamentBundle.getTournament().getTeams().size() >= 2)
                                updateChangeStatusButton("Complete", tournamentBundle);
                            else
                                hideChangeStatusButton();
                            break;
                        case COMPLETE:
                        case CANCELLED:
                        case DONE:
                            hideChangeStatusButton();
                            binding.changeState.setVisibility(View.GONE);
                            break;
                    }

                    if (tournament.getStatus() == Tournament.Status.DONE ||
                            tournament.getStatus() == Tournament.Status.CANCELLED)
                        binding.cancelButton.setVisibility(View.GONE);
                    else {
                        binding.cancelButton.setVisibility(View.VISIBLE);
                        binding.cancelButton.setOnClickListener(v ->
                                update(TournamentService.getInstance()::cancel,
                                        tournamentBundle));
                    }

                    if (tournament.getStatus() == Tournament.Status.CONFIRMED) {
                        binding.teamAction.setVisibility(View.VISIBLE);
                        binding.teamAction.setText("Add team");
                        binding.teamAction.setOnClickListener(v -> showAddTeam(tournamentBundle));
                    } else {
                        binding.teamAction.setVisibility(View.GONE);
                    }


                    break;
                case PLAYER:
                    binding.changeState.setVisibility(View.GONE);
                    binding.cancelButton.setVisibility(View.GONE);

                    if (tournament.getStatus() == Tournament.Status.CONFIRMED) {
                        binding.buttonsGroup.setVisibility(View.VISIBLE);
                    } else {
                        binding.buttonsGroup.setVisibility(View.GONE);
                    }

                    break;
            }

        } else {
            binding.buttonsGroup.setVisibility(View.GONE);
        }
    }

    private void updateView(Bundle arguments) {
        if (arguments == null)
            return;

        TournamentBundle tournamentBundle = TournamentBundle.retrieve(arguments);

        if (tournamentBundle != null)
            updateView(tournamentBundle);

    }

    private void showAddTeam(TournamentBundle tournamentBundle) {

        AutoCompleteTextView teamMember = binding.teamMember;

        SuggestionAdapter adapter = new SuggestionAdapter(getContext());
        TeamAdapter teamAdapter = new TeamAdapter(getContext());

        teamMember.setAdapter(adapter);
        teamMember.addTextChangedListener(adapter.getWatcher());
        teamMember.setThreshold(1);
        teamMember.setOnItemClickListener((parent, view, position, id) -> {
            UserInfo item = adapter.getItem(position);
            teamAdapter.addPlayer(item);

            if (teamAdapter.getItemCount() == tournamentBundle.getSportInfo().getPlayerPerTeam()) {
                binding.addTeam.setVisibility(View.VISIBLE);
                teamMember.setVisibility(View.GONE);
            } else
                teamMember.setText("");
        });


        binding.addTeam.setOnClickListener(v -> {
            binding.addTeamPane.setVisibility(View.GONE);
            update(TournamentService.getInstance()::addTeam,
                    tournamentBundle,
                    teamAdapter.getIDs());
        });
        binding.addTeam.setVisibility(View.INVISIBLE);
        binding.cancelTeamAddition.setOnClickListener(v ->
                binding.addTeamPane.setVisibility(View.GONE));
        binding.teamPlayers.setAdapter(teamAdapter);
        binding.teamPlayers.setHasFixedSize(false);
        binding.teamPlayers.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.addTeamPane.setVisibility(View.VISIBLE);
    }

    private static class SuggestionAdapter extends BaseAdapter implements Filterable {

        private final List<UserInfo> list;
        private final LayoutInflater inflater;

        private final Context context;

        private AtomicInteger c;

        public SuggestionAdapter(Context context) {
            list = Collections.synchronizedList(new ArrayList<>());
            inflater = LayoutInflater.from(context);
            this.context = context;
            c = new AtomicInteger(0);
        }

        public void setSuggestions(Collection<UserInfo> suggestions) {
            new Handler(context.getMainLooper()).post(() -> {
//                notifyDataSetInvalidated();
                list.clear();
                list.addAll(suggestions);
                notifyDataSetChanged();
            });
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public UserInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.piece_userinfo,
                        parent,
                        false);

            PieceUserinfoBinding bind = PieceUserinfoBinding.bind(convertView);
            UserInfo item = getItem(position);

            bind.displayName.setText(item.getDisplayName());
            bind.email.setText(item.getEmail());

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new NoFilterFilter();
        }

        public TextWatcher getWatcher() {
            return new Watcher();
        }

        public class NoFilterFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                filterResults.count = 1;
                filterResults.values = Collections.singletonList(list.get(0));

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        }

        private class Watcher implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = c.incrementAndGet();

                new Thread(() -> {
                    try {
                        List<UserInfo> suggestions = UserService.getInstance().getSuggestions(
                                s.toString(),
                                3);
                        int i1 = c.get();
                        if (i == i1) {
                            setSuggestions(suggestions);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTournamentBinding.inflate(inflater, container, false);

        binding.addTeamPane.setVisibility(View.GONE);

        Bundle arguments = getArguments();
        updateView(arguments);

        return binding.getRoot();
    }

    private void update(UpdateFunction updateFunction, TournamentBundle tournamentBundle) {
        ProgressDialog show = ProgressDialog.show(getContext(),
                "Loading",
                "Requested action in progress",
                true);

        new Thread(() -> {

            try {
                TournamentBundle res = updateFunction.update(tournamentBundle);

                FragmentActivity activity = getActivity();

                if (activity != null)
                    activity.runOnUiThread(() -> updateView(res));

            } catch (IOException e) {
                e.printStackTrace();
            }

            show.dismiss();
        }).start();
    }

    private <T> void update(ParamsUpdateFunction<T> updateFunction,
                            TournamentBundle tournamentBundle,
                            T params) {

        ProgressDialog show = ProgressDialog.show(getContext(),
                "Loading",
                "Requested action in progress",
                true);

        new Thread(() -> {

            try {
                TournamentBundle res = updateFunction.update(tournamentBundle, params);

                FragmentActivity activity = getActivity();

                if (activity != null)
                    activity.runOnUiThread(() -> updateView(res));

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            show.dismiss();
        }).start();
    }

    private interface UpdateFunction {

        TournamentBundle update(TournamentBundle tournamentBundle) throws IOException;
    }

    private interface ParamsUpdateFunction<T> {

        TournamentBundle update(TournamentBundle tournamentBundle, T t) throws IOException, InterruptedException;
    }

    public static class SwitchListener implements View.OnClickListener {

        private final Drawable up;
        private final Drawable down;

        private final ImageView btn;
        private final View target;

        private boolean shown;

        public SwitchListener(Context context, ImageView btn, View target) {
            this.down = ContextCompat.getDrawable(context, android.R.drawable.arrow_down_float);
            this.up = ContextCompat.getDrawable(context, android.R.drawable.arrow_up_float);
            this.shown = false;
            this.btn = btn;
            this.target = target;
        }

        @Override
        public void onClick(View v) {
            if (shown) {
                target.setVisibility(View.GONE);
                btn.setImageDrawable(down);
                shown = false;
            } else {
                target.setVisibility(View.VISIBLE);
                btn.setImageDrawable(up);
                shown = true;
            }
        }
    }
}