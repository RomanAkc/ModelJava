package fss.model;

import java.util.ArrayList;

public class SchemePart {
    public enum Source {
        NO,
        FROM_OUT,
        PREV_STAGE
    }

    public int ID = 0;
    public String name = null;
    public int cntRound = 0;
    public StagePool.StageType stageType;
    public ArrayList<TeamsSource> teamSources = new ArrayList<>();
    public int cntGroups = 0;
    public RatingType ratingType = RatingType.STANDART;

    public SchemePart(int ID, String name, int cntRound, StagePool.StageType stageType) {
        this.ID = ID;
        this.name = name;
        this.cntRound = cntRound;
        this.stageType = stageType;
    }

    public SchemePart(int ID, String name, int cntRound, int cntGroups) {
        this.ID = ID;
        this.name = name;
        this.cntRound = cntRound;
        this.stageType = StagePool.StageType.GROUPS;
        this.cntGroups = cntGroups;
    }
}
