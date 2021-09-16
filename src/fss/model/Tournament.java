package fss.model;

import java.util.ArrayList;

public abstract class Tournament {
    protected String name = null;

    public Tournament(String name) {
        this.name = name;
    }

    public abstract void addScheme(Scheme scheme);
    public abstract void addTeamsToStage(int nID, ArrayList<SimpleTeam> teams) ;
    public abstract void addRating(Rating rating);
    public abstract void addWinRules(ArrayList<Table.WinRules> rules);

    public abstract void calc();
}
