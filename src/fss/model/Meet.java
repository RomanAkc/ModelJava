package fss.model;

class Meet implements Gameable {
    private SimpleTeam teamHome = null;
    private SimpleTeam teamAway = null;
    private Result result = null;

    public Meet(SimpleTeam teamHome, SimpleTeam teamAway) {
        this.teamHome = teamHome;
        this.teamAway = teamAway;
    }

    @Override
    public void calc() {
        result = calculate(false);
    }

    public void calcUseOwner() {
        result = calculate(true);
    }

    protected Result calculate(boolean useOwner) {
        if(useOwner)
            return ResultCalculator.calcUseOwner(teamHome.getPower(), teamAway.getPower());

        return ResultCalculator.calc(teamHome.getPower(), teamAway.getPower());
    }

    @Override
    public String toString() {
        var res = new StringBuffer();
        res.append(teamHome.getName());
        res.append(" - ");
        res.append(teamAway.getName());
        res.append(" ");
        if(result != null) {
            res.append(result.toString());
        }
        return  res.toString();
    }

    @Override
    public SimpleTeam getTeamHome() {
        return teamHome;
    }

    @Override
    public SimpleTeam getTeamAway() {
        return teamAway;
    }

    @Override
    public Result getResultMeet() {
        return result;
    }

    @Override
    public boolean isWinnerHomeTeam() {
        return result.isWin();
    }

    public boolean isWinnerHomeTeamWOPen() {
        return isWinnerHomeTeam();
    }

    @Override
    public boolean isDraw() {
        return result.isDraw();
    }

    public boolean isDrawWOPen() {
        return isDraw();
    }

    @Override
    public SimpleTeam getWinner() {
        if(result.isWin()) {
            return teamHome;
        }

        if(result.isLose()) {
            return teamAway;
        }

        return null;
    }

    @Override
    public SimpleTeam getLoser() {
        if(result.isWin()) {
            return teamAway;
        }

        if(result.isLose()) {
            return teamHome;
        }

        return null;
    }
}

class WinTwoMeet extends Meet {
    private Meet firstMeet = null;
    private Result resultAdd = null;
    private Result resultPen = null;

    public WinTwoMeet(SimpleTeam teamHome, SimpleTeam teamAway) {
        super(teamAway, teamHome);
        firstMeet = new Meet(teamHome, teamAway);
    }

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
