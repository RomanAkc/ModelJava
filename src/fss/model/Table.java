package fss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Table {
    public enum WinRules {
        BY_DIFFERENCE_GOAL,
        BY_MEET,
        BY_GOAL_FOR,
        BY_COUNT_WIN,
        BY_GOAL_AWAY_MEET,
    }

    private class MeetData {
        public boolean win = false;
        public boolean draw = false;
        public int goalFor = 0;
        public int goalAgainst = 0;
    }

    private class PairTeam {
        public SimpleTeam teamHome = null;
        public SimpleTeam teamAway = null;
        public PairTeam(SimpleTeam teamHome, SimpleTeam teamAway) {
            this.teamHome = teamHome;
            this.teamAway = teamAway;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final PairTeam other = (PairTeam) obj;

            return other.teamHome == teamHome && other.teamAway == teamAway;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + (this.teamHome != null ? this.teamHome.hashCode() : 0);
            hash = 53 * hash + (this.teamAway != null ? this.teamAway.hashCode() : 0);
            return hash;
        }
    }

    private class Row {
        public SimpleTeam team = null;
        public int meet = 0;
        public int win = 0;
        public int draw = 0;
        public int lose = 0;
        public int point = 0;
        public int goalFor = 0;
        public int goalAgainst = 0;
        public int goalDif = 0;

        public Row(SimpleTeam team) {
            this.team = team;
        }

        public Row(Row row) {
            team = row.team;
            meet = row.meet;
            win = row.win;
            draw = row.draw;
            lose = row.lose;
            point = row.point;
            goalFor = row.goalFor;
            goalAgainst = row.goalAgainst;
            goalDif = row.goalDif;
        }
    }

    private class TableDay {
        public RoundSystem.Day day = null;
        public HashMap<SimpleTeam, Row> rowsByTeam = new HashMap<>();
        public ArrayList<Row> rows = new ArrayList<>();
    }

    private boolean alreadyCalculated = false;
    private ArrayList<WinRules> rules = new ArrayList<>();
    private ArrayList<RoundSystem.Day> days = null;
    private ArrayList<TableDay> tables = null;

    private Table() {
        days = new ArrayList<>();
        rules = new ArrayList<>();
        rules.add(WinRules.BY_DIFFERENCE_GOAL);
        rules.add(WinRules.BY_GOAL_AWAY_MEET);
    }

    public Table(ArrayList<RoundSystem.Day> days, ArrayList<WinRules> rules) {
        this.days = days;
        this.rules = rules;
    }
    public void addWinRule(WinRules rule) {
        rules.add(rule);
    }
    public void clearRules() {
        rules.clear();
    }

    private void addMeetToRows(Meet meet, Row rowHome, Row rowAway) {
        var res = meet.getResultMeet();

        rowHome.meet++;
        rowHome.win += (res.isWin() ? 1 : 0);
        rowHome.draw += (res.isDraw() ? 1 : 0);
        rowHome.lose += (res.isLose() ? 1 : 0);
        rowHome.point += (res.isWin() ? 3 : (res.isDraw() ? 1 : 0));
        rowHome.goalFor += res.getGoalHome();
        rowHome.goalAgainst += res.getGoalAway();
        rowHome.goalDif = rowHome.goalFor - rowHome.goalAgainst;

        rowAway.meet++;
        rowAway.win += (res.isLose() ? 1 : 0);
        rowAway.draw += (res.isDraw() ? 1 : 0);
        rowAway.lose += (res.isWin() ? 1 : 0);
        rowAway.point += (res.isLose() ? 3 : (res.isDraw() ? 1 : 0));
        rowAway.goalFor += res.getGoalAway();
        rowAway.goalAgainst += res.getGoalHome();
        rowAway.goalDif = rowAway.goalFor - rowAway.goalAgainst;
    }

    private void addMeetToPair(Meet meet, HashMap<PairTeam, ArrayList<Meet>> meetByPairTeam) {
        var key = new PairTeam(meet.getTeamHome(), meet.getTeamAway());
        var val = meetByPairTeam.get(key);
        if(val == null) {
            val = new ArrayList<>();
            meetByPairTeam.put(key, val);
        }
        val.add(meet);
    }

    private Row getRowByTeam(SimpleTeam team) {
        if(tables.isEmpty()) {
            return new Row(team);
        }
        return new Row(tables.get(tables.size() - 1).rowsByTeam.get(team));
    }

    private void addRowToTableDay(Row row, TableDay td) {
        td.rowsByTeam.put(row.team, row);
        td.rows.add(row);
    }

    private int addMeetAsDayToTempTable(ArrayList<Meet> meets, Table tempTable, int dayFrom) {
        if(meets != null) {
            for(var m : meets) {
                tempTable.days.add(new RoundSystem.Day(dayFrom + 1));
                tempTable.days.get(dayFrom).addMeet(m);
                tempTable.days.get(dayFrom).updateAlreadyCalculated();
                dayFrom++;
            }
        }
        return dayFrom;
    }

    private int getWinnerByMeets(ArrayList<Meet> meets12, ArrayList<Meet> meets21, SimpleTeam team) {
        if(meets12 != null || meets21 != null) {
            var tempTable = new Table();
            int dayFrom = addMeetAsDayToTempTable(meets12, tempTable, 0);
            addMeetAsDayToTempTable(meets21, tempTable, dayFrom);

            tempTable.calc();
            if(tempTable.tables.isEmpty()) {
                return  0;
            }

            return tempTable.tables.get(tempTable.tables.size() - 1).rows.get(0).team == team ? 1 : -1;
        }

        return 0;
    }

    private int getWinnerByAwayMeetGoals(ArrayList<Meet> meets12, ArrayList<Meet> meets21, SimpleTeam team) {
        if (meets12 != null || meets21 != null) {
            class NestedPair {
                SimpleTeam team1 = null;
                int goalTeam1 = 0;
                SimpleTeam team2 = null;
                int goalTeam2 = 0;
            };

            var nestedPair = new NestedPair();

            if(meets12 != null && !meets12.isEmpty()) {
                nestedPair.team1 = meets12.get(0).getTeamHome();
                nestedPair.team2 = meets12.get(0).getTeamAway();
                for (var m : meets12) {
                    nestedPair.goalTeam2 += m.getResultMeet().getGoalAway();
                }
            }

            if(meets21 != null && !meets21.isEmpty()) {
                if(nestedPair.team1 == null || nestedPair.team2 == null) {
                    nestedPair.team1 = meets21.get(0).getTeamAway();
                    nestedPair.team2 = meets21.get(0).getTeamHome();
                }
                for (var m : meets21) {
                    nestedPair.goalTeam1 += m.getResultMeet().getGoalAway();
                }
            }

            if(nestedPair.goalTeam1 != nestedPair.goalTeam2) {
                if(nestedPair.goalTeam1 > nestedPair.goalTeam2) {
                    return nestedPair.team1 == team ? 1 : -1;
                } else {
                    return nestedPair.team2 == team ? 1 : -1;
                }
            }
        }

        return 0;
    }

    private void sortTableDay(TableDay td, HashMap<PairTeam, ArrayList<Meet>> meetByPairTeam) {
        Collections.sort(td.rows, (r1, r2) -> {
            if(r1.point != r2.point) {
                return r2.point - r1.point;
            } else {
                for(var r : rules) {
                    switch (r) {
                        case BY_COUNT_WIN: {
                            if(r2.win != r1.win) {
                                return  r2.win - r1.win;
                            }
                            break;
                        }
                        case BY_DIFFERENCE_GOAL: {
                            if(r2.goalDif != r1.goalDif){
                                return r2.goalDif - r1.goalDif;
                            }
                            break;
                        }
                        case BY_GOAL_FOR: {
                            if(r2.goalFor != r1.goalFor){
                                return r2.goalFor - r1.goalFor;
                            }
                            break;
                        }
                        case BY_MEET: {
                            var meets12 = meetByPairTeam.get(new PairTeam(r1.team, r2.team));
                            var meets21 = meetByPairTeam.get(new PairTeam(r2.team, r1.team));
                            var res = getWinnerByMeets(meets12, meets21, r2.team);
                            if(res != 0)
                                return res;
                            break;
                        }
                        case BY_GOAL_AWAY_MEET: {
                            var meets12 = meetByPairTeam.get(new PairTeam(r1.team, r2.team));
                            var meets21 = meetByPairTeam.get(new PairTeam(r2.team, r1.team));
                            var res = getWinnerByAwayMeetGoals(meets12, meets21, r2.team);
                            if(res != 0)
                                return res;
                            break;
                        }
                    }
                }

                return 0;
            }
        });
    }

    public void calc() {
        if(alreadyCalculated) {
            return;
        }

        tables = new ArrayList<>();
        var meetByPairTeam = new HashMap<PairTeam, ArrayList<Meet>>();

        for(var day : days) {
            var td = new TableDay();
            td.day = day;

            for(var meet : day.getMeetings()) {
                addMeetToPair(meet, meetByPairTeam);

                var rowHome = getRowByTeam(meet.getTeamHome());
                var rowAway = getRowByTeam(meet.getTeamAway());;

                addMeetToRows(meet, rowHome, rowAway);

                addRowToTableDay(rowHome, td);
                addRowToTableDay(rowAway, td);
            }

            sortTableDay(td, meetByPairTeam);
            tables.add(td);
        }

        alreadyCalculated = true;
    }

    public SimpleTeam getNTeam(int n) {
        if(!alreadyCalculated)
            return null; //TODO: бросить exception

        var table = tables.get(tables.size() - 1);
        return table.rows.get(n).team;
    }

    private ArrayList<SimpleTeam> getNTeams(int first, int last) {
        if(!alreadyCalculated)
            return null; //TODO: бросить exception

        var out = new ArrayList<SimpleTeam>();
        for(int i = first; i <= last; ++i) {
            out.add(getNTeam(i));
        }

        return out;
    }

    public ArrayList<SimpleTeam> getNFirst(int n) {
        if(!alreadyCalculated)
            return null; //TODO: бросить exception

        return getNTeams(0, n - 1);
    }

    public ArrayList<SimpleTeam> getNLast(int n) {
        if(!alreadyCalculated)
            return null; //TODO: бросить exception

        var table = tables.get(tables.size() - 1);
        return getNTeams(table.rows.size() - n, table.rows.size());
    }

    @Override
    public String toString() {
        var result = new StringBuffer();

        for(var t : tables) {
            result.append(t.day.toString());
            result.append(System.lineSeparator());
            result.append(String.format("%10$-4s%1$-20s%2$3s%3$3s%4$3s%5$3s%6$4s%7$4s-%8$-4s%9$4s",
                    "Team", "G", "W", "D", "L", "P", "GF", "GA", "GD", "P."));
            result.append(System.lineSeparator());

            for(int i = 0; i < t.rows.size(); ++i) {
                var row = t.rows.get(i);
                result.append(String.format("%10$-4d%1$-20s%2$3d%3$3d%4$3d%5$3d%6$4d%7$4d-%8$-4d%9$4d"
                        , row.team.getName(), row.meet, row.win, row.draw, row.lose
                        , row.point, row.goalFor, row.goalAgainst, row.goalDif, i + 1));
                result.append(System.lineSeparator());
            }

            result.append(System.lineSeparator());
        }

        return result.toString();
    }
}
