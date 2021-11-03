package fss.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

    protected ArrayList<SimpleTeam> generateTeamsWithPower(int cnt, int power) {
        var country = generateCounty();
        var teams = new ArrayList<SimpleTeam>();
        for(int i = 0; i < cnt; ++i) {
            teams.add(new ClubTeam(i, String.format("Team %d", i), country, power, power, power));
        }
        return teams;
    }

    protected Country generateCounty() {
        return generateCountryWithID(1);
    }

    protected HashMap<ClubTeam, Country> generateClubTeams(int cntCountries, int cntClubForCountry) {
        var result = new HashMap<ClubTeam, Country>();

        int teamID = 1;
        for(int i = 0; i < cntCountries; ++i) {
            var country = generateCountryWithID(i);
            for(int j = 0; j < cntClubForCountry; ++j) {
                teamID += 1;
                var team = new ClubTeam(teamID, String.format("Team %d", teamID), country, 30);
                result.put(team, country);
            }
        }

        return result;
    }

    public Country generateCountryWithID(int id) {
        return new Country(id, "Country" + Integer.toString(id), WorldPart.EUROPE);
    }
}
