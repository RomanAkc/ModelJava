package fss.model;

class Meet {
    private SimpleTeam teamHome = null;
    private SimpleTeam teamAway = null;
    private Result result = null;

    public Meet(SimpleTeam teamHome, SimpleTeam teamAway) {
        this.teamHome = teamHome;
        this.teamAway = teamAway;
    }

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

    public SimpleTeam getTeamHome() {
        return teamHome;
    }

    public SimpleTeam getTeamAway() {
        return teamAway;
    }

    public Result getResultMeet() {
        return result;
    }

    public boolean isWinnerHomeTeam() {
        return result.isWin();
    }

    public boolean isWinnerHomeTeamWOPen() {
        return isWinnerHomeTeam();
    }

    public SimpleTeam getWinner() {
        if(result.isWin()) {
            return teamHome;
        }

        if(result.isLose()) {
            return teamAway;
        }

        return null;
    }

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

class WinMeet extends Meet {
    private Result resultAdd = null;
    private Result resultPen = null;

    public WinMeet(SimpleTeam teamHome, SimpleTeam teamAway) {
        super(teamHome, teamAway);
    }

    @Override
    public boolean isWinnerHomeTeam() {
        return getResultMeet().isWin();
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
    public void calc() {
        super.calc();

        if(!getResultMeet().isDraw()) {
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
        var resultWOPen = getResultMeetWOPen();

        if(resultPen != null) {
            return new Result(resultWOPen.getGoalHome() + resultPen.getGoalHome()
                    , resultWOPen.getGoalAway() + resultPen.getGoalAway());
        }

        return resultWOPen;
    }

    @Override
    public boolean isWinnerHomeTeamWOPen() {
        return isWinnerHomeTeam();
    }

    @Override
    public String toString() {
        var res = new StringBuffer();
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
        var goalsFor = super.getResultMeet().getGoalHome();
        var goalsAway = super.getResultMeet().getGoalAway();
        if(resultAdd != null) {
            goalsFor += resultAdd.getGoalHome();
            goalsAway += resultAdd.getGoalAway();
        }
        return new Result(goalsFor, goalsAway);
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

    private boolean isDraw() {
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

        if(!isDraw()) {
            return;
        }

        resultAdd = ResultCalculator.calcAddTime(getTeamAway().getPower(), getTeamHome().getPower());
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
