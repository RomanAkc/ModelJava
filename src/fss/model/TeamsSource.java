package fss.model;

public class TeamsSource {
    public TeamsSource() {
        source = SchemePart.Source.FROM_OUT;
    }

    public TeamsSource(int sourcePrevID, SchemePart.TypeSourcePrev typeSourcePrev) {
        this.source = SchemePart.Source.PREV_STAGE;
        this.sourcePrevID = sourcePrevID;
        this.typeSourcePrev = typeSourcePrev;
    }

    public TeamsSource(int sourcePrevID, SchemePart.TypeSourcePrev typeSourcePrev, int cntOrNTeamTypeSourcePrev) {
        this.source = SchemePart.Source.PREV_STAGE;
        this.sourcePrevID = sourcePrevID;
        this.typeSourcePrev = typeSourcePrev;
        if(typeSourcePrev == SchemePart.TypeSourcePrev.N_TEAM)
            this.teamN = cntOrNTeamTypeSourcePrev;
        else
            this.cntTeam = cntOrNTeamTypeSourcePrev;
    }



    public SchemePart.Source source = SchemePart.Source.NO;
    public int sourcePrevID = -1;
    public SchemePart.TypeSourcePrev typeSourcePrev = SchemePart.TypeSourcePrev.NO;
    public int cntTeam = 0;
    public int teamN = 0;
}
