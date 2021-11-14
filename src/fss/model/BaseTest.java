package fss.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//Base test class
public class BaseTest {
    public class WinTwoMeetTest implements WinTwoGameable {
        private SimpleTeam firstTeam = null;
        private SimpleTeam secondTeam = null;
        private Result firstMeet = null;
        private Result secondMeet = null;
        private Result addTime = null;
        private Result pen = null;

        public WinTwoMeetTest(SimpleTeam firstTeam, SimpleTeam secondTeam) {
            this.firstTeam = firstTeam;
            this.secondTeam = secondTeam;
        }

        public void SetFirstMeetResult(int goalHome, int goalAway) {
            firstMeet = new Result(goalHome, goalAway);
        }

        public void SetSecondMeetResult(int goalHome, int goalAway) {
            secondMeet = new Result(goalHome, goalAway);
        }

        public void SetAddTimeMeetResult(int goalHome, int goalAway) {
            addTime = new Result(goalHome, goalAway);
        }

        public void SetPenMeetResult(int goalHome, int goalAway) {
            pen = new Result(goalHome, goalAway);
        }

        @Override
        public boolean isWinnerHomeTeamFirstMeet() {
            return firstMeet.isWin();
        }

        @Override
        public boolean isDrawFirstMeet() {
            return firstMeet.isDraw();
        }

        @Override
        public boolean isWinnerHomeTeamSecondMeet() {
            return !secondMeet.isWin() && !secondMeet.isDraw();
        }

        @Override
        public boolean isDrawSecondMeet() {
            return secondMeet.isDraw();
        }

        private int getGoalsFirstTeamMainTime() {
            return firstMeet.getGoalHome() + secondMeet.getGoalAway();
        }

        private int getGoalsSecondTeamMainTime() {
            return firstMeet.getGoalAway() + secondMeet.getGoalHome();
        }

        private Result getResultMeetWOPen() {
            var goalsFor = getGoalsFirstTeamMainTime();
            var goalsAway = getGoalsSecondTeamMainTime();

            if(addTime != null) {
                goalsFor += addTime.getGoalAway();
                goalsAway += addTime.getGoalHome();
            }

            return new Result(goalsFor, goalsAway);
        }

        @Override
        public boolean isWinnerHomeTeamWOPen() {
            return getResultMeetWOPen().isWin();
        }

        @Override
        public boolean isDrawWOPen() {
            return getResultMeetWOPen().isDraw();
        }

        @Override
        public SimpleTeam getTeamHome() {
            return firstTeam;
        }

        @Override
        public SimpleTeam getTeamAway() {
            return secondTeam;
        }

        @Override
        public void calc() {
        }

        @Override
        public Result getResultMeet() {
            return null;
        }

        @Override
        public boolean isWinnerHomeTeam() {
            var goalsFirstTeam = getGoalsFirstTeamMainTime();
            var goalsSecondTeam = getGoalsSecondTeamMainTime();

            if(goalsFirstTeam != goalsSecondTeam) {
                return goalsFirstTeam > goalsSecondTeam;
            }

            if(!addTime.isDraw()) {
                return addTime.getGoalAway() > addTime.getGoalHome();
            }

            return pen.getGoalAway() > pen.getGoalHome();
        }

        @Override
        public boolean isDraw() {
            return false;
        }

        @Override
        public SimpleTeam getWinner() {
            return isWinnerHomeTeam() ? firstTeam : secondTeam;
        }

        @Override
        public SimpleTeam getLoser() {
            return isWinnerHomeTeam() ? secondTeam : firstTeam;
        }
    }

    protected ArrayList<SimpleTeam> generateTeams(int cnt) {
        var country = generateCounty();
        var teams = new ArrayList<SimpleTeam>();
        for(int i = 0; i < cnt; ++i) {
            teams.add(new ClubTeam(i, String.format("Team %d", i), country, 30, 30, 30));
        }
        return teams;
    }

    protected ArrayList<SimpleTeam> generateTeamsWithPower(int cnt, int power) {
        var country = generateCounty();
        var teams = new ArrayList<SimpleTeam>();
        for(int i = 0; i < cnt; ++i) {
            teams.add(new ClubTeam(i, String.format("Team %d", i), country, power, power, power));
        }
        return teams;
    }

    protected Country generateCounty() {
        return generateCountryWithID(1);
    }

    protected HashMap<ClubTeam, Country> generateClubTeams(int cntCountries, int cntClubForCountry) {
        var result = new HashMap<ClubTeam, Country>();

        int teamID = 1;
        for(int i = 0; i < cntCountries; ++i) {
            var country = generateCountryWithID(i);
            for(int j = 0; j < cntClubForCountry; ++j) {
                teamID += 1;
                var team = new ClubTeam(teamID, String.format("Team %d", teamID), country, 30);
                result.put(team, country);
            }
        }

        return result;
    }

    public Country generateCountryWithID(int id) {
        return new Country(id, "Country" + Integer.toString(id), WorldPart.EUROPE);
    }
}
