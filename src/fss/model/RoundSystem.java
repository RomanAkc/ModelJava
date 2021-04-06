package fss.model;
import java.util.ArrayList;
import java.util.Collections;

public class RoundSystem {
    public static class Day {
        private boolean alreadyCalculated = false;
        private ArrayList<Meet> meetings = new ArrayList<Meet>();
        private int numDay = 0;
        public Day(int numDay) {
            this.numDay = numDay;
        }
        public void addMeet(Meet meet) {
            meetings.add(meet);
        }

        public void calc() {
            calculate(false);
        }
        public void calcUseOwner() {
            calculate(true);
        }

        public void updateAlreadyCalculated() {
            boolean already = true;
            for(var m : meetings) {
                if(!m.isAlreadyCalculated()) {
                    already = false;
                    break;
                }
            }
            alreadyCalculated = already;
        }

        private void calculate(boolean useOwner) {
            if(alreadyCalculated) {
                return;
            }

            for(var meet : meetings) {
                if(useOwner) {
                    meet.calcUseOwner();
                } else {
                    meet.calc();
                }
            }
            alreadyCalculated = true;
        }

        @Override
        public String toString() {
            var result = new StringBuffer();
            result.append(Integer.toString(numDay) + " day");
            result.append(System.lineSeparator());

            for(var meet : meetings) {
                result.append(meet.toString());
                result.append(System.lineSeparator());
             }

            return result.toString();
        }

        public ArrayList<Meet> getMeetings() {
            return meetings;
        }
    }

    private static class TeamWithHome {
        public SimpleTeam team = null;
        public boolean isHome = true;
        public TeamWithHome(SimpleTeam team, boolean isHome) {
            this.team = team;
            this.isHome = isHome;
        }

        @Override
        public String toString() {
            return team.toString();
        }
    }

    private static class TeamPair {
        public SimpleTeam home = null;
        public SimpleTeam away = null;
        public TeamPair(SimpleTeam home, SimpleTeam away) {
            this.home = home;
            this.away = away;
        }
    }

    public static ArrayList<Day> fillDays2Round(ArrayList<SimpleTeam> teams) {
        var days = fillDays(teams);
        int size = days.size();

        for(int i = 0; i < size; ++i) {
            var day = new Day(size + 1 + i);
            for(var m : days.get(i).meetings) {
                day.addMeet(new Meet(m.getTeamAway(), m.getTeamHome()));
            }
            days.add(day);
        }

        return days;
    }

    public static ArrayList<Day> fillDays(ArrayList<SimpleTeam> teams) {
        var days = new ArrayList<Day>();

        if(teams.size() < 2) {
            return days;
        }

        var teamFirst = new TeamWithHome(teams.get(0), true);
        var dayOrder = getInitialTeamWithHome(teams);

        for(int i = 0; i < teams.size() - 1; ++i) {
            var day = new Day(i + 1);
            //Круговая система, вращаем все команды кроме первой, чтобы обеспечить встречу каждой с каждой
            if(i != 0) {
                Collections.rotate(dayOrder, 1);
            }

            addMeetToDay(teamFirst, dayOrder.get(0), day);
            for(int j = 0; j < (teams.size() / 2) - 1; ++j) {
                var idx1 = j + 1;
                var idx2 = dayOrder.size() - 1 - j;
                addMeetToDay(dayOrder.get(idx1), dayOrder.get(idx2), day);
            }

            days.add(day);
        }

        return  days;
    }

    private static ArrayList<TeamWithHome> getInitialTeamWithHome(ArrayList<SimpleTeam> teams) {
        var prevDayOrder = new ArrayList<TeamWithHome>();
        for(int i = 1; i < teams.size(); ++i) {
            prevDayOrder.add(new TeamWithHome(teams.get(i), true));
        }
        return prevDayOrder;
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
