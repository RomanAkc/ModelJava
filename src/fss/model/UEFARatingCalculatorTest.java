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

        AddSchemeForTournament(LC, calculator);
        AddSchemeForTournament(LE, calculator);

        calculator.calc();
    }

    private void AddSchemeForTournament(UEFATournamentTest tournament, UEFARatingCalculator calculator) {

        for(int i = 0; i < tournament.stages.size(); ++i) {

        }
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

    private UEFAStagePoolTest getLCFinal() {
        ArrayList<SimpleTeam> teamsF = new ArrayList<>();
        teamsF.add(teams.get("Chelsea"));
        teamsF.add(teams.get("Internazionale"));

        WinMeetTest finalLC = new WinMeetTest(teamsF.get(0), teamsF.get(1));
        finalLC.SetMeetResult(2, 3);

        var lcFinal = new UEFAStagePoolTest( StageType.PLAYOFF, "LC Final", teamsF, null, 1);
        lcFinal.meets.add(finalLC);
        return lcFinal;
    }

    private UEFAStagePoolTest getLC12() {
        ArrayList<SimpleTeam> teams12 = new ArrayList<>();
        teams12.add(teams.get("Chelsea"));
        teams12.add(teams.get("Juventus"));
        teams12.add(teams.get("Real Madrid"));
        teams12.add(teams.get("Internazionale"));

        WinTwoMeetTest meet1 = new WinTwoMeetTest(teams12.get(0), teams12.get(1));
        meet1.SetFirstMeetResult(2, 0);
        meet1.SetSecondMeetResult(1,1);

        WinTwoMeetTest meet2 = new WinTwoMeetTest(teams12.get(2), teams12.get(3));
        meet2.SetFirstMeetResult(2, 2);
        meet2.SetSecondMeetResult(0,0);
        meet2.SetAddTimeMeetResult(0, 0);
        meet2.SetPenMeetResult(7, 6);

        var lc12 = new UEFAStagePoolTest( StageType.PLAYOFF, "LC 1/2", teams12, null, 2);
        lc12.meets.add(meet1);
        lc12.meets.add(meet2);
        return lc12;
    }

    private UEFAStagePoolTest getLCGroup() {
        ArrayList<SimpleTeam> teamsGroup = new ArrayList<>();
        teamsGroup.add(teams.get("Real Madrid"));
        teamsGroup.add(teams.get("Juventus"));
        teamsGroup.add(teams.get("PSG"));
        teamsGroup.add(teams.get("Bayern Munich"));
        teamsGroup.add(teams.get("Barcelona"));
        teamsGroup.add(teams.get("Chelsea"));
        teamsGroup.add(teams.get("Internazionale"));
        teamsGroup.add(teams.get("Spartak Moscow"));

        UEFAStagePoolTest groupStage = new UEFAStagePoolTest( StageType.GROUPS, "LC GROUPS", teamsGroup, null, 2);
        groupStage.meets.add(new MeetTest(teamsGroup.get(0), teamsGroup.get(1), 1, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(2), teamsGroup.get(3), 1, 2));
        groupStage.meets.add(new MeetTest(teamsGroup.get(3), teamsGroup.get(0), 3, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(1), teamsGroup.get(2), 4, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(3), teamsGroup.get(1), 1, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(0), teamsGroup.get(2), 2, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(1), teamsGroup.get(3), 2, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(2), teamsGroup.get(0), 1, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(0), teamsGroup.get(3), 2, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(2), teamsGroup.get(1), 1, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(1), teamsGroup.get(0), 4, 2));
        groupStage.meets.add(new MeetTest(teamsGroup.get(3), teamsGroup.get(2), 0, 0));

        //RM - 6 3 1 2 10 +
        //Ju - 6 3 1 2 10 +
        //PS - 6 1 2 3 5
        //Ba - 6 2 2 2 8

        groupStage.meets.add(new MeetTest(teamsGroup.get(4), teamsGroup.get(5), 1, 2));
        groupStage.meets.add(new MeetTest(teamsGroup.get(6), teamsGroup.get(7), 5, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(7), teamsGroup.get(4), 1, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(5), teamsGroup.get(6), 1, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(7), teamsGroup.get(5), 0, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(4), teamsGroup.get(6), 0, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(5), teamsGroup.get(7), 3, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(6), teamsGroup.get(4), 2, 0));
        groupStage.meets.add(new MeetTest(teamsGroup.get(4), teamsGroup.get(7), 1, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(6), teamsGroup.get(5), 3, 2));
        groupStage.meets.add(new MeetTest(teamsGroup.get(5), teamsGroup.get(4), 1, 1));
        groupStage.meets.add(new MeetTest(teamsGroup.get(7), teamsGroup.get(6), 1, 1));

        //Ba - 6 0 2 4 2
        //Ch - 6 2 3 1 9 +
        //In - 6 4 2 0 14 +
        //Sp - 6 1 3 2 6

        return groupStage;
    }

    private UEFAStagePoolTest getLCQual2() {
        ArrayList<SimpleTeam> teamsQual2 = new ArrayList<>();
        teamsQual2.add(teams.get("Juventus"));
        teamsQual2.add(teams.get("Rangers"));
        teamsQual2.add(teams.get("Barcelona"));
        teamsQual2.add(teams.get("Manchester City"));

        WinTwoMeetTest meet1 = new WinTwoMeetTest(teamsQual2.get(0), teamsQual2.get(3));
        meet1.SetFirstMeetResult(0, 0);
        meet1.SetSecondMeetResult(1,1);
        meet1.SetAddTimeMeetResult(0,0);
        meet1.SetPenMeetResult(2,4);

        WinTwoMeetTest meet2 = new WinTwoMeetTest(teamsQual2.get(1), teamsQual2.get(2));
        meet2.SetFirstMeetResult(2, 3);
        meet2.SetSecondMeetResult(4,1);

        var qual2 = new UEFAStagePoolTest( StageType.PLAYOFF, "LC QUAL 2", teamsQual2, null, 2);
        qual2.meets.add(meet1);
        qual2.meets.add(meet2);
        //qual2.winners.add(teams.get("Juventus"));
        //qual2.winners.add(teams.get("Barcelona"));
        //qual2.losers.add(teams.get("Manchester City"));
        //qual2.losers.add(teams.get("Rangers"));
        return qual2;
    }

    private UEFAStagePoolTest getLCQual1() {
        ArrayList<SimpleTeam> teamsQual1 = new ArrayList<>();
        teamsQual1.add(teams.get("Juventus"));
        teamsQual1.add(teams.get("Porto"));
        teamsQual1.add(teams.get("Rangers"));
        teamsQual1.add(teams.get("PSV"));

        var meet1 = new WinTwoMeetTest(teamsQual1.get(0), teamsQual1.get(3));
        meet1.SetFirstMeetResult(3, 0);
        meet1.SetSecondMeetResult(1,1);

        var meet2 = new WinTwoMeetTest(teamsQual1.get(1), teamsQual1.get(2));
        meet2.SetFirstMeetResult(2, 1);
        meet2.SetSecondMeetResult(1,0);
        meet2.SetAddTimeMeetResult(1,0);

        var qual1 = new UEFAStagePoolTest( StageType.PLAYOFF, "LC QUAL 1", teamsQual1, null, 2);
        qual1.meets.add(meet1);
        qual1.meets.add(meet2);
        qual1.winners.add(teams.get("Juventus"));
        qual1.winners.add(teams.get("Rangers"));
        qual1.losers.add(teams.get("Porto"));
        qual1.losers.add(teams.get("PSV"));
        return qual1;
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