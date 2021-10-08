package fss.model;

import java.util.ArrayList;

public class RatingByTeamOrder implements Rating {
    private ArrayList<SimpleTeam> teams;

    public RatingByTeamOrder(ArrayList<SimpleTeam> teams) {
        this.teams = teams;
    }

    public int getTeamPosition(SimpleTeam team) {
        return teams.lastIndexOf(team);
    }
}
