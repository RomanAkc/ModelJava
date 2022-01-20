package fss.model;

import java.util.ArrayList;

public class RatingByTeamOrder implements Ratingable {
    private final ArrayList<SimpleTeam> teams;

    public RatingByTeamOrder(ArrayList<SimpleTeam> teams) {
        this.teams = teams;
    }

    public int getTeamPosition(SimpleTeam team) {
        return teams.lastIndexOf(team);
    }
}
