package fss.model.tests;

import fss.model.Result;
import fss.model.SimpleTeam;
import fss.model.WinGameable;

public class WinMeetMock implements WinGameable {
    private final SimpleTeam firstTeam;
    private final SimpleTeam secondTeam;
    private Result mainTime = null;
    private Result addTime = null;
    private Result pen = null;

    public WinMeetMock(SimpleTeam firstTeam, SimpleTeam secondTeam) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
    }

    public WinMeetMock(SimpleTeam firstTeam, SimpleTeam secondTeam, int goalHome, int goalAway) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.mainTime = new Result(goalHome, goalAway);
    }

    public WinMeetMock(SimpleTeam firstTeam, SimpleTeam secondTeam, int goalHome, int goalAway
            , int goalHomeAdd, int goalAwayAdd) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.mainTime = new Result(goalHome, goalAway);
        this.addTime = new Result(goalHomeAdd, goalAwayAdd);
    }

    public WinMeetMock(SimpleTeam firstTeam, SimpleTeam secondTeam, int goalHome, int goalAway
            , int goalHomeAdd, int goalAwayAdd, int goalHomePen, int goalAwayPen) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.mainTime = new Result(goalHome, goalAway);
        this.addTime = new Result(goalHomeAdd, goalAwayAdd);
        this.pen = new Result(goalHomePen, goalAwayPen);
    }

    public WinMeetMock SetMeetResult(int goalHome, int goalAway) {
        mainTime = new Result(goalHome, goalAway);
        return this;
    }

    public WinMeetMock SetAddTimeMeetResult(int goalHome, int goalAway) {
        addTime = new Result(goalHome, goalAway);
        return this;
    }

    public WinMeetMock SetPenMeetResult(int goalHome, int goalAway) {
        pen = new Result(goalHome, goalAway);
        return this;
    }

    private Result getResultMeetWOPen() {
        var goalsFor = mainTime.getGoalHome();
        var goalsAway = mainTime.getGoalAway();

        if (addTime != null) {
            goalsFor += addTime.getGoalHome();
            goalsAway += addTime.getGoalAway();
        }

        return new Result(goalsFor, goalsAway);
    }

    @Override
    public boolean isWinnerHomeTeamWOPen() {
        return getResultMeetWOPen().isWin();
    }

    @Override
    public boolean isDrawWOPen() {
        return getResultMeetWOPen().isDraw();
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
        return mainTime;
    }

    @Override
    public boolean isWinnerHomeTeam() {
        if (!mainTime.isDraw()) {
            return mainTime.getGoalHome() > mainTime.getGoalAway();
        }

        if (!addTime.isDraw()) {
            return addTime.getGoalHome() > addTime.getGoalAway();
        }

        return pen.getGoalHome() > pen.getGoalAway();
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public SimpleTeam getWinner() {
        return isWinnerHomeTeam() ? firstTeam : secondTeam;
    }

    @Override
    public SimpleTeam getLoser() {
        return isWinnerHomeTeam() ? secondTeam : firstTeam;
    }
}
