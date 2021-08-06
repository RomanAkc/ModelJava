package fss.model;

import java.util.ArrayList;

public class TestRating implements Rating {
    private ArrayList<SimpleTeam> teams;

    //public TestRating() {}

    public TestRating(ArrayList<SimpleTeam> teams) {
        this.teams = teams;
    }

    public void addTeam(SimpleTeam team) {
        teams.add(team);
    }

    public int getTeamPosition(SimpleTeam team) {
        return teams.lastIndexOf(team);
    }
}
