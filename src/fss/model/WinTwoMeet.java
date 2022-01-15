package fss.model;

public class WinTwoMeet implements WinTwoGameable {
    private final Meet firstMeet;
    private final Meet secondMeet;
    private Result resultAdd = null;
    private Result resultPen = null;

    public WinTwoMeet(SimpleTeam firstTeam, SimpleTeam secondTeam) {
        firstMeet = new Meet(firstTeam, secondTeam);
        secondMeet = new Meet(secondTeam, firstTeam);
    }

    @Override
    public SimpleTeam getTeamHome() {
        return firstMeet.getTeamHome();
    }

    @Override
    public SimpleTeam getTeamAway() {
        return firstMeet.getTeamAway();
    }

    private int getGoalsFirstTeamMainTime() {
        return firstMeet.getResultMeet().getGoalHome() + secondMeet.getResultMeet().getGoalAway();
    }

    private int getGoalsSecondTeamMain() {
        return firstMeet.getResultMeet().getGoalAway() + secondMeet.getResultMeet().getGoalHome();
    }

    private boolean checkIsDraw() {
        return getGoalsFirstTeamMainTime() == getGoalsSecondTeamMain();
    }

    @Override
    public void calc() {
        firstMeet.calcUseOwner();
        secondMeet.calcUseOwner();

        if(!checkIsDraw()) {
            return;
        }

        resultAdd = ResultCalculator.calcAddTime(secondMeet.getTeamHome().getPower(), secondMeet.getTeamAway().getPower());
        if(!resultAdd.isDraw()) {
            return;
        }

        resultPen = ResultCalculator.calcPen();
    }

    @Override
    public Result getResultMeet() {
        return null;
    }

    @Override
    public boolean isWinnerHomeTeam() {
        var goalsFirstTeam = getGoalsFirstTeamMainTime();
        var goalsSecondTeam = getGoalsSecondTeamMain();

        if(goalsFirstTeam != goalsSecondTeam) {
            return goalsFirstTeam > goalsSecondTeam;
        }

        if(!resultAdd.isDraw()) {
            return resultAdd.getGoalAway() > resultAdd.getGoalHome();
        }

        return resultPen.getGoalAway() > resultPen.getGoalHome();
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public SimpleTeam getWinner() {
        if(isWinnerHomeTeam()) {
            return getTeamHome();
        }
        return getTeamAway();
    }

    @Override
    public SimpleTeam getLoser() {
        if(isWinnerHomeTeam()) {
            return getTeamAway();
        }
        return getTeamHome();
    }

    private Result getResultMeetWOPen() {
        var goalsFor = getGoalsFirstTeamMainTime();
        var goalsAway = getGoalsSecondTeamMain();

        if(resultAdd != null) {
            goalsFor += resultAdd.getGoalAway();
            goalsAway += resultAdd.getGoalHome();
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
    public boolean isWinnerHomeTeamFirstMeet() {
        return firstMeet.isWinnerHomeTeamWOPen();
    }

    @Override
    public boolean isDrawFirstMeet() {
        return firstMeet.isDrawWOPen();
    }

    @Override
    public boolean isWinnerHomeTeamSecondMeet() {
        return !secondMeet.isWinnerHomeTeamWOPen() && !secondMeet.isDraw(); //because in second meet teams in reverse order
    }

    @Override
    public boolean isDrawSecondMeet() {
        return secondMeet.isDraw();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(firstMeet);
        res.append(System.lineSeparator());
        res.append(secondMeet);

        if(resultAdd != null) {
            res.append(", add ");
            res.append(resultAdd);
        }

        if(resultPen != null) {
            res.append(", pen ");
            res.append(resultPen);
        }

        return res.toString();
    }
}
