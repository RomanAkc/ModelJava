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

