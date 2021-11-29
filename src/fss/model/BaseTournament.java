package fss.model;

import java.util.ArrayList;

public abstract class BaseTournament {
    private int id = 0;
    protected String name = null;

    public BaseTournament(String name) {
        this.name = name;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public abstract void addScheme(Scheme scheme);
    public abstract void addTeamsToStage(int stageID, ArrayList<SimpleTeam> teams) ;
    public abstract void addRating(Ratingable rating);
    public abstract void addWinRules(ArrayList<Table.WinRules> rules);

    public abstract void calc();

    public abstract int getCntStagePool();
    public abstract int getStageID(int stagePoolIndex);
    public abstract StagePool getStagePoolByStageID(int stageID);

    public abstract ArrayList<Table.Row> getFinalTableRows(int stageID);
    public abstract ArrayList<SimpleTeam> getFirstStageTeams(int stageID, int cntTeam);
    public abstract ArrayList<SimpleTeam> getLastStageTeams(int stageID, int cntTeam);
    public abstract ArrayList<SimpleTeam> getNTeamStageTeams(int stageID, int nTeam);
    public abstract ArrayList<SimpleTeam> getWinnersStageTeams(int stageID);
    public abstract ArrayList<SimpleTeam> getLosersStageTeams(int stageID);
    public abstract ArrayList<SimpleTeam> getAllTournamentTeams();
}
