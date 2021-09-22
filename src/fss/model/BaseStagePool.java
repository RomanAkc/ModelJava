package fss.model;

import java.util.ArrayList;

public abstract class BaseStagePool {
    public enum StageType {
        CIRCLE,
        GROUPS,
        PLAYOFF
    }

    protected StageType stageType = StageType.PLAYOFF;
    protected String name;
    protected ArrayList<SimpleTeam> teams = null;
    protected Rating rating = null;
    protected int cntRounds = 0;

    public BaseStagePool(StageType stageType, String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        this.stageType = stageType;
        this.teams = new ArrayList<>(teams);
        this.rating = rating;
        this.name = name;
        this.cntRounds = cntRounds;
    }

    public StageType getStageType() {
        return stageType;
    }

    public abstract void calc();
    public abstract ArrayList<SimpleTeam> getWinners();
    public abstract ArrayList<SimpleTeam> getLosers();
}

abstract class AbstractRoundRobinStagePool extends BaseStagePool {
    protected ArrayList<Table.WinRules> rules = null;

    public AbstractRoundRobinStagePool(StageType stageType, String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(stageType, name, teams, rating, cntRounds);
    }

    public void addWinRules(ArrayList<Table.WinRules> rules) {
        this.rules = rules;
    }
    public abstract ArrayList<SimpleTeam> getN(int n);
}

abstract class BaseRoundRobinStagePool extends AbstractRoundRobinStagePool {
    public BaseRoundRobinStagePool(String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(StageType.CIRCLE, name, teams, rating, cntRounds);
    }

    public abstract ArrayList<SimpleTeam> getFirstN(int cnt);
    public abstract ArrayList<SimpleTeam> getLastN(int cnt);
}

abstract class BasePlayOffStagePool extends BaseStagePool {
    public BasePlayOffStagePool(String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(StageType.PLAYOFF, name, teams, rating, cntRounds);
    }
}

abstract class BaseGroupsStagePool extends AbstractRoundRobinStagePool {
    protected int cntGroups = 0;

    public BaseGroupsStagePool(String name, int cntGroups, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(StageType.GROUPS, name, teams, rating, cntRounds);
        this.cntGroups = cntGroups;
    }
}

//Написать наследников для BaseRoundRobinStagePool, BaseRoundRobinGroupsStagePool, BaseStagePoolPlayOff
//Использовать их в турнире
//Проверить тесты на StagePoolImpl
