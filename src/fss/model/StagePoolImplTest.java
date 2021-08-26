package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StagePoolImplTest extends BaseTest {
    @Test
    public void calcStagePoolPlayOff() {
        var teams = generateTeams(8);
        calcStagePoolPlayOff(teams, true);
        calcStagePoolPlayOff(teams, false);
    }

    private void calcStagePoolPlayOff(ArrayList<SimpleTeam> teams, boolean twoRounds) {
        var stagePool = new StagePoolImpl(StagePool.StageType.PLAYOFF, "Test", teams, new TestRating(teams), twoRounds ? 2 : 1);
        stagePool.calc();
        Assert.assertEquals(stagePool.getWinners().size(), teams.size() / 2);
        Assert.assertEquals(stagePool.getLosers().size(), teams.size() / 2);
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
        var stagePool = new StagePoolImpl(StagePool.StageType.CIRCLE, "Test",  teams, new TestRating(teams), cntRounds);
        stagePool.calc();
        Assert.assertEquals(stagePool.getWinners().size(), 1);
        Assert.assertEquals(stagePool.getLosers().size(), 1);
        Assert.assertEquals(stagePool.getFirstN(4).size(), 4);
        Assert.assertEquals(stagePool.getLastN(4).size(), 4);
        Assert.assertTrue(stagePool.getN(4) != null);
        Assert.assertTrue(stagePool.getN(1500) == null);
    }

    @Test
    public void calcStagePoolGroups() {
        var teams = generateTeams(51);
        var stagePool = new StagePoolImpl("Test", 10, teams, new TestRating(teams), 2);
        stagePool.calc();
        Assert.assertTrue(true);
    }
}