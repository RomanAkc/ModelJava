package fss.model;

import org.junit.Assert;
import org.junit.Test;

public class MeetTest extends BaseTest {
    @Test
    public void meetCalc() {
        Assert.assertTrue(checkMeetIsCalculated(createMeetAndCalc(false)));
        Assert.assertTrue(checkMeetIsCalculated(createMeetAndCalc(true)));
    }

    @Test
    public void meetNotCalc() {
        var meet = createMeet();
        Assert.assertFalse(meet.isAlreadyCalculated());
    }

    @Test
    public void winTwoMeetResult() {
        for(int i = 0; i < 10000; ++i) {
            var meet = createWinTwoMeet();
            meet.calc();
            var result = meet.getResultMeet();
            Assert.assertNotEquals(result.getGoalHome(), result.getGoalAway());
        }
    }

    private WinTwoMeet createWinTwoMeet() {
        var teams = generateTeams(2);
        return new WinTwoMeet(teams.get(0), teams.get(1));
    }

    private Meet createMeet() {
        var teams = generateTeams(2);
        return new Meet(teams.get(0), teams.get(1));
    }

    private Meet createMeetAndCalc(boolean useOwner) {
        var meet = createMeet();
        if(useOwner) {
            meet.calcUseOwner();
        } else {
            meet.calc();
        }
        return meet;
    }

    private boolean checkMeetIsCalculated(Meet meet) {
        return  meet.getResultMeet() != null;
    }
}