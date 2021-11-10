package fss.model;

class WinTwoMeet implements WinGameable {
    private Meet firstMeet = null;
    private Meet secondMeet = null;
    private Result resultAdd = null;
    private Result resultPen = null;

    public WinTwoMeet(SimpleTeam firstTeam, SimpleTeam secondTeam) {
        firstMeet = new Meet(firstTeam, secondTeam);
        secondMeet = new Meet(secondTeam, firstTeam);
    }

    @Override
    public SimpleTeam getTeamHome() {
        return null;
    }

    @Override
    public SimpleTeam getTeamAway() {
        return null;
    }

    //TODO: продолжить переписывать




    private int getGoalsFirstTeamMain() {
        return firstMeet.getResultMeet().getGoalHome() + super.getResultMeet().getGoalAway();
    }

    private int getGoalsSecondTeamMain() {
        return firstMeet.getResultMeet().getGoalAway() + super.getResultMeet().getGoalHome();
    }

    private boolean checkIsDraw() {
        if(getGoalsFirstTeamMain() != getGoalsSecondTeamMain()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isWinnerHomeTeam() {
        return getResultMeet().isWin();
    }

    @Override
    public SimpleTeam getWinner() {
        if(isWinnerHomeTeam()) {
            return getTeamAway(); //because teams in base class are in reverse order
        }
        return getTeamHome(); //because teams in base class are in reverse order
    }

    @Override
    public SimpleTeam getLoser() {
        if(isWinnerHomeTeam()) {
            return getTeamHome(); //because teams in base class are in reverse order
        }
        return getTeamAway(); //because teams in base class are in reverse order
    }

    @Override
    public void calc() {
        firstMeet.calcUseOwner();
        super.calcUseOwner();

        if(!checkIsDraw()) {
            return;
        }

        resultAdd = ResultCalculator.calcAddTime(getTeamHome().getPower(), getTeamAway().getPower());
        if(!resultAdd.isDraw()) {
           return;
        }

        resultPen = ResultCalculator.calcPen().reverse();
    }

    @Override
    public Result getResultMeet() {
        var resultWOPen = getResultMeetWOPen();
        if(resultPen != null) {
            return new Result(resultWOPen.getGoalHome() + resultPen.getGoalAway()
                    , resultWOPen.getGoalAway() + resultPen.getGoalHome());
        }
        return resultWOPen;
    }

    @Override
    public boolean isWinnerHomeTeamWOPen() {
        return getResultMeetWOPen().isWin();
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public boolean isDrawWOPen() {
        return getResultMeetWOPen().isDraw();
    }

    public boolean isWinnerHomeTeamFirstMeet() {
        return firstMeet.isWinnerHomeTeamWOPen();
    }

    public boolean isDrawFirstMeet() {
        return firstMeet.isDrawWOPen();
    }

    public boolean isWinnerHomeTeamSecondMeet() {
        return super.getResultMeet().getGoalAway() + resultAdd.getGoalAway()
                > super.getResultMeet().getGoalHome() + resultAdd.getGoalHome();
    }

    public boolean isDrawSecondMeet() {
        return super.getResultMeet().getGoalAway() + resultAdd.getGoalAway()
                == super.getResultMeet().getGoalHome() + resultAdd.getGoalHome();
    }

    @Override
    public String toString() {
        var res = new StringBuffer();
        res.append(firstMeet.toString());
        res.append(System.lineSeparator());
        res.append(super.toString());

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

    private Result getResultMeetWOPen() {
        var goalsFor = getGoalsFirstTeamMain();
        var goalsAway = getGoalsSecondTeamMain();

        if(resultAdd != null) {
            goalsFor += resultAdd.getGoalAway();
            goalsAway += resultAdd.getGoalHome();
        }

        return new Result(goalsFor, goalsAway);
    }
}
