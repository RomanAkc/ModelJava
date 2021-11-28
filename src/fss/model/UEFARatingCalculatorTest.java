package fss.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/*         LC Q2 Q1 LE Q1 Q2
1  Spain   2  1     1
2  England 2  1     1     1
3  Italy   2     1  1
4  Germany 1        1  1
5  France  1        1  1
6  Russia  1        1     1
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

        UEFARatingCalculator calculator = new UEFARatingCalculator();
        calculator.setTournament1stLevel(LC);
        calculator.setTournament2ndLevel(LE);

        AddSchemeForTournament(LC, 1, calculator);
        AddSchemeForTournament(LE, 2, calculator);

        calculator.calc();

        HashMap<SimpleTeam, Double> pointsByTeam = calculator.getPointsByTeam();
        HashMap<Country, Double> pointsByCountry = calculator.getPointsByCountry();
    }

    private void AddSchemeForTournament(UEFATournamentTest tournament, int tournamentID, UEFARatingCalculator calculator) {
        for(int i = 0; i < tournament.stages.size(); ++i) {
            UEFAStagePoolTest stage = (UEFAStagePoolTest)tournament.stages.get(i);

            UEFARatingSchemePart schemePart = new UEFARatingSchemePart();
            schemePart.tournamentID = tournamentID;
            schemePart.stageID = i;
            schemePart.ratingStageType = stage.getName().contains("QUAL")
                    ? UEFARatingStageType.QUALIFICATION : UEFARatingStageType.NORMAL;
            schemePart.bonusPoint = stage.bonusPoint;

            calculator.addRatingScheme(schemePart);
        }
    }


    private void addMeetToStagePool(SimpleTeam teamHome, SimpleTeam teamAway, int goalHome, int goalAway, UEFAStagePoolTest stagePool) {
        MeetTest meet = new MeetTest(teamHome, teamAway);
        meet.SetMeetResult(goalHome, goalAway);
        stagePool.meets.add(meet);
    }

    private void addWinMeetToStagePool(SimpleTeam teamHome, SimpleTeam teamAway, int goalHome, int goalAway, UEFAStagePoolTest stagePool) {
        WinMeetTest meet = new WinMeetTest(teamHome, teamAway);
        meet.SetMeetResult(goalHome, goalAway);
        stagePool.meets.add(meet);
    }

    private void addWinTwoMeetToStagePool(SimpleTeam teamHome, SimpleTeam teamAway
            , int goalHomeFirstMeet, int goalAwayFirstMeet, int goalHomeSecondMeet, int goalAwaySecondMeet
            , boolean addTime, int goalHomeSecondMeetAddTime, int goalAwaySecondMeetAddTime
            , boolean pen, int goalHomeSecondMeetPen, int goalAwaySecondMeetPen
            , UEFAStagePoolTest stagePool) {
        WinTwoMeetTest meet = new WinTwoMeetTest(teamHome, teamAway);
        meet.SetFirstMeetResult(goalHomeFirstMeet, goalAwayFirstMeet);
        meet.SetSecondMeetResult(goalHomeSecondMeet, goalAwaySecondMeet);
        if(addTime) {
            meet.SetAddTimeMeetResult(goalHomeSecondMeetAddTime, goalAwaySecondMeetAddTime);
        }
        if(pen) {
            meet.SetPenMeetResult(goalHomeSecondMeetPen, goalAwaySecondMeetPen);
        }
        stagePool.meets.add(meet);
    }

    private ArrayList<SimpleTeam> fillStageTeams(String ... teamNames) {
        ArrayList<SimpleTeam> stageTeams = new ArrayList<>();
        for(var name : teamNames) {
            stageTeams.add(teams.get(name));
        }
        return stageTeams;
    }

    private UEFATournamentTest getLCTournament() {
        UEFATournamentTest LC = new UEFATournamentTest("LC");

        LC.stages.add(getLCQual1());
        LC.stages.add(getLCQual2());
        LC.stages.add(getLCGroup());
        LC.stages.add(getLC12());
        LC.stages.add(getLCFinal());

        return LC;
    }

    private UEFAStagePoolTest getLCQual1() {
        ArrayList<SimpleTeam> stageTeams = fillStageTeams("Juventus", "Porto", "Rangers", "PSV");
        UEFAStagePoolTest lcQual1 = new UEFAStagePoolTest( StageType.PLAYOFF, "LC QUAL 1", stageTeams, 2);

        addWinTwoMeetToStagePool(stageTeams.get(0), stageTeams.get(3), 3, 0, 1, 1
                , false, 0, 0, false, 0, 0, lcQual1);
        addWinTwoMeetToStagePool(stageTeams.get(1), stageTeams.get(2), 2, 1, 1, 0
                , true, 1, 0, false, 0, 0, lcQual1);

        return lcQual1;
    }

    private UEFAStagePoolTest getLCQual2() {
        ArrayList<SimpleTeam> stageTeams = fillStageTeams("Juventus", "Rangers", "Barcelona", "Manchester City");
        var lcQual2 = new UEFAStagePoolTest( StageType.PLAYOFF, "LC QUAL 2", stageTeams, 2);

        addWinTwoMeetToStagePool(stageTeams.get(0), stageTeams.get(3), 0, 0, 1, 1
                , true, 0, 0, true, 2, 4, lcQual2);
        addWinTwoMeetToStagePool(stageTeams.get(1), stageTeams.get(2), 2, 3, 4, 1
                , false, 0, 0, false, 0, 0, lcQual2);

        return lcQual2;
    }

    private UEFAStagePoolTest getLCGroup() {
        ArrayList<SimpleTeam> stageTeams = fillStageTeams("Real Madrid", "Juventus", "PSG", "Bayern Munich"
                , "Barcelona", "Chelsea", "Internazionale", "Spartak Moscow");

        UEFAStagePoolTest groupStage = new UEFAStagePoolTest( StageType.GROUPS, "LC GROUPS", stageTeams, 2);
        groupStage.bonusPoint = 5;

        addMeetToStagePool(stageTeams.get(0), stageTeams.get(1), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(2), stageTeams.get(3), 1, 2, groupStage);
        addMeetToStagePool(stageTeams.get(3), stageTeams.get(0), 3, 0, groupStage);
        addMeetToStagePool(stageTeams.get(1), stageTeams.get(2), 4, 1, groupStage);
        addMeetToStagePool(stageTeams.get(3), stageTeams.get(1), 1, 1, groupStage);
        addMeetToStagePool(stageTeams.get(0), stageTeams.get(2), 2, 0, groupStage);
        addMeetToStagePool(stageTeams.get(1), stageTeams.get(3), 2, 1, groupStage);
        addMeetToStagePool(stageTeams.get(2), stageTeams.get(0), 1, 1, groupStage);
        addMeetToStagePool(stageTeams.get(0), stageTeams.get(3), 2, 1, groupStage);
        addMeetToStagePool(stageTeams.get(2), stageTeams.get(1), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(1), stageTeams.get(0), 4, 2, groupStage);
        addMeetToStagePool(stageTeams.get(3), stageTeams.get(2), 0, 0, groupStage);

        //RM - 6 3 1 2 10 ++
        //Ju - 6 3 1 2 10 ++
        //PS - 6 1 2 3 5
        //Ba - 6 2 2 2 8 +

        addMeetToStagePool(stageTeams.get(4), stageTeams.get(5), 1, 2, groupStage);
        addMeetToStagePool(stageTeams.get(6), stageTeams.get(7), 5, 0, groupStage);
        addMeetToStagePool(stageTeams.get(7), stageTeams.get(4), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(5), stageTeams.get(6), 1, 1, groupStage);
        addMeetToStagePool(stageTeams.get(7), stageTeams.get(5), 0, 0, groupStage);
        addMeetToStagePool(stageTeams.get(4), stageTeams.get(6), 0, 1, groupStage);
        addMeetToStagePool(stageTeams.get(5), stageTeams.get(7), 3, 0, groupStage);
        addMeetToStagePool(stageTeams.get(6), stageTeams.get(4), 2, 0, groupStage);
        addMeetToStagePool(stageTeams.get(4), stageTeams.get(7), 1, 1, groupStage);
        addMeetToStagePool(stageTeams.get(6), stageTeams.get(5), 3, 2, groupStage);
        addMeetToStagePool(stageTeams.get(5), stageTeams.get(4), 1, 1, groupStage);
        addMeetToStagePool(stageTeams.get(7), stageTeams.get(6), 1, 1, groupStage);

        //Ba - 6 0 2 4 2
        //Ch - 6 2 3 1 9 ++
        //In - 6 4 2 0 14 ++
        //Sp - 6 1 3 2 6 +

        return groupStage;
    }

    private UEFAStagePoolTest getLC12() {
        ArrayList<SimpleTeam> stageTeams = fillStageTeams("Chelsea", "Juventus", "Real Madrid", "Internazionale");
        var lc12 = new UEFAStagePoolTest( StageType.PLAYOFF, "LC 1/2", stageTeams, 2);
        lc12.bonusPoint = 2;

        addWinTwoMeetToStagePool(stageTeams.get(0), stageTeams.get(1), 2, 0, 1, 1
                , false, 0, 0, false, 0, 0, lc12);
        addWinTwoMeetToStagePool(stageTeams.get(2), stageTeams.get(3), 2, 2, 0, 0
                , true, 0, 0, true, 7, 6, lc12);

        return lc12;
    }

    private UEFAStagePoolTest getLCFinal() {
        ArrayList<SimpleTeam> stageTeams = fillStageTeams("Chelsea", "Internazionale");

        UEFAStagePoolTest lcFinal = new UEFAStagePoolTest( StageType.PLAYOFF, "LC Final", stageTeams, 1);
        lcFinal.bonusPoint = 2;
        addWinMeetToStagePool(stageTeams.get(0), stageTeams.get(1), 2, 3, lcFinal);

        return lcFinal;
    }

    private UEFATournamentTest getLETournament(UEFATournamentTest LC) {
        UEFATournamentTest LE = new UEFATournamentTest("LE");

        LE.stages.add(getLEQual1());
        LE.stages.add(getLEQual2());
        LE.stages.add(getLEGroup(LC));
        LE.stages.add(getLE14(LC));
        LE.stages.add(getLE12());
        LE.stages.add(getLEFinal());

        return LE;
    }

    private UEFAStagePoolTest getLEQual1() {
        ArrayList<SimpleTeam> stageTeams = fillStageTeams("Tottenham Hotspur", "Zenit St Petersburg", "Willem II", "Sturm");
        UEFAStagePoolTest leQual1 = new UEFAStagePoolTest( StageType.PLAYOFF, "LE QUAL 1", stageTeams, 2);

        addWinTwoMeetToStagePool(stageTeams.get(0), stageTeams.get(2), 6, 0, 0, 3
                , false, 0, 0, false, 0, 0, leQual1);
        addWinTwoMeetToStagePool(stageTeams.get(1), stageTeams.get(3), 2, 0, 0, 1
                , false, 0, 0, false, 0, 0, leQual1);

        return leQual1;
    }
    private UEFAStagePoolTest getLEQual2() {
        ArrayList<SimpleTeam> stageTeams = fillStageTeams("Tottenham Hotspur", "Zenit St Petersburg", "Bremen", "Lyon");
        UEFAStagePoolTest leQual2 = new UEFAStagePoolTest( StageType.PLAYOFF, "LE QUAL 2", stageTeams, 2);

        addWinTwoMeetToStagePool(stageTeams.get(3), stageTeams.get(1), 1, 1, 1, 1
                , true, 0, 0, true, 7, 6, leQual2);
        addWinTwoMeetToStagePool(stageTeams.get(2), stageTeams.get(0), 1, 0, 1, 0
                , true, 0, 1, false, 0, 0, leQual2);

        return leQual2;
    }
    private UEFAStagePoolTest getLEGroup(UEFATournamentTest LC) {
        ArrayList<SimpleTeam> stageTeams = fillStageTeams("Zenit St Petersburg", "Atletico Madrid", "Celtic", "Benfica"
            , "Bremen", "Fiorentina", "Manchester City", "Rangers");

        UEFAStagePoolTest groupStage = new UEFAStagePoolTest( StageType.GROUPS, "LE GROUPS", stageTeams, 2);

        addMeetToStagePool(stageTeams.get(0), stageTeams.get(1), 0, 1, groupStage);
        addMeetToStagePool(stageTeams.get(2), stageTeams.get(3), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(3), stageTeams.get(0), 2, 4, groupStage);
        addMeetToStagePool(stageTeams.get(1), stageTeams.get(2), 2, 0, groupStage);
        addMeetToStagePool(stageTeams.get(3), stageTeams.get(1), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(0), stageTeams.get(2), 2, 2, groupStage);
        addMeetToStagePool(stageTeams.get(1), stageTeams.get(3), 2, 1, groupStage);
        addMeetToStagePool(stageTeams.get(2), stageTeams.get(0), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(0), stageTeams.get(3), 4, 0, groupStage);
        addMeetToStagePool(stageTeams.get(2), stageTeams.get(1), 2, 2, groupStage);
        addMeetToStagePool(stageTeams.get(1), stageTeams.get(0), 1, 2, groupStage);
        addMeetToStagePool(stageTeams.get(3), stageTeams.get(2), 0, 1, groupStage);

        //Ze - 6 3 1 2 10 +
        //AM - 6 3 1 2 10 +
        //Ce - 6 3 2 1 11 +
        //Be - 6 1 0 5 3

        addMeetToStagePool(stageTeams.get(4), stageTeams.get(5), 3, 1, groupStage);
        addMeetToStagePool(stageTeams.get(6), stageTeams.get(7), 2, 0, groupStage);
        addMeetToStagePool(stageTeams.get(7), stageTeams.get(4), 1, 1, groupStage);
        addMeetToStagePool(stageTeams.get(5), stageTeams.get(6), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(7), stageTeams.get(5), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(4), stageTeams.get(6), 4, 3, groupStage);
        addMeetToStagePool(stageTeams.get(5), stageTeams.get(7), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(6), stageTeams.get(4), 2, 3, groupStage);
        addMeetToStagePool(stageTeams.get(4), stageTeams.get(7), 2, 1, groupStage);
        addMeetToStagePool(stageTeams.get(6), stageTeams.get(5), 2, 3, groupStage);
        addMeetToStagePool(stageTeams.get(5), stageTeams.get(4), 1, 0, groupStage);
        addMeetToStagePool(stageTeams.get(7), stageTeams.get(6), 1, 1, groupStage);

        //Br - 6 4 1 1 13 +
        //Fi - 6 4 0 2 12 +
        //MC - 6 1 1 4 4
        //Ra - 6 1 2 3 5 +

        return groupStage;
    }
    private UEFAStagePoolTest getLE14(UEFATournamentTest LC) {
        ArrayList<SimpleTeam> teams14 = new ArrayList<>();
        teams14.add(teams.get("Zenit St Petersburg"));
        teams14.add(teams.get("Atletico Madrid"));
        teams14.add(teams.get("Celtic"));
        teams14.add(teams.get("Bremen"));
        teams14.add(teams.get("Fiorentina"));
        teams14.add(teams.get("Rangers"));
        teams14.add(teams.get("Bayern Munich"));
        teams14.add(teams.get("Spartak Moscow"));

        var meet1 = new WinTwoMeetTest(teams14.get(0), teams14.get(6));
        meet1.SetFirstMeetResult(1, 1);
        meet1.SetSecondMeetResult(2,0);

        var meet2 = new WinTwoMeetTest(teams14.get(1), teams14.get(5));
        meet2.SetFirstMeetResult(3, 0);
        meet2.SetSecondMeetResult(1,0);

        var meet3 = new WinTwoMeetTest(teams14.get(2), teams14.get(7));
        meet3.SetFirstMeetResult(2, 1);
        meet3.SetSecondMeetResult(2,0);

        var meet4 = new WinTwoMeetTest(teams14.get(3), teams14.get(4));
        meet4.SetFirstMeetResult(1, 1);
        meet4.SetSecondMeetResult(0,0);
        meet4.SetAddTimeMeetResult(1,1);
        meet4.SetSecondMeetResult(4,2);

        var le14 = new UEFAStagePoolTest( StageType.PLAYOFF, "LE 1/4", teams14, 2);
        le14.meets.add(meet1);
        le14.meets.add(meet2);
        le14.meets.add(meet3);
        le14.meets.add(meet4);

        return le14;

    }
    private UEFAStagePoolTest getLE12() {
        ArrayList<SimpleTeam> teams12 = new ArrayList<>();
        teams12.add(teams.get("Bayern Munich"));
        teams12.add(teams.get("Atletico Madrid"));
        teams12.add(teams.get("Spartak Moscow"));
        teams12.add(teams.get("Fiorentina"));

        var meet1 = new WinTwoMeetTest(teams12.get(0), teams12.get(2));
        meet1.SetFirstMeetResult(1, 2);
        meet1.SetSecondMeetResult(0,3);

        var meet2 = new WinTwoMeetTest(teams12.get(1), teams12.get(3));
        meet2.SetFirstMeetResult(1, 0);
        meet2.SetSecondMeetResult(1,0);
        meet2.SetAddTimeMeetResult(1,0);

        var le12 = new UEFAStagePoolTest( StageType.PLAYOFF, "LE QUAL 2", teams12, 2);
        le12.meets.add(meet1);
        le12.meets.add(meet2);

        return le12;
    }
    private UEFAStagePoolTest getLEFinal() {
        ArrayList<SimpleTeam> teamsF = new ArrayList<>();
        teamsF.add(teams.get("Bayern Munich"));
        teamsF.add(teams.get("Fiorentina"));

        WinMeetTest finalLC = new WinMeetTest(teamsF.get(0), teamsF.get(1));
        finalLC.SetMeetResult(3, 1);

        var leFinal = new UEFAStagePoolTest( StageType.PLAYOFF, "LC Final", teamsF, 1);
        leFinal.bonusPoint = 2;
        leFinal.meets.add(finalLC);
        return leFinal;
    }

}

class UEFAStagePoolTest extends StagePool {
    public ArrayList<SimpleTeam> winners = new ArrayList<>();
    public ArrayList<SimpleTeam> losers = new ArrayList<>();
    public ArrayList<Gameable> meets = new ArrayList<>();
    int bonusPoint = 0;

    public UEFAStagePoolTest(StageType stageType, String name, ArrayList<SimpleTeam> teams, int cntRounds) {
        super(stageType, name, teams, null, cntRounds);
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
        return stages.size();
    }

    @Override
    public int getStageID(int stagePoolIndex) {
        return stagePoolIndex;
    }

    @Override
    public StagePool getStagePoolByStageID(int stageID) {
        return stages.get(stageID);
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