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
        if(getGoalsFirstTeamMainTime() != getGoalsSecondTeamMain()) {
            return false;
        }

        return true;
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

    //TODO: продолжить переписывать
















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


}
