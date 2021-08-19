package fss.model;

import java.util.ArrayList;

public abstract class StagePool {
    public enum StageType {
        CIRCLE,
        GROUPS,
        PLAYOFF
    }

    protected StageType stageType = StageType.PLAYOFF;
    protected String name;
    protected int cntGroups = 0;
    protected ArrayList<SimpleTeam> teams = null;
    protected Rating rating = null;
    protected int cntRounds = 0;

    public StagePool(StageType stageType, String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        this.stageType = stageType;
        this.teams = new ArrayList<>(teams);
        this.rating = rating;
        this.name = name;
        this.cntRounds = cntRounds;
    }

    public StagePool(String name, int cntGroups, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        this(StageType.GROUPS, name, teams, rating, cntRounds);
        this.cntGroups = cntGroups;
    }

    public abstract void calc();
    public abstract ArrayList<SimpleTeam> getWinners();
    public abstract ArrayList<SimpleTeam> getLosers();
    public abstract ArrayList<SimpleTeam> getFirstN(int cnt);
    public abstract ArrayList<SimpleTeam> getLastN(int cnt);
    public abstract SimpleTeam getN(int n);
}
