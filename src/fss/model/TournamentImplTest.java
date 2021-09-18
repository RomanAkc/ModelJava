package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TournamentImplTest {
    @Test
    public void calcCountryChampionship() {
        var tournament = new TournamentImpl("Italy championship");
        tournament.addScheme(createScheme());
        tournament.addTeamsToStage(1, createClubs());
        tournament.addWinRules(createWinRules());

        tournament.calc();

        Assert.assertEquals(true, true);
    }

    ArrayList<Table.WinRules> createWinRules() {
        var winRules = new ArrayList<Table.WinRules>();
        winRules.add(Table.WinRules.BY_MEET);
        winRules.add(Table.WinRules.BY_GOAL_AWAY_MEET);
        winRules.add(Table.WinRules.BY_DIFFERENCE_GOAL);
        winRules.add(Table.WinRules.BY_GOAL_FOR);
        winRules.add(Table.WinRules.BY_COUNT_WIN);
        return winRules;
    }

    private Scheme createScheme() {
        var teamSource = new TeamsSource();
        teamSource.source = SchemePart.Source.FROM_OUT;

        var part = new SchemePart(1, "Championship", 2, StagePool.StageType.CIRCLE);
        part.teamSources.add(teamSource);

        var scheme = new Scheme();
        scheme.AddPart(part);

        return scheme;
    }

    ArrayList<SimpleTeam> createClubs() {
        ArrayList<SimpleTeam> teams = new ArrayList<>();
        teams.add(new ClubTeam(1, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 28, 28));
        teams.add(new ClubTeam(2, "Roma", "Italy", SimpleTeam.WorldPart.EUROPE, 26, 26, 26));
        teams.add(new ClubTeam(3, "Milan", "Italy", SimpleTeam.WorldPart.EUROPE, 27, 27, 27));
        teams.add(new ClubTeam(4, "Napoli", "Italy", SimpleTeam.WorldPart.EUROPE, 26, 26, 26));
        teams.add(new ClubTeam(5, "Udineze", "Italy", SimpleTeam.WorldPart.EUROPE, 25, 25, 25));
        teams.add(new ClubTeam(6, "Bologna", "Italy", SimpleTeam.WorldPart.EUROPE, 24, 24, 24));
        teams.add(new ClubTeam(7, "Lazio", "Italy", SimpleTeam.WorldPart.EUROPE, 25, 25, 25));
        teams.add(new ClubTeam(8, "Torino", "Italy", SimpleTeam.WorldPart.EUROPE, 22, 22, 22));
        teams.add(new ClubTeam(9, "Fiorentina", "Italy", SimpleTeam.WorldPart.EUROPE, 27, 27, 27));
        teams.add(new ClubTeam(10, "Atalanta", "Italy", SimpleTeam.WorldPart.EUROPE, 24, 24, 24));
        teams.add(new ClubTeam(11, "Sassuolo", "Italy", SimpleTeam.WorldPart.EUROPE, 21, 21, 21));
        teams.add(new ClubTeam(12, "Empoli", "Italy", SimpleTeam.WorldPart.EUROPE, 21, 21, 21));
        teams.add(new ClubTeam(13, "Genoa", "Italy", SimpleTeam.WorldPart.EUROPE, 22, 22, 22));
        teams.add(new ClubTeam(14, "Venezia", "Italy", SimpleTeam.WorldPart.EUROPE, 21, 21, 21));
        teams.add(new ClubTeam(15, "Sampdoria", "Italy", SimpleTeam.WorldPart.EUROPE, 25, 25, 25));
        teams.add(new ClubTeam(16, "Juventus", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 28, 28));
        teams.add(new ClubTeam(17, "Cagliari", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 28, 28));
        teams.add(new ClubTeam(18, "Spezia", "Italy", SimpleTeam.WorldPart.EUROPE, 20, 20, 20));
        teams.add(new ClubTeam(19, "Hellas Verona", "Italy", SimpleTeam.WorldPart.EUROPE, 23, 23, 23));
        teams.add(new ClubTeam(20, "Salernitana", "Italy", SimpleTeam.WorldPart.EUROPE, 19, 19, 19));
        return teams;
    }

    @Test
    public void calcContinentClubTournament() {

    }

    @Test
    public void calcWorldCup() {

    }

}