package fss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Table {
    public enum WinRules {
        BY_DIFFERENCE_GOAL,
        BY_MEET,
        BY_GOAL_FOR,
        BY_RATING
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

    public Table() {
        rules = new ArrayList<>();
        rules.add(WinRules.BY_MEET);
    }

    public Table(ArrayList<RoundSystem.Day> days) {
        this.days = days;
    }
    public void addWinRule(WinRules rule) {
        rules.add(rule);
    }
    public void clearRules() {
        rules.clear();
    }

    private void addMeetToRows(Meet meet, Row rowHome, Row rowAway) {
        var res = meet.getResult();

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
                var key = new PairTeam(meet.getTeamHome(), meet.getTeamAway());
                var val = meetByPairTeam.get(key);
                if(val == null) {
                    val = new ArrayList<>();
                    meetByPairTeam.put(key, val);
                }
                val.add(meet);

                Row rowHome = null;
                Row rowAway = null;

                if(tables.isEmpty()) {
                    rowHome = new Row(meet.getTeamHome());
                    rowAway = new Row(meet.getTeamAway());
                } else {
                    rowHome = new Row(td.rowsByTeam.get(meet.getTeamHome()));
                    rowAway = new Row(td.rowsByTeam.get(meet.getTeamAway()));
                }

                addMeetToRows(meet, rowHome, rowAway);

                td.rowsByTeam.put(meet.getTeamHome(), rowHome);
                td.rowsByTeam.put(meet.getTeamAway(), rowAway);
                td.rows.add(rowHome);
                td.rows.add(rowAway);
            }

            Collections.sort(td.rows, new Comparator<Row>() {
                public int compare(Row r1, Row r2) {
                    return r1.point - r2.point;
                }});


            tables.add(td);
        }

        alreadyCalculated = true;
    }

    public String getString() {
        var result = new StringBuffer();

        for(var t : tables) {
            result.append(t.day.getString());
            //to do: печать строки
            result.append(System.lineSeparator());
        }

        return result.toString();
    }
}
