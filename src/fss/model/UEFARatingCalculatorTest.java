package fss.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class UEFARatingCalculatorTest extends BaseTest {
    private static int FIRST_LC_QUALIFICATION          = 1;
    private static int SECOND_LC_QUALIFICATION         = 2;
    private static int LC_GROUP                        = 3;
    private static int LC_1_2                          = 4;
    private static int LC_FINAL                        = 5;
    private static int FIRST_EC_QUALIFICATION          = 6;
    private static int SECOND_EC_QUALIFICATION         = 7;
    private static int EC_GROUP                        = 8;
    private static int EC_1_4                          = 9;
    private static int EC_1_2                          = 10;
    private static int EC_FINAL                        = 11;

    private HashMap<String, Country> countries = new HashMap<>();

    public UEFARatingCalculatorTest() {
        countries.put("Italy", new Country(1, "Italy", WorldPart.EUROPE));
        //countries.put("Italy", new Country(1, "Italy", WorldPart.EUROPE));
        //countries.put("Italy", new Country(1, "Italy", WorldPart.EUROPE));
        //countries.put("Italy", new Country(1, "Italy", WorldPart.EUROPE));
        //countries.put("Italy", new Country(1, "Italy", WorldPart.EUROPE));
        //countries.put("Italy", new Country(1, "Italy", WorldPart.EUROPE));
        //countries.put("Italy", new Country(1, "Italy", WorldPart.EUROPE));
        //countries.put("Italy", new Country(1, "Italy", WorldPart.EUROPE));
    }

    @Test
    public void CalculatorTest() {

        UEFATournamentTest LC = getLCTournament();
        UEFATournamentTest LE = getLETournament(LC);

        var calculator = new UEFARatingCalculator();
        calculator.setTournament1stLevel(LC);
        calculator.setTournament2ndLevel(LE);

        calculator.calc();
    }

    private UEFATournamentTest getLCTournament() {
        UEFATournamentTest LC = new UEFATournamentTest("LC");

        ArrayList<SimpleTeam> teamsQual1 = new ArrayList<>();
        //teamsQual1.add(new ClubTeam())
        var Qual1 = new UEFAStagePoolTest( StageType.PLAYOFF, "LC QUAL 1", teamsQual1, null, 2);
        LC.stages.add(Qual1);


        return LC;
    }

    private UEFATournamentTest getLETournament(UEFATournamentTest LC) {
        UEFATournamentTest LE = new UEFATournamentTest("LE");



        return LE;
    }

}

class UEFAStagePoolTest extends StagePool {
    public ArrayList<SimpleTeam> winners = new ArrayList<>();
    public ArrayList<SimpleTeam> losers = new ArrayList<>();
    public ArrayList<Meet> meets = new ArrayList<>();

    public UEFAStagePoolTest(StageType stageType, String name, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        super(stageType, name, teams, rating, cntRounds);
    }

    @Override
    public void calc() {

    }

    @Override
    public ArrayList<SimpleTeam> getWinners() {
        return winners;
    }

    @Override
    public ArrayList<SimpleTeam> getLosers() {
        return losers;
    }

    @Override
    public ArrayList<Meet> getMeetings() {
        return meets;
    }
}

class UEFATournamentTest extends BaseTournament {
    public ArrayList<StagePool> stages = new ArrayList<>();

    public UEFATournamentTest(String name) {
        super(name);
    }

    @Override
    public void addScheme(Scheme scheme) {

    }

    @Override
    public void addTeamsToStage(int stageID, ArrayList<SimpleTeam> teams) {

    }

    @Override
    public void addRating(Ratingable rating) {

    }

    @Override
    public void addWinRules(ArrayList<Table.WinRules> rules) {

    }

    @Override
    public void calc() {

    }

    @Override
    public int getCntStagePool() {
        return 0;
    }

    @Override
    public int getStageID(int stagePoolIndex) {
        return 0;
    }

    @Override
    public StagePool getStagePoolByStageID(int stageID) {
        return null;
    }

    @Override
    public ArrayList<Table.Row> getFinalTableRows(int stageID) {
        return null;
    }

    @Override
    public ArrayList<SimpleTeam> getFirstStageTeams(int stageID, int cntTeam) {
        return null;
    }

    @Override
    public ArrayList<SimpleTeam> getLastStageTeams(int stageID, int cntTeam) {
        return null;
    }

    @Override
    public ArrayList<SimpleTeam> getNTeamStageTeams(int stageID, int nTeam) {
        return null;
    }

    @Override
    public ArrayList<SimpleTeam> getWinnersStageTeams(int stageID) {
        return null;
    }

    @Override
    public ArrayList<SimpleTeam> getLosersStageTeams(int stageID) {
        return null;
    }

    @Override
    public ArrayList<SimpleTeam> getAllTournamentTeams() {
        return null;
    }
}