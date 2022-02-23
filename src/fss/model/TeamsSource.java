package fss.model;

public class TeamsSource {
    public TeamsSource() {
        source = SchemePart.Source.FROM_OUT;
    }

    public TeamsSource(int sourcePrevID, TypeSource typeSource) {
        this.source = SchemePart.Source.PREV_STAGE;
        this.sourcePrevID = sourcePrevID;
        this.typeSource = typeSource;
    }

    public TeamsSource(int sourcePrevID, TypeSource typeSource, int cntOrNTeamTypeSourcePrev) {
        this.source = SchemePart.Source.PREV_STAGE;
        this.sourcePrevID = sourcePrevID;
        this.typeSource = typeSource;
        if(typeSource == TypeSource.N_TEAM)
            this.teamN = cntOrNTeamTypeSourcePrev;
        else
            this.cntTeam = cntOrNTeamTypeSourcePrev;
    }



    public SchemePart.Source source;
    public int sourcePrevID = -1;
    public TypeSource typeSource = TypeSource.NO;
    public int cntTeam = 0;
    public int teamN = 0;
}
