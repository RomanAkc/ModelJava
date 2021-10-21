package fss.model;

import java.util.HashMap;

class UEFARatingCalculator {
    private HashMap<Integer, HashMap<Integer, UEFARatingSchemePart>> ratingScheme = new HashMap<>();
    private BaseTournament tournament1stLevel = null;
    private BaseTournament tournament2ndLevel = null;
    private BaseTournament tournament3rdLevel = null;

    private class PairPoints {
        public double homePoint = 0;
        public double awayPoint = 0;

        public PairPoints(double homePoint, double awayPoint) {
            this.homePoint = homePoint;
            this.awayPoint = awayPoint;
        }
    }

    public UEFARatingCalculator() {

    }

    public void addRatingScheme(UEFARatingSchemePart schemePart) {
        var scheme = getSchemeForTournament(schemePart.tournamentID);
        scheme.put(schemePart.stageID, schemePart);
    }

    public void setTournament1stLevel(BaseTournament tournament1stLevel) {
        this.tournament1stLevel = tournament1stLevel;
    }

    public void setTournament2ndLevel(BaseTournament tournament2ndLevel) {
        this.tournament2ndLevel = tournament2ndLevel;
    }

    public void setTournament3rdLevel(BaseTournament tournament3rdLevel) {
        this.tournament3rdLevel = tournament3rdLevel;
    }

    public void calc() {
        HashMap<SimpleTeam, Double> pointsByTeam = new HashMap<>();
        calcForTournament(tournament1stLevel, pointsByTeam);
    }

    private void calcForTournament(BaseTournament tournament, HashMap<SimpleTeam, Double> pointsByTeam) {
        var scheme = getSchemeForTournament(tournament.getId());

        for(int i = 0; i < tournament.getCntStagePool(); ++i) {
            var id = tournament.getStageID(i);
            var schemePart = getSchemePart(scheme, id);
            boolean isHalfPoint = schemePart.ratingStageType == UEFARatingStageType.QUALIFICATION;
            var stageType = tournament.getStageType(i);

            if(stageType == StageType.PLAYOFF) {
                var meetings = tournament.getStageMeetings(id);

                for(var meet : meetings) {
                    if(!pointsByTeam.containsKey(meet.getTeamHome())) {
                        pointsByTeam.put(meet.getTeamHome(), 0.0);
                    }
                    if(!pointsByTeam.containsKey(meet.getTeamAway())) {
                        pointsByTeam.put(meet.getTeamAway(), 0.0);
                    }

                    var pH = pointsByTeam.get(meet.getTeamHome());
                    var pA = pointsByTeam.get(meet.getTeamAway());

                    //pH += schemePart.addPoint + (meet.isWinnerHomeTeamWOPen() ? (isHalfPoint ? 1.0 : 2.0) : 0.0);

                    //meet.isWinnerHomeTeamWOPen()
                }
            }



        }

    }

    private PairPoints getPointsByMeet(Meet meet, boolean isHalfPoint) {
        return new PairPoints(0, 0);
    }

    private UEFARatingSchemePart getSchemePart(HashMap<Integer, UEFARatingSchemePart> tournamentScheme, int stageID) {
        if(tournamentScheme.containsKey(stageID))
            return tournamentScheme.get(stageID);

        return null;
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
