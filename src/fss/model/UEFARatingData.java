package fss.model;

import java.io.*;
import java.util.HashMap;

public class UEFARatingData implements Serializable {
    public int year = 0;
    public ClubTeam team = null;
    public Country country = null;
    public double point = 0.0;
    private int teamID = -1;
    private int countryID = -1;

    public UEFARatingData(int year, Country country, double point) {
        this.country = country;
        countryID = country.getID();
        setInitData(year, point);
    }

    public UEFARatingData(int year, ClubTeam team, double point) {
        this.team = team;
        teamID = team.getID();
        setInitData(year, point);
    }

    public void RestoreClubCountry(HashMap<Integer, ClubTeam> teams, HashMap<Integer, Country> countries) {
        if(teams.containsKey(teamID)) {
            team = teams.get(teamID);
        }

        if(countries.containsKey(countryID)) {
            country = countries.get(countryID);
        }
    }

    private void setInitData(int year, double point) {
        this.year = year;
        this.point = point;
    }
}
