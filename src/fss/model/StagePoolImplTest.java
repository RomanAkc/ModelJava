package fss.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StagePoolImplTest extends BaseTest {
    @Test
    public void CreateAndCalcStagePoolPlayOff() {
        var teams = generateTeams(8);
        var sp = new StagePoolImpl(StagePool.StageType.PLAYOFF, "Test", teams, new TestRating(teams), 1);
        sp.calc();
        var winners = sp.getWinners();
        Assert.assertEquals(winners.size(), teams.size() / 2);
    }

}