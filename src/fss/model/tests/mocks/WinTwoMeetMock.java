package fss.model.tests.mocks;

import fss.model.Result;
import fss.model.SimpleTeam;
import fss.model.WinTwoGameable;

public class WinTwoMeetMock implements WinTwoGameable {
    private final SimpleTeam firstTeam;
    private final SimpleTeam secondTeam;
    private Result firstMeet = null;
    private Result secondMeet = null;
    private Result addTime = null;
    private Result pen = null;

    public WinTwoMeetMock(SimpleTeam firstTeam, SimpleTeam secondTeam) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
    }

    public WinTwoMeetMock SetFirstMeetResult(int goalHome, int goalAway) {
        firstMeet = new Result(goalHome, goalAway);
        return this;
    }

    public WinTwoMeetMock SetSecondMeetResult(int goalHome, int goalAway) {
        secondMeet = new Result(goalHome, goalAway);
        return this;
    }

    public WinTwoMeetMock SetAddTimeMeetResult(int goalHome, int goalAway) {
        addTime = new Result(goalHome, goalAway);
        return this;
    }

    public WinTwoMeetMock SetPenMeetResult(int goalHome, int goalAway) {
        pen = new Result(goalHome, goalAway);
        return this;
    }

    @Override
    public boolean isWinnerHomeTeamFirstMeet() {
        return firstMeet.isWin();
    }

    @Override
    public boolean isDrawFirstMeet() {
        return firstMeet.isDraw();
    }

    @Override
    public boolean isWinnerHomeTeamSecondMeet() {
        return !secondMeet.isWin() && !secondMeet.isDraw();
    }

    @Override
    public boolean isDrawSecondMeet() {
        return secondMeet.isDraw();
    }

    private int getGoalsFirstTeamMainTime() {
        return firstMeet.getGoalHome() + secondMeet.getGoalAway();
    }

    private int getGoalsSecondTeamMainTime() {
        return firstMeet.getGoalAway() + secondMeet.getGoalHome();
    }

    private Result getResultMeetWOPen() {
        var goalsFor = getGoalsFirstTeamMainTime();
        var goalsAway = getGoalsSecondTeamMainTime();

        if (addTime != null) {
            goalsFor += addTime.getGoalAway();
            goalsAway += addTime.getGoalHome();
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
        return null;
    }

    @Override
    public boolean isWinnerHomeTeam() {
        var goalsFirstTeam = getGoalsFirstTeamMainTime();
        var goalsSecondTeam = getGoalsSecondTeamMainTime();

        if (goalsFirstTeam != goalsSecondTeam) {
            return goalsFirstTeam > goalsSecondTeam;
        }

        if (!addTime.isDraw()) {
            return addTime.getGoalAway() > addTime.getGoalHome();
        }

        return pen.getGoalAway() > pen.getGoalHome();
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
