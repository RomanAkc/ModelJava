package fss.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UEFARatingCalculatorTest extends BaseTest {
    @Test
    void CalculatorTest() {
        var LC = new UEFATournamentTest("LC");
        var LE = new UEFATournamentTest("LE");

        var calculator = new UEFARatingCalculator();
        calculator.setTournament1stLevel(LC);
        calculator.setTournament2ndLevel(LE);

        calculator.calc();
        

    }

}

class UEFATournamentTest extends BaseTournament {
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