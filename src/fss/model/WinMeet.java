package fss.model;

public class WinMeet implements WinGameable {
    private Meet meet = null;
    private Result resultAdd = null;
    private Result resultPen = null;

    public WinMeet(SimpleTeam teamHome, SimpleTeam teamAway) {
        meet = new Meet(teamHome, teamAway);
    }

    @Override
    public SimpleTeam getTeamHome() {
        return meet.getTeamHome();
    }

    @Override
    public SimpleTeam getTeamAway() {
        return meet.getTeamAway();
    }

    @Override
    public void calc() {
        meet.calc();

        if(!meet.isDraw()) {
            return;
        }

        resultAdd = ResultCalculator.calcAddTime(getTeamHome().getPower(), getTeamAway().getPower());
        if(!resultAdd.isDraw()) {
            return;
        }

        resultPen = ResultCalculator.calcPen();
    }

    @Override
    public Result getResultMeet() {
        return meet.getResultMeet();
    }

    private Result getResultMeetWOPen() {
        var goalsFor = meet.getResultMeet().getGoalHome();
        var goalsAway = meet.getResultMeet().getGoalAway();
        if(resultAdd != null) {
            goalsFor += resultAdd.getGoalHome();
            goalsAway += resultAdd.getGoalAway();
        }
        return new Result(goalsFor, goalsAway);
    }

    @Override
    public boolean isWinnerHomeTeam() {
        var resultWOPen = getResultMeetWOPen();
        if(resultPen != null) {
            return new Result(resultWOPen.getGoalHome() + resultPen.getGoalHome()
                    , resultWOPen.getGoalAway() + resultPen.getGoalAway()).isWin();
        }
        return resultWOPen.isWin();
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

    @Override
    public boolean isDrawWOPen() {
        return getResultMeetWOPen().isDraw();
    }

    @Override
    public boolean isWinnerHomeTeamWOPen() {
        return getResultMeetWOPen().isWin();
    }

    @Override
    public String toString() {
        var res = new StringBuffer();
        res.append(meet.toString());

        if(resultAdd != null) {
            res.append(", add ");
            res.append(resultAdd.toString());
        }

        if(resultPen != null) {
            res.append(", pen ");
            res.append(resultPen.toString());
        }

        return res.toString();
    }
}
