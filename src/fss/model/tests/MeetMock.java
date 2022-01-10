package fss.model.tests;

import fss.model.Gameable;
import fss.model.Result;
import fss.model.SimpleTeam;

public class MeetMock implements Gameable {
    private final SimpleTeam firstTeam;
    private final SimpleTeam secondTeam;
    private Result result = null;

    public MeetMock(SimpleTeam firstTeam, SimpleTeam secondTeam) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
    }

    public MeetMock(SimpleTeam firstTeam, SimpleTeam secondTeam, int goalHome, int goalAway) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        SetMeetResult(goalHome, goalAway);
    }

    public MeetMock SetMeetResult(int goalHome, int goalAway) {
        result = new Result(goalHome, goalAway);
        return this;
    }

    @Override
    public SimpleTeam getTeamHome() {
        return firstTeam;
    }

    @Override
    public SimpleTeam getTeamAway() {
        return secondTeam;
    }

    @Override
    public void calc() {}

    @Override
    public Result getResultMeet() {
        return result;
    }

    @Override
    public boolean isWinnerHomeTeam() {
        return result.isWin();
    }

    @Override
    public boolean isDraw() {
        return result.isDraw();
    }

    @Override
    public SimpleTeam getWinner() {
        if (result.isWin()) {
            return firstTeam;
        } else if (result.isDraw()) {
            return null;
        }
        return secondTeam;
    }

    @Override
    public SimpleTeam getLoser() {
        if (result.isWin()) {
            return secondTeam;
        } else if (result.isDraw()) {
            return null;
        }
        return firstTeam;
    }
}
