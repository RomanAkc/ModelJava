package fss.model;

import com.sun.source.tree.BreakTree;

import java.util.HashMap;

class UEFARatingCalculator {
    private HashMap<Integer, HashMap<Integer, UEFARatingSchemePart>> ratingScheme = new HashMap<>();
    private BaseTournament tournament1stLevel = null;
    private BaseTournament tournament2ndLevel = null;
    private BaseTournament tournament3rdLevel = null;

    private class PairPoints {
        public double homePoint = 0;
        public double awayPoint = 0;
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

                    //НАДО НАПИСАТЬ ПОЛУЧЕНИЕ 1-го и 2-го матча из WinTwoMeet

                    //pH += schemePart.addPoint + (meet.isWinnerHomeTeamWOPen() ? (isHalfPoint ? 1.0 : 2.0) : 0.0);

                    //meet.isWinnerHomeTeamWOPen()
                }
            }



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
