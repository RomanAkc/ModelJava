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
    }

    @Test
    public void calcStagePoolCircle() {
        var teams = generateTeams(8);
        var stagePool = new StagePoolImpl(StagePool.StageType.CIRCLE, "Test",  teams, new TestRating(teams), 2);
        stagePool.calc();
        Assert.assertEquals(stagePool.getWinners().size(), 1);
    }
}