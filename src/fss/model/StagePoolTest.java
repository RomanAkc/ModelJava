package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class StagePoolTest extends BaseTest {
    @Test
    public void calcStagePoolPlayOff() {
        var teams = generateTeams(8);
        calcStagePoolPlayOff(teams, true);
        calcStagePoolPlayOff(teams, false);
    }

    private void calcStagePoolPlayOff(ArrayList<SimpleTeam> teams, boolean twoRounds) {
        var rating = new RatingByTeamOrder(teams);

        var stagePool = new PlayOffStagePool("Test", teams, rating, twoRounds ? 2 : 1);
        stagePool.calc();
        Assert.assertEquals(stagePool.getWinners().size(), teams.size() / 2);
        Assert.assertEquals(stagePool.getLosers().size(), teams.size() / 2);

        var meetings = stagePool.getMeetings();
        for(var meet : meetings) {
            var posHome = rating.getTeamPosition(meet.getTeamHome());
            var posAway = rating.getTeamPosition(meet.getTeamAway());

            boolean isInTopHome = posHome < teams.size() / 2;
            boolean isInTopAway = posAway < teams.size() / 2;

            Assert.assertNotEquals(isInTopHome, isInTopAway);
        }
    }

    @Test
    public void calcStagePoolCircle() {
        var teams = generateTeams(8);
        calcStagePoolCircle(teams, 1);
        calcStagePoolCircle(teams, 2);
        calcStagePoolCircle(teams, 3);
        calcStagePoolCircle(teams, 4);
    }

    private void calcStagePoolCircle(ArrayList<SimpleTeam> teams, int cntRounds) {
        var stagePool = new RoundRobinStagePool("Test",  teams, new RatingByTeamOrder(teams), cntRounds);
        stagePool.calc();
        Assert.assertEquals(stagePool.getWinners().size(), 1);
        Assert.assertEquals(stagePool.getLosers().size(), 1);
        Assert.assertEquals(stagePool.getFirstN(4).size(), 4);
        Assert.assertEquals(stagePool.getLastN(4).size(), 4);
        Assert.assertEquals(stagePool.getN(4).size(), 1);
        Assert.assertEquals(stagePool.getN(1500).size(), 0);
    }

    @Test
    public void calcStagePoolGroups() {
        var teams = generateTeams(51);
        var stagePool = new GroupsStagePool("Test", 10, teams, new RatingByTeamOrder(teams), 2);
        stagePool.calc();
        Assert.assertEquals(stagePool.getWinners().size(), 10);
        Assert.assertEquals(stagePool.getLosers().size(), 10);
        Assert.assertEquals(stagePool.getN(2).size(), 10);
    }
}