package fss.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/*         LC Q1 Q2 LE Q1 Q2
1  Spain   2  1     1
2  England 2  1     1     1
3  Italy   2     1  1
4  Germany 1        1  1
5  France  1        1  1
6  Russia  1        1    1
7  Portuga 1     1  1
8  Scotlan 1     1  1
9  Netherl 1     1  1     1
10 Austria 0        1     1
*/

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
    private HashMap<String, ClubTeam> teams = new HashMap<>();

    public UEFARatingCalculatorTest() {
        countries.put("Spain", new Country(1, "Spain", WorldPart.EUROPE));
        countries.put("England", new Country(2, "England", WorldPart.EUROPE));
        countries.put("Italy", new Country(3, "Italy", WorldPart.EUROPE));
        countries.put("Germany", new Country(4, "Germany", WorldPart.EUROPE));
        countries.put("France", new Country(5, "France", WorldPart.EUROPE));
        countries.put("Russia", new Country(6, "Russia", WorldPart.EUROPE));
        countries.put("Portugal", new Country(7, "Portugal", WorldPart.EUROPE));
        countries.put("Scotland", new Country(8, "Scotland", WorldPart.EUROPE));
        countries.put("Netherlands", new Country(9, "Netherlands", WorldPart.EUROPE));
        countries.put("Austria", new Country(10, "Austria", WorldPart.EUROPE));

        teams.put("Real Madrid", new ClubTeam(1, "Real Madrid", countries.get("Spain"), 30));
        teams.put("Barcelona", new ClubTeam(2, "Barcelona", countries.get("Spain"), 30));
        teams.put("Atletico Madrid", new ClubTeam(3, "Atletico Madrid", countries.get("Spain"), 30));
        teams.put("Chelsea", new ClubTeam(4, "Chelsea", countries.get("England"), 30));
        teams.put("Manchester City", new ClubTeam(5, "Manchester City", countries.get("England"), 30));
        teams.put("Tottenham Hotspur", new ClubTeam(6, "Tottenham Hotspur", countries.get("England"), 30));
        teams.put("Internazionale", new ClubTeam(7, "Internazionale", countries.get("Italy"), 30));
        teams.put("Juventus", new ClubTeam(8, "Juventus", countries.get("Italy"), 30));
        teams.put("Fiorentina", new ClubTeam(9, "Fiorentina", countries.get("Italy"), 30));
        teams.put("Bayern Munich", new ClubTeam(10, "Bayern Munich", countries.get("Germany"), 30));
        teams.put("Bremen", new ClubTeam(11, "Bremen", countries.get("Germany"), 30));
        teams.put("PSG", new ClubTeam(12, "PSG", countries.get("France"), 30));
        teams.put("Lyon", new ClubTeam(13, "Lyon", countries.get("France"), 30));
        teams.put("Spartak Moscow", new ClubTeam(14, "Spartak Moscow", countries.get("Russia"), 30));
        teams.put("Zenit St Petersburg", new ClubTeam(15, "Zenit St Petersburg", countries.get("Russia"), 30));
        teams.put("Porto", new ClubTeam(16, "Porto", countries.get("Portugal"), 30));
        teams.put("Benfica", new ClubTeam(17, "Benfica", countries.get("Portugal"), 30));
        teams.put("Rangers", new ClubTeam(18, "Rangers", countries.get("Scotland"), 30));
        teams.put("Celtic", new ClubTeam(19, "Celtic", countries.get("Scotland"), 30));
        teams.put("PSV", new ClubTeam(20, "PSV", countries.get("Netherlands"), 30));
        teams.put("Willem II", new ClubTeam(21, "Willem II", countries.get("Netherlands"), 30));
        teams.put("Sturm", new ClubTeam(22, "Sturm", countries.get("Austria"), 30));
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
        teamsQual1.add(teams.get("Juventus"));
        teamsQual1.add(teams.get("Porto"));
        teamsQual1.add(teams.get("Rangers"));
        teamsQual1.add(teams.get("PSV"));
        var qual1 = new UEFAStagePoolTest( StageType.PLAYOFF, "LC QUAL 1", teamsQual1, null, 2);
        //var meet1 = new WinTwoMeet(teamsQual1.get(0), teamsQual1.get(1));
        LC.stages.add(qual1);


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
    public ArrayList<Gameable> meets = new ArrayList<>();

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
    public ArrayList<Gameable> getMeetings() {
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