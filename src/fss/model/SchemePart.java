package fss.model;

import java.util.ArrayList;

public class SchemePart {
    public enum Source {
        NO,
        FROM_OUT,
        PREV_STAGE
    }

    public final int ID;
    public final String name;
    public final int cntRound;
    public final StageType stageType;
    public ArrayList<TeamsSource> teamSources = new ArrayList<>();
    public int cntGroups = 0;
    public RatingType ratingType = RatingType.STANDART;

    public SchemePart(int ID, String name, int cntRound, StageType stageType) {
        this.ID = ID;
        this.name = name;
        this.cntRound = cntRound;
        this.stageType = stageType;
    }

    public SchemePart(int ID, String name, int cntRound, int cntGroups) {
        this.ID = ID;
        this.name = name;
        this.cntRound = cntRound;
        this.stageType = StageType.GROUPS;
        this.cntGroups = cntGroups;
    }
}
