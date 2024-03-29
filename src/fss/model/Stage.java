package fss.model;

import java.util.*;

abstract class Stage {
    private String name = null;
    protected ArrayList<SimpleTeam> teams = null;

    public Stage(String name) {
        this.name = name;
    }

    public void addTeam(SimpleTeam team) {
        if(teams == null) {
            teams = new ArrayList<>();
        }
        teams.add(team);
    }
    public void addTeams(ArrayList<SimpleTeam> teams) {
        if(this.teams == null) {
            this.teams = new ArrayList<>(teams);
            return;
        }

        this.teams.addAll(teams);
    }

    public ArrayList<SimpleTeam> getTeams() {
        return new ArrayList<>(teams);
    }

    public abstract void calc();
    public abstract ArrayList<Gameable> getMeetings();

    @Override
    public String toString() {
        var sb = new StringBuffer();
        sb.append("Stage: ");
        sb.append(name);
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}

class CircleStage extends Stage {
    private Table table = null;
    private ArrayList<RoundSystem.Day> days = null;
    private int cntRounds = 0;
    private ArrayList<Table.WinRules> rules = null;

    public CircleStage(String name, int cntRounds) {
        super(name);
        this.cntRounds = cntRounds;
    }

    private ArrayList<RoundSystem.Day> calcDays() {
        var days = RoundSystem.fillDaysNRound(teams, cntRounds);
        var useOwner = cntRounds > 1;
        for(var day : days) {
            if(useOwner) {
                day.calcUseOwner();
            } else {
                day.calc();
            }
        }
        return days;
    }

    private void fillRulesByDefault() {
        rules = new ArrayList<>();
        rules.clear();
        rules.add(Table.WinRules.BY_MEET);
        rules.add(Table.WinRules.BY_COUNT_WIN);
        rules.add(Table.WinRules.BY_DIFFERENCE_GOAL);
        rules.add(Table.WinRules.BY_GOAL_FOR);
    }

    @Override
    public void calc() {
        var days = calcDays();
        if(rules == null) {
            fillRulesByDefault();
        }

        table = new Table(teams, days, rules);
        table.calc();
    }

    @Override
    public ArrayList<Gameable> getMeetings() {
        var result = new ArrayList<Gameable>();
        for(var day : days)
            result.addAll(day.getMeetings());
        return result;
    }

    public void addWinRules(ArrayList<Table.WinRules> rules) {
        this.rules = rules;
    }

    ArrayList<SimpleTeam> getNFirst(int n) {
        return table.getNFirst(n);
    }

    ArrayList<SimpleTeam> getNLast(int n) {
        return table.getNLast(n);
    }

    SimpleTeam getNTeam(int n) {
        return table.getNTeam(n);
    }

    ArrayList<Table.Row> getFinalTableRows() {
        return table.getLastRows();
    }

    @Override
    public String toString() {
        var sb = new StringBuffer();
        sb.append(super.toString());
        if(table != null) {
            sb.append(table.toString());
        } else {
            sb.append("Is not calculated");
        }
        return sb.toString();
    }
}

class PlayOffStage extends Stage {
    private boolean twoMeets = false;
    private boolean sortTeams = true;
    private ArrayList<WinGameable> meets = null;

    public PlayOffStage(String name, boolean twoMeets) {
        super(name);
        this.twoMeets = twoMeets;
    }

    public void addMeets(ArrayList<WinGameable> meets) {
        this.meets = new ArrayList<>();
        this.meets.addAll(meets);
    }
    public void setSortTeams(boolean sortTeams) {this.sortTeams = sortTeams;}
    public void sort(ArrayList<SimpleTeam> teams) {
        Collections.shuffle(teams);
    }

    public WinGameable createMeet(SimpleTeam teamHome, SimpleTeam teamAway) {
        if(twoMeets) {
            return new WinTwoMeet(teamHome, teamAway);
        }

        return new WinMeet(teamHome, teamAway);
    }

    private void fillMeets() {
        if(meets != null) {
            return;
        }

        var teamsForPair = new ArrayList<SimpleTeam>(teams);
        if(sortTeams) {
            sort(teamsForPair);
        }

        meets = new ArrayList<WinGameable>();
        for(int i = 0; i < teamsForPair.size(); i = i + 2) {
            meets.add(createMeet(teamsForPair.get(i), teamsForPair.get(i + 1)));
        }
    }

    private void calcMeets() {
        for(var m : meets) {
            m.calc();
        }
    }

    @Override
    public void calc() {
        fillMeets();
        calcMeets();
    }

    @Override
    public ArrayList<Gameable> getMeetings() {
        return new ArrayList<>(meets);
    }

    public ArrayList<SimpleTeam> getWinners() {
        var winners = new ArrayList<SimpleTeam>();
        for(var m : meets) {
            winners.add(m.getWinner());
        }
        return winners;

    }

    public ArrayList<SimpleTeam> getLosers() {
        var looses = new ArrayList<SimpleTeam>();
        for(var m : meets) {
            looses.add(m.getLoser());
        }
        return looses;
    }

    @Override
    public String toString() {
        var sb = new StringBuffer();
        sb.append(super.toString());
        for(var m : meets) {
            sb.append(m.toString());
            sb.append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}
