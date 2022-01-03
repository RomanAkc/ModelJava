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
        Assert.assertFalse(checkMeetIsCalculated(meet));
    }

    @Test
    public void winTwoMeetResult() {
        for(int i = 0; i < 1000; ++i) {
            var meet = createWinTwoMeet();
            meet.calc();
            Assert.assertEquals(meet.getResultMeet(), null);
        }
    }

    private WinMeet createWinMeet() {
        var teams1 = generateClubTeamsWithPower(1, 30);
        var teams2 = generateClubTeamsWithPower(2, 29);
        return new WinMeet(teams1.get(0), teams2.get(1));
    }

    private WinTwoMeet createWinTwoMeet() {
        var teams1 = generateClubTeamsWithPower(1, 30);
        var teams2 = generateClubTeamsWithPower(2, 29);
        return new WinTwoMeet(teams1.get(0), teams2.get(1));
    }

    private Meet createMeet() {
        var teams = generateClubTeams(2);
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