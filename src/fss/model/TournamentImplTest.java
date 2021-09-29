package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TournamentImplTest {
    private static int CHAMPIONSHIP_STAGE_ID                = 1;
    private static int FIRST_EUROCUP_QUALIFICATION          = 2;
    private static int SECOND_EUROCUP_QUALIFICATION         = 3;
    private static int EUROCUP_GROUP                        = 4;
    private static int EUROCUP_1_8                          = 5;
    private static int EUROCUP_1_4                          = 6;
    private static int EUROCUP_1_2                          = 7;
    private static int EUROCUP_FINAL                        = 8;

    @Test
    public void calcCountryChampionship() {
        var tournament = new TournamentImpl("Italy championship");
        tournament.addScheme(createSchemeChampionship());
        tournament.addTeamsToStage(CHAMPIONSHIP_STAGE_ID, createClubsForChampionship());
        tournament.addWinRules(createWinRules());

        tournament.calc();
        Assert.assertEquals(tournament.getCntStagePool(), 1);

        var rows = tournament.getFinalTableRows(CHAMPIONSHIP_STAGE_ID);
        Assert.assertFalse(rows == null);

        checkFinalTable(rows);
    }

    private void checkFinalTable(ArrayList<Table.Row> rows) {
        int prevPoint = Integer.MAX_VALUE;
        int sumFor = 0;
        int sumAgainst = 0;
        for(var row : rows) {
            Assert.assertTrue(row.point <= prevPoint);
            Assert.assertEquals(row.point, row.win * 3 + row.draw);
            sumFor += row.goalFor;
            sumAgainst += row.goalAgainst;
        }
        Assert.assertEquals(sumFor, sumAgainst);
    }

    private ArrayList<Table.WinRules> createWinRules() {
        var winRules = new ArrayList<Table.WinRules>();
        winRules.add(Table.WinRules.BY_MEET);
        winRules.add(Table.WinRules.BY_GOAL_AWAY_MEET);
        winRules.add(Table.WinRules.BY_DIFFERENCE_GOAL);
        winRules.add(Table.WinRules.BY_GOAL_FOR);
        winRules.add(Table.WinRules.BY_COUNT_WIN);
        return winRules;
    }

    private Scheme createSchemeChampionship() {
        var scheme = new Scheme();

        var part = new SchemePart(CHAMPIONSHIP_STAGE_ID, "Championship", 2, BaseStagePool.StageType.CIRCLE);
        part.teamSources.add(new TeamsSource());
        scheme.AddPart(part);

        return scheme;
    }

    private ArrayList<SimpleTeam> createClubsForChampionship() {
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

    private Scheme createSchemeContinentalClubTournament() {
        var scheme = new Scheme();

        var firstQual = new SchemePart(FIRST_EUROCUP_QUALIFICATION, "First Qual", 2, BaseStagePool.StageType.PLAYOFF);
        firstQual.teamSources.add(new TeamsSource());
        scheme.AddPart(firstQual);


        return scheme;
    }

    @Test
    public void calcWorldCup() {

    }

}