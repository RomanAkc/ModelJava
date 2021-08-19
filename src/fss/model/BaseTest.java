package fss.model;

import java.util.ArrayList;

public class BaseTest {
    protected ArrayList<SimpleTeam> generateTeams(int cnt) {
        var teams = new ArrayList<SimpleTeam>();
        for(int i = 0; i < cnt; ++i) {
            teams.add(new ClubTeam(i, String.format("%d", i), String.format("%d", i), SimpleTeam.WorldPart.EUROPE, 30, 30, 30));
        }
        return teams;
    }
}
