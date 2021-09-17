package fss.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TournamentImplTest {
    @Test
    public void calcCountryChampionship() {
        var teams = createClubs();
        
        var scheme = new Scheme();
        var tournament = new TournamentImpl("Italy championship");

    }

    ArrayList<ClubTeam> createClubs() {
        ArrayList<ClubTeam> teams = new ArrayList<>();
        teams.add(new ClubTeam(1, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(2, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(3, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(4, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(5, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(6, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(7, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(8, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(9, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(10, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(11, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(12, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(13, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(14, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(15, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(16, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(17, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(18, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(19, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        teams.add(new ClubTeam(20, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 26, 30));
        return teams;
    }

    @Test
    public void calcContinentClubTournament() {

    }

    @Test
    public void calcWorldCup() {

    }

}