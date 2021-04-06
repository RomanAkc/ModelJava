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
        calculate(false);
    }

    public void calcUseOwner() {
        calculate(true);
    }

    private void calculate(boolean useOwner) {
        if(alreadyCalculated) {
            return;
        }

        result = useOwner ? ResultCalculator.calcUseOwner(teamHome.getPower(), teamAway.getPower())
                : ResultCalculator.calc(teamHome.getPower(), teamAway.getPower());
        alreadyCalculated = true;
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

    public Result getResult() {
        return result;
    }
}

class WinMeet extends Meet {
    private Result resultAdd;
    private Result resultPen;

    public WinMeet(SimpleTeam teamHome, SimpleTeam teamAway) {
        super(teamHome, teamAway);
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

class WinTwoMeet {
    private Meet firstMeet = null;
    private WinMeet secondMeet = null;

    @Override
    public String toString() {
        var res = new StringBuffer();
        res.append(firstMeet.toString());
        res.append(System.lineSeparator());
        res.append(secondMeet.toString());
        return res.toString();
    }
}
