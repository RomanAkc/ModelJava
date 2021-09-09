package fss.model;

import java.util.ArrayList;

public class SchemePart {
    public enum Source {
        NO,
        FROM_OUT,
        PREV_STAGE
    }

    public class TeamsSource {
        public Source source = Source.NO;
        public int sourcePrevID = -1;
        public int cntTeam = 0;
    }

    public int ID;
    public StagePool.StageType stageType;
    public ArrayList<TeamsSource> teamSources = new ArrayList<>();

    public SchemePart(int ID, StagePool.StageType stageType) {
        this.ID = ID;
        this.stageType = stageType;
    }
}
