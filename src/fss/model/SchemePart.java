package fss.model;

import java.util.ArrayList;

public class SchemePart {
    public enum Source {
        NO,
        FROM_OUT,
        PREV_STAGE
    }

    public enum TypeSourcePrev {
        NO,
        WINNERS,
        LOSERS,
        N_FIRST,
        N_LAST,
        N_TEAM
    }

    public int ID = 0;
    public String name = null;
    public int cntRound = 0;
    public BaseStagePool.StageType stageType;
    public ArrayList<TeamsSource> teamSources = new ArrayList<>();
    public int cntGroups = 0;

    public SchemePart(int ID, String name, int cntRound, BaseStagePool.StageType stageType) {
        this.ID = ID;
        this.name = name;
        this.cntRound = cntRound;
        this.stageType = stageType;
    }

    public  void setCntGroups(int cntGroups) {
        this.cntGroups = cntGroups;
    }
}
