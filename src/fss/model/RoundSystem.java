package fss.model;
import java.util.ArrayList;
import java.util.Collections;

public class RoundSystem {
    public static class Meet {
        private boolean alreadyCalculated = false;
        private SimpleTeam teamHome;
        private SimpleTeam teamAway;
        private Result result;

        public Meet(SimpleTeam teamHome, SimpleTeam teamAway) {
            this.teamHome = teamHome;
            this.teamAway = teamAway;
        }

        public void calc() {
            if(alreadyCalculated) {
                return;
            }

            result = ResultCalculator.calc(teamHome.getPower(), teamAway.getPower());
            alreadyCalculated = true;
        }

        public String getString() {
            return teamHome.getName() + " - " + teamAway.getName() + " " + result.getString();
        }
    }

    public static class WinMeet extends Meet {
        private Result resultAdd;
        private Result resultPen;

        public WinMeet(SimpleTeam teamHome, SimpleTeam teamAway) {
            super(teamHome, teamAway);
        }
    }

    public static class WinTwoMeet {
        private Meet firstMeet;
        private WinMeet secondMeet;
    }

    public static class Day {
        private boolean alreadyCalculated = false;
        private ArrayList<Meet> meetings = new ArrayList<Meet>();
        public void addMeet(Meet meet) {
            meetings.add(meet);
        }
        public void calc() {
            if(alreadyCalculated) {
                return;
            }

            for(var meet : meetings) {
                meet.calc();
            }

            alreadyCalculated = true;
        }

        public String getString() {
            var result = new StringBuffer();
            for(var meet : meetings) {
                result.append(meet.getString());
                result.append(System.lineSeparator());
            }
            return result.toString();
        }
    }

    private static class TeamWithHome {
        public SimpleTeam team = null;
        public boolean isHome = true;
        public TeamWithHome(SimpleTeam team, boolean isHome) {
            this.team = team;
            this.isHome = isHome;
        }
    }

    private static class TeamPair {
        public SimpleTeam home;
        public SimpleTeam away;
        public TeamPair(SimpleTeam home, SimpleTeam away) {
            this.home = home;
            this.away = away;
        }
    }

    public static ArrayList<Day> fillDays(ArrayList<SimpleTeam> teams) {
        var days = new ArrayList<Day>();
        if(teams.size() < 2) {
            return days;
        }

        var index0 = new TeamWithHome(teams.get(0), true);
        var prevDayOrder = new ArrayList<TeamWithHome>();
        int size = teams.size();
        
        for(int i = 0; i < size - 1; ++i) {
            var day = new Day();

            if(i == 0) {
                for(int j = 1; j < size; ++j)
                prevDayOrder.add(new TeamWithHome(teams.get(j), true));
            } else {
                Collections.rotate(prevDayOrder, 1);
            }

            addMeetToDay(index0, prevDayOrder.get(0), day);
            for(int j = 0; j < (size / 2) - 1; ++j) {
                var index = (2 * j) + 1;
                addMeetToDay(prevDayOrder.get(index), prevDayOrder.get(index + 1), day);
            }

            days.add(day);
        }

        return  days;
    }

    private static void addMeetToDay(TeamWithHome first, TeamWithHome second, Day day) {
        var pair = getTeamPairWithHome(first, second);
        day.addMeet(new Meet(pair.home, pair.away));
    }

    private static TeamPair getTeamPairWithHome(TeamWithHome first, TeamWithHome second) {
        if(first.isHome == second.isHome || second.isHome) {
            first.isHome = true;
            second.isHome = false;
            return new TeamPair(first.team, second.team);
        }

        first.isHome = false;
        second.isHome = true;
        return new TeamPair(second.team, first.team);
    }


}
