package fss.model;

import java.util.HashMap;

public class UEFARatingCalculator {
    private HashMap<Integer, HashMap<Integer, UEFARatingSchemePart>> ratingScheme = new HashMap<>();
    private Tournament tournament1stLevel = null;
    private Tournament tournament2ndLevel = null;
    private Tournament tournament3rdLevel = null;

    public UEFARatingCalculator() {

    }

    public void addRatingScheme(UEFARatingSchemePart schemePart) {
        var scheme = getSchemeForTournament(schemePart.tournamentID);
        scheme.put(schemePart.stageID, schemePart);
    }

    public void setTournament1stLevel(Tournament tournament1stLevel) {
        this.tournament1stLevel = tournament1stLevel;
    }

    public void setTournament2ndLevel(Tournament tournament2ndLevel) {
        this.tournament2ndLevel = tournament2ndLevel;
    }

    public void setTournament3rdLevel(Tournament tournament3rdLevel) {
        this.tournament3rdLevel = tournament3rdLevel;
    }

    private HashMap<Integer, UEFARatingSchemePart> getSchemeForTournament(int tournamentID) {
        if(ratingScheme.containsKey(tournamentID)) {
            return ratingScheme.get(tournamentID);
        }

        var scheme = new HashMap<Integer, UEFARatingSchemePart>();
        ratingScheme.put(tournamentID, scheme);
        return scheme;
    }


}
