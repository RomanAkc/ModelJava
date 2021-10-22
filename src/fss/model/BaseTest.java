package fss.model;

import java.util.ArrayList;

//Base test class
public class BaseTest {
    protected ArrayList<SimpleTeam> generateTeams(int cnt) {
        var country = generateCounty();
        var teams = new ArrayList<SimpleTeam>();
        for(int i = 0; i < cnt; ++i) {
            teams.add(new ClubTeam(i, String.format("Team %d", i), country, 30, 30, 30));
        }
        return teams;
    }

    protected Country generateCounty() {
        return new Country(1, "Country", WorldPart.EUROPE);
    }
}
