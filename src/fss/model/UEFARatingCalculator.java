package fss.model;

import java.util.HashMap;

class UEFARatingCalculator {
    private HashMap<Integer, HashMap<Integer, UEFARatingSchemePart>> ratingScheme = new HashMap<>();
    private BaseTournament tournament1stLevel = null;
    private BaseTournament tournament2ndLevel = null;
    private BaseTournament tournament3rdLevel = null;
    private HashMap<SimpleTeam, Double> pointsByTeam = new HashMap<>();
    private HashMap<Country, Double> pointsByCountry = new HashMap<>();

    private class PairPoints {
        public double homePoint = 0;
        public double awayPoint = 0;
    }

    public UEFARatingCalculator() {}

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
        calcForTournament(tournament1stLevel);
        calcForTournament(tournament2ndLevel);
        calcForTournament(tournament3rdLevel);
        calcCountries();
    }

    public HashMap<SimpleTeam, Double> getPointsByTeam() {
        return pointsByTeam;
    }

    public HashMap<Country, Double> getPointsByCountry() {
        return pointsByCountry;
    }

    private void calcForTournament(BaseTournament tournament) {
        if(tournament == null)
            return;

        var scheme = getSchemeForTournament(tournament.getID());

        for(int i = 0; i < tournament.getCntStagePool(); ++i) {
            var id = tournament.getStageID(i);
            var stage = tournament.getStagePoolByStageID(id);
            var schemePart = getSchemePart(scheme, id);

            addBonusPoints(schemePart, stage, pointsByTeam);

            boolean isHalfPoint = schemePart.ratingStageType == UEFARatingStageType.QUALIFICATION;
            var meetings = stage.getMeetings();

            for(var meet : meetings) {
                var points = getPointsByMeet(meet, isHalfPoint);
                addPointsToPointsByTeam(meet.getTeamHome(), points.homePoint, pointsByTeam);
                addPointsToPointsByTeam(meet.getTeamAway(), points.awayPoint, pointsByTeam);
            }
        }
    }

    private void calcCountries() {
        for(var kv : pointsByTeam.entrySet()) {
            var country = kv.getKey().getCountry();
            var value = kv.getValue();

            if(pointsByCountry.containsKey(country)) {
                pointsByCountry.put(country, pointsByCountry.get(country) + value);
            } else {
                pointsByCountry.put(country, value);
            }
        }

    }

    private void addBonusPoints(UEFARatingSchemePart schemePart, StagePool stage, HashMap<SimpleTeam, Double> pointsByTeam) {
        if(schemePart.bonusPoint > 0) {
            for (var team : stage.getTeams()) {
                addPointsToPointsByTeam(team, schemePart.bonusPoint, pointsByTeam);
            }
        }
    }

    private void addPointsToPointsByTeam(SimpleTeam team, double points, HashMap<SimpleTeam, Double> pointsByTeam) {
        if(!pointsByTeam.containsKey(team)) {
            pointsByTeam.put(team, points);
        } else {
            pointsByTeam.put(team, pointsByTeam.get(team) + points);
        }
    }

    private PairPoints getPointsByMeet(Meet meet, boolean isHalfPoint) {
        var result = new PairPoints();

        if(meet instanceof WinTwoMeet) {
            var twoMeet = (WinTwoMeet)meet;
            AddPointByResult(twoMeet.isWinnerHomeTeamFirstMeet(), twoMeet.isDrawFirstMeet(), result);
            AddPointByResult(twoMeet.isWinnerHomeTeamSecondMeet(), twoMeet.isDrawSecondMeet(), result);
        } else {
            AddPointByResult(meet.isWinnerHomeTeamWOPen(), meet.isDrawWOPen(), result);
        }

        return ModifyPointIfNeed(isHalfPoint, result);
    }

    private PairPoints ModifyPointIfNeed(boolean isHalfPoint, PairPoints result) {
        if(isHalfPoint) {
            result.homePoint /= 2;
            result.awayPoint /= 2;
        }
        return result;
    }

    private void AddPointByResult(boolean isWinnerHome, boolean isDraw, PairPoints result) {
        if(isWinnerHome) {
            result.homePoint += 2;
        } else if(isDraw) {
            result.homePoint += 1;
            result.awayPoint += 1;
        } else {
            result.awayPoint += 2;
        }
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
