package fss.model;

public class Meet {
    private boolean alreadyCalculated = false;
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
        if(alreadyCalculated) {
            //TODO: бросить исключение
            return new Result(-1, -1);
        }

        var res = useOwner ? ResultCalculator.calcUseOwner(teamHome.getPower(), teamAway.getPower())
                : ResultCalculator.calc(teamHome.getPower(), teamAway.getPower());
        alreadyCalculated = true;

        return res;
    }

    boolean isAlreadyCalculated() {
        return alreadyCalculated;
    }

    @Override
    public String toString() {
        var res = new StringBuffer();
        res.append(teamHome.getName());
        res.append(" - ");
        res.append(teamAway.getName());

        if (alreadyCalculated) {
            res.append(" ");
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

    public SimpleTeam getWinner() {
        if(result.isWin()) {
            return teamHome;
        }

        if(result.isLose()) {
            return teamAway;
        }

        return null;
    }

    public SimpleTeam getLooser() {
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
    private Result resultAdd;
    private Result resultPen;

    public WinMeet(SimpleTeam teamHome, SimpleTeam teamAway) {
        super(teamHome, teamAway);
    }

    @Override
    public boolean isWinnerHomeTeam() {
        if(!getResultMeet().isDraw()) {
            if(getResultMeet().isWin()) {
                return true;
            }
            return false;
        }

        if(!resultAdd.isDraw()) {
            if(resultAdd.isWin()) {
                return true;
            }
            return false;
        }

        if(resultPen.isWin()) {
            return true;
        }
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
    public void calc() {
        if(isAlreadyCalculated()) {
            return;
        }

        super.calc();
        if(getResultMeet().isDraw()) {
            resultAdd = calculate(false);
        }

        if(resultAdd.isDraw()) {
            resultPen = ResultCalculator.calcPen();
        }
    }

    @Override
    public String toString() {
        var res = new StringBuffer();
        res.append(super.toString());
        res.append(", add ");
        res.append(resultAdd.toString());

        if(resultAdd.isDraw()) {
            res.append(", pen ");
            res.append(resultAdd.toString());
        }

        return res.toString();
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

    private int getGoalsFirstTeam() {
        return firstMeet.getResultMeet().getGoalHome() + super.getResultMeet().getGoalAway();
    }

    private int getGoalsSecondTeam() {
        return firstMeet.getResultMeet().getGoalAway() + super.getResultMeet().getGoalHome();
    }

    private boolean isDraw() {
        if(getGoalsFirstTeam() != getGoalsSecondTeam()) {
            return false;
        }

        if(firstMeet.getResultMeet().getGoalAway() != super.getResultMeet().getGoalAway()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isWinnerHomeTeam() {
        if(getGoalsFirstTeam() != getGoalsSecondTeam()) {
            return getGoalsFirstTeam() < getGoalsSecondTeam();
        }

        if(firstMeet.getResultMeet().getGoalAway() != super.getResultMeet().getGoalAway()) {
            return super.getResultMeet().getGoalAway() < firstMeet.getResultMeet().getGoalAway();
        }

        return resultPen.isLose();
    }

    @Override
    public SimpleTeam getWinner() {
        if(isWinnerHomeTeam()) {
            return getTeamHome();
        }
        return getTeamAway();
    }

    @Override
    public void calc() {
        if(isAlreadyCalculated()) {
            return;
        }

        firstMeet.calcUseOwner();
        super.calcUseOwner();

        if(!isDraw()) {
            return;
        }

        resultAdd = ResultCalculator.calcAddTime(getTeamAway().getPower(), getTeamHome().getPower());
        if(resultAdd.getGoalHome() != 0) {
           return;
        }

        resultPen = ResultCalculator.calcPen();
    }

    @Override
    public String toString() {
        var res = new StringBuffer();
        res.append(firstMeet.toString());
        res.append(System.lineSeparator());
        res.append(super.toString());
        return res.toString();
    }
}
