package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

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
        tournament.addTeamsToStage(CHAMPIONSHIP_STAGE_ID, createClubsChampionship());
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

    private ArrayList<SimpleTeam> createClubsChampionship() {
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
        var tournament = new TournamentImpl("League champions");
        tournament.addScheme(createSchemeContinentalClubTournament());

        tournament.addTeamsToStage(FIRST_EUROCUP_QUALIFICATION, createClubsLC1stQual());
        tournament.addTeamsToStage(SECOND_EUROCUP_QUALIFICATION, createClubsLC2stQual());
        tournament.addTeamsToStage(EUROCUP_GROUP, createClubsLCGroup());

        var rating = createRatingContinentalClubTournament(tournament.getAllTournamentTeams());
        tournament.addRating(rating);
        tournament.addWinRules(createWinRules());

        tournament.calc();

        Assert.assertEquals(tournament.getCntStagePool(), 7);
        Assert.assertEquals(tournament.getWinnersStageTeams(FIRST_EUROCUP_QUALIFICATION).size(), 8);
        Assert.assertEquals(tournament.getWinnersStageTeams(SECOND_EUROCUP_QUALIFICATION).size(), 8);
        Assert.assertEquals(tournament.getNTeamStageTeams(EUROCUP_GROUP, 1).size(), 8);
        Assert.assertEquals(tournament.getNTeamStageTeams(EUROCUP_GROUP, 2).size(), 8);
        Assert.assertEquals(tournament.getWinnersStageTeams(EUROCUP_1_8).size(), 8);
        Assert.assertEquals(tournament.getWinnersStageTeams(EUROCUP_1_4).size(), 4);
        Assert.assertEquals(tournament.getWinnersStageTeams(EUROCUP_1_2).size(), 2);
        Assert.assertEquals(tournament.getWinnersStageTeams(EUROCUP_FINAL).size(), 1);

        checkPlayOffStageWithRating(tournament, FIRST_EUROCUP_QUALIFICATION, rating);
        checkPlayOffStageWithRating(tournament, SECOND_EUROCUP_QUALIFICATION, rating);

        //Проверить использование рейтнга для группировки (?)

        System.out.println(tournament);
    }

    private class CompareTeams implements Comparator<SimpleTeam> {
        Rating rating = null;

        public CompareTeams(Rating rating) {
            this.rating = rating;
        }

        public int compare(SimpleTeam t1, SimpleTeam t2) {
            var pos1 = rating.getTeamPosition(t1);
            var pos2 = rating.getTeamPosition(t2);
            return pos1 > pos2 ? 1 : (pos1 == pos2 ? 0 : -1);
        }
    }

    private void checkPlayOffStageWithRating(Tournament tournament, int stageID, Rating rating) {
        var winners = tournament.getWinnersStageTeams(stageID);
        var losers = tournament.getLosersStageTeams(stageID);

        var allTeams = new ArrayList<SimpleTeam>(winners);
        allTeams.addAll(losers);
        Collections.sort(allTeams, new CompareTeams(rating));

        var posByTeams = new HashMap<SimpleTeam, Integer>();
        for(int i = 0; i < allTeams.size(); ++i) {
            posByTeams.put(allTeams.get(i), i);
        }

        var meetings = tournament.getStageMeetings(stageID);
        for(var meet : meetings) {
            var posHome = posByTeams.get(meet.getTeamHome());
            var posAway = posByTeams.get(meet.getTeamAway());

            boolean isInTopHome = posHome < allTeams.size() / 2;
            boolean isInTopAway = posAway < allTeams.size() / 2;

            Assert.assertNotEquals(isInTopHome, isInTopAway);
        }


    }

    private Scheme createSchemeContinentalClubTournament() {
        var scheme = new Scheme();

        var firstQual = new SchemePart(FIRST_EUROCUP_QUALIFICATION, "First Qual", 2, BaseStagePool.StageType.PLAYOFF);
        firstQual.teamSources.add(new TeamsSource());
        scheme.AddPart(firstQual);

        var secondQual = new SchemePart(SECOND_EUROCUP_QUALIFICATION, "Second Qual", 2, BaseStagePool.StageType.PLAYOFF);
        secondQual.teamSources.add(new TeamsSource());
        secondQual.teamSources.add(new TeamsSource(FIRST_EUROCUP_QUALIFICATION, TypeSource.WINNERS));
        scheme.AddPart(secondQual);

        var group = new SchemePart(EUROCUP_GROUP, "Group", 2, 8);
        group.teamSources.add(new TeamsSource());
        group.teamSources.add(new TeamsSource(SECOND_EUROCUP_QUALIFICATION, TypeSource.WINNERS));
        scheme.AddPart(group);

        var playOff18 = new SchemePart(EUROCUP_1_8, "1/8", 2, BaseStagePool.StageType.PLAYOFF);
        playOff18.teamSources.add(new TeamsSource(EUROCUP_GROUP, TypeSource.N_TEAM, 1));
        playOff18.teamSources.add(new TeamsSource(EUROCUP_GROUP, TypeSource.N_TEAM, 2));
        playOff18.ratingType = RatingType.BY_ORDER;
        scheme.AddPart(playOff18);

        var playOff14 = new SchemePart(EUROCUP_1_4, "1/4", 2, BaseStagePool.StageType.PLAYOFF);
        playOff14.teamSources.add(new TeamsSource(EUROCUP_1_8, TypeSource.WINNERS));
        playOff14.ratingType = RatingType.NO;
        scheme.AddPart(playOff14);

        var playOff12 = new SchemePart(EUROCUP_1_2, "1/2", 2, BaseStagePool.StageType.PLAYOFF);
        playOff12.teamSources.add(new TeamsSource(EUROCUP_1_4, TypeSource.WINNERS));
        playOff12.ratingType = RatingType.NO;
        scheme.AddPart(playOff12);

        var playOffFinal = new SchemePart(EUROCUP_FINAL, "Final", 1, BaseStagePool.StageType.PLAYOFF);
        playOffFinal.teamSources.add(new TeamsSource(EUROCUP_1_2, TypeSource.WINNERS));
        playOffFinal.ratingType = RatingType.NO;
        scheme.AddPart(playOffFinal);

        return scheme;
    }

    private ArrayList<SimpleTeam> createClubsLC1stQual() {
        var teams = new ArrayList<SimpleTeam>();

        teams.add(new ClubTeam(33, "Olympiakos", "Greece", SimpleTeam.WorldPart.EUROPE, 20));
        teams.add(new ClubTeam(34, "Fenerbahce", "Tukey", SimpleTeam.WorldPart.EUROPE, 23));
        teams.add(new ClubTeam(35, "Viktoria", "Czech", SimpleTeam.WorldPart.EUROPE, 19));
        teams.add(new ClubTeam(36, "AEK", "Cyprus", SimpleTeam.WorldPart.EUROPE, 16));
        teams.add(new ClubTeam(37, "Osijec", "Croatia", SimpleTeam.WorldPart.EUROPE, 15));
        teams.add(new ClubTeam(38, "Basel", "Switzerland", SimpleTeam.WorldPart.EUROPE, 18));
        teams.add(new ClubTeam(39, "Antwerp", "Belgium", SimpleTeam.WorldPart.EUROPE, 19));
        teams.add(new ClubTeam(40, "Partizan", "Serbia", SimpleTeam.WorldPart.EUROPE, 17));
        teams.add(new ClubTeam(41, "Shakhtar Donetsk", "Ukraine", SimpleTeam.WorldPart.EUROPE, 22));
        teams.add(new ClubTeam(42, "Spartak Moscow", "Russia", SimpleTeam.WorldPart.EUROPE, 22));
        teams.add(new ClubTeam(43, "Celtic", "Scotland", SimpleTeam.WorldPart.EUROPE, 23));
        teams.add(new ClubTeam(44, "Rapid Vienna", "Austria", SimpleTeam.WorldPart.EUROPE, 19));
        teams.add(new ClubTeam(45, "AZ", "Netherlands", SimpleTeam.WorldPart.EUROPE, 21));
        teams.add(new ClubTeam(46, "Sporting Braga", "Portugal", SimpleTeam.WorldPart.EUROPE, 23));
        teams.add(new ClubTeam(47, "Lyon", "France", SimpleTeam.WorldPart.EUROPE, 26));
        teams.add(new ClubTeam(48, "Wolfsburg", "Germany", SimpleTeam.WorldPart.EUROPE, 25));

        return teams;
    }

    private ArrayList<SimpleTeam> createClubsLC2stQual() {
        var teams = new ArrayList<SimpleTeam>();

        teams.add(new ClubTeam(25, "Appolon", "Cyprus", SimpleTeam.WorldPart.EUROPE, 16));
        teams.add(new ClubTeam(26, "PSV", "Netherlands", SimpleTeam.WorldPart.EUROPE, 22));
        teams.add(new ClubTeam(27, "Benfica", "Portugal", SimpleTeam.WorldPart.EUROPE, 24));
        teams.add(new ClubTeam(28, "Monaco", "France", SimpleTeam.WorldPart.EUROPE, 27));
        teams.add(new ClubTeam(29, "Borussia Dortmund", "Germany", SimpleTeam.WorldPart.EUROPE, 27));
        teams.add(new ClubTeam(30, "Juventus", "Italy", SimpleTeam.WorldPart.EUROPE, 28));
        teams.add(new ClubTeam(31, "Sevilla", "Spain", SimpleTeam.WorldPart.EUROPE, 26));
        teams.add(new ClubTeam(32, "Chelsea", "England", SimpleTeam.WorldPart.EUROPE, 28));

        return teams;
    }

    private ArrayList<SimpleTeam> createClubsLCGroup() {
        var teams = new ArrayList<SimpleTeam>();

        teams.add(new ClubTeam(1, "Dinamo Zagbeb", "Cyprus", SimpleTeam.WorldPart.EUROPE, 18));
        teams.add(new ClubTeam(2, "Young Boys", "Switzerland", SimpleTeam.WorldPart.EUROPE, 22));
        teams.add(new ClubTeam(3, "Brugge", "Belgium", SimpleTeam.WorldPart.EUROPE, 21));
        teams.add(new ClubTeam(4, "Red Star Belgrade", "Serbia", SimpleTeam.WorldPart.EUROPE, 20));
        teams.add(new ClubTeam(5, "Dinamo Kiev", "Ukraine", SimpleTeam.WorldPart.EUROPE, 24));
        teams.add(new ClubTeam(6, "Zenit", "Russia", SimpleTeam.WorldPart.EUROPE, 26));
        teams.add(new ClubTeam(7, "Rangers", "Scotland", SimpleTeam.WorldPart.EUROPE, 25));
        teams.add(new ClubTeam(8, "Salzburg", "Austria", SimpleTeam.WorldPart.EUROPE, 23));
        teams.add(new ClubTeam(9, "Ajax", "Netherlands", SimpleTeam.WorldPart.EUROPE, 25));
        teams.add(new ClubTeam(10, "Sporting Lisbon", "Portugal", SimpleTeam.WorldPart.EUROPE, 25));
        teams.add(new ClubTeam(11, "Porto", "Portugal", SimpleTeam.WorldPart.EUROPE, 27));
        teams.add(new ClubTeam(12, "PSG", "France", SimpleTeam.WorldPart.EUROPE, 29));
        teams.add(new ClubTeam(13, "Lille", "France", SimpleTeam.WorldPart.EUROPE, 26));
        teams.add(new ClubTeam(14, "RB Leipzig", "Germany", SimpleTeam.WorldPart.EUROPE, 24));
        teams.add(new ClubTeam(15, "Bayern Munich", "Germany", SimpleTeam.WorldPart.EUROPE, 30));
        teams.add(new ClubTeam(16, "Atalanta", "Italy", SimpleTeam.WorldPart.EUROPE, 25));
        teams.add(new ClubTeam(17, "AC Milan", "Italy", SimpleTeam.WorldPart.EUROPE, 27));
        teams.add(new ClubTeam(18, "Inter Milan", "Italy", SimpleTeam.WorldPart.EUROPE, 29));
        teams.add(new ClubTeam(19, "Barcelona", "Spain", SimpleTeam.WorldPart.EUROPE, 30));
        teams.add(new ClubTeam(20, "Real Madrid", "Spain", SimpleTeam.WorldPart.EUROPE, 30));
        teams.add(new ClubTeam(21, "Atletico Madrid", "Spain", SimpleTeam.WorldPart.EUROPE, 28));
        teams.add(new ClubTeam(22, "Liverpool", "England", SimpleTeam.WorldPart.EUROPE, 29));
        teams.add(new ClubTeam(23, "Manchester United", "England", SimpleTeam.WorldPart.EUROPE, 28));
        teams.add(new ClubTeam(24, "Manchester City", "England", SimpleTeam.WorldPart.EUROPE, 30));

        return teams;
    }

    private RatingByTeamOrder createRatingContinentalClubTournament(ArrayList<SimpleTeam> teams) {
        var teamIndexByNames = new HashMap<String, Integer>();
        for(int i = 0; i < teams.size(); ++i) {
            teamIndexByNames.put(teams.get(i).getName(), i);
        }

        var ratingTeams = new ArrayList<SimpleTeam>();
        var teamsInOrder = getTeamNamesInOrder();
        for(var teamName : teamsInOrder) {
            ratingTeams.add(teams.get(teamIndexByNames.get(teamName)));
        }

        return new RatingByTeamOrder(ratingTeams);
    }

    private ArrayList<String> getTeamNamesInOrder() {
        var teamsInOrder = new ArrayList<String>();
        teamsInOrder.add("Bayern Munich");
        teamsInOrder.add("Manchester City");
        teamsInOrder.add("Liverpool");
        teamsInOrder.add("Chelsea");
        teamsInOrder.add("Barcelona");
        teamsInOrder.add("PSG");
        teamsInOrder.add("Real Madrid");
        teamsInOrder.add("Juventus");
        teamsInOrder.add("Atletico Madrid");
        teamsInOrder.add("Manchester United");
        teamsInOrder.add("Sevilla");
        teamsInOrder.add("Borussia Dortmund");
        teamsInOrder.add("Porto");
        teamsInOrder.add("Shakhtar Donetsk");
        teamsInOrder.add("RB Leipzig");
        teamsInOrder.add("Ajax");
        teamsInOrder.add("Salzburg");
        teamsInOrder.add("Lyon");
        teamsInOrder.add("Atalanta");
        teamsInOrder.add("Inter Milan");
        teamsInOrder.add("Benfica");
        teamsInOrder.add("Basel");
        teamsInOrder.add("Zenit");
        teamsInOrder.add("Dinamo Kiev");
        teamsInOrder.add("Sporting Lisbon");
        teamsInOrder.add("Dinamo Zagbeb");
        teamsInOrder.add("Brugge");
        teamsInOrder.add("Olympiakos");
        teamsInOrder.add("Young Boys");
        teamsInOrder.add("AC Milan");
        teamsInOrder.add("Rangers");
        teamsInOrder.add("Viktoria");
        teamsInOrder.add("Celtic");
        teamsInOrder.add("PSV");
        teamsInOrder.add("Partizan");
        teamsInOrder.add("Wolfsburg");
        teamsInOrder.add("AEK");
        teamsInOrder.add("Spartak Moscow");
        teamsInOrder.add("AZ");
        teamsInOrder.add("Lille");
        teamsInOrder.add("Monaco");
        teamsInOrder.add("Appolon");
        teamsInOrder.add("Rapid Vienna");
        teamsInOrder.add("Fenerbahce");
        teamsInOrder.add("Antwerp");
        teamsInOrder.add("Osijec");
        teamsInOrder.add("Sporting Braga");
        teamsInOrder.add("Red Star Belgrade");
        return teamsInOrder;
    }

    @Test
    public void calcWorldCup() {

    }

}