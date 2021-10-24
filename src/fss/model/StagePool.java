package fss.model;

import java.util.ArrayList;

abstract class StagePool {
    protected StageType stageType = StageType.PLAYOFF;
    protected String name;
    private ArrayList<SimpleTeam> teams = null;
    protected Ratingable rating = null;
    protected int cntRounds = 0;

    public StagePool(StageType stageType, String name, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        this.stageType = stageType;
        this.teams = new ArrayList<>(teams);
        this.rating = rating;
        this.name = name;
        this.cntRounds = cntRounds;
    }

    public ArrayList<SimpleTeam> getTeams() {
        return new ArrayList<>(teams);
    }

    public StageType getStageType() {
        return stageType;
    }

    public abstract void calc();
    public abstract ArrayList<SimpleTeam> getWinners();
    public abstract ArrayList<SimpleTeam> getLosers();
    public abstract ArrayList<Meet> getMeetings();
}

abstract class CommonRoundRobinGroupsStagePool extends StagePool {
    protected ArrayList<Table.WinRules> rules = null;

    public CommonRoundRobinGroupsStagePool(StageType stageType, String name, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        super(stageType, name, teams, rating, cntRounds);
    }

    public void addWinRules(ArrayList<Table.WinRules> rules) {
        this.rules = rules;
    }
    public abstract ArrayList<SimpleTeam> getN(int n);
}

abstract class BaseRoundRobinStagePool extends CommonRoundRobinGroupsStagePool {
    public BaseRoundRobinStagePool(String name, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        super(StageType.CIRCLE, name, teams, rating, cntRounds);
    }

    public abstract ArrayList<SimpleTeam> getFirstN(int cnt);
    public abstract ArrayList<SimpleTeam> getLastN(int cnt);
    public abstract ArrayList<Table.Row> getFinalTableRows();
}

abstract class BasePlayOffStagePool extends StagePool {
    public BasePlayOffStagePool(String name, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        super(StageType.PLAYOFF, name, teams, rating, cntRounds);
    }
}

abstract class BaseGroupsStagePool extends CommonRoundRobinGroupsStagePool {
    protected int cntGroups = 0;

    public BaseGroupsStagePool(String name, int cntGroups, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        super(StageType.GROUPS, name, teams, rating, cntRounds);
        this.cntGroups = cntGroups;
    }

    public abstract ArrayList<ArrayList<SimpleTeam>> getGroupTeams();
}
