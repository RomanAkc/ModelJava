package fss.model;

public class Meet {
    private boolean alreadyCalculated = false;
    private SimpleTeam teamHome = null;
    private SimpleTeam teamAway= null;
    private Result result= null;

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

    public String getString() {
        return teamHome.getName() + " - " + teamAway.getName() + " " + result.getString();
    }

    public SimpleTeam getTeamHome() {
        return teamHome;
    }

    public SimpleTeam getTeamAway() {
        return teamAway;
    }
}

class WinMeet extends Meet {
    private Result resultAdd;
    private Result resultPen;

    public WinMeet(SimpleTeam teamHome, SimpleTeam teamAway) {
        super(teamHome, teamAway);
    }
}

class WinTwoMeet {
    private Meet firstMeet;
    private WinMeet secondMeet;
}
