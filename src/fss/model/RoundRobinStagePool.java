package fss.model;

import java.util.ArrayList;

public class RoundRobinStagePool extends BaseRoundRobinStagePool {
    private CircleStage stage = null;

    public RoundRobinStagePool(String name, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        super(name, teams, rating, cntRounds);
    }

    @Override
    public void calc() {
        stage = new CircleStage(getName(), getCntRounds());
        stage.addWinRules(rules);
        stage.addTeams(getTeams());
        stage.calc();
    }

    @Override
    public ArrayList<SimpleTeam> getWinners() {
        return new ArrayList<>(stage.getNFirst(1));
    }

    @Override
    public ArrayList<SimpleTeam> getLosers() {
        return new ArrayList<>(stage.getNLast(1));
    }

    @Override
    public ArrayList<SimpleTeam> getN(int n) {
        var result = new ArrayList<SimpleTeam>();
        var team = stage.getNTeam(n);
        if(team != null) {
            result.add(team);
        }
        return result;
    }

    @Override
    public ArrayList<SimpleTeam> getFirstN(int cnt) {
        return new ArrayList<>(stage.getNFirst(cnt));
    }

    @Override
    public ArrayList<SimpleTeam> getLastN(int cnt) {
        return new ArrayList<>(stage.getNLast(cnt));
    }

    @Override
    public ArrayList<Table.Row> getFinalTableRows() {
        return stage.getFinalTableRows();
    }

    @Override
    public ArrayList<Gameable> getMeetings() {
        return stage.getMeetings();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Round-robin stage: " + getName());
        sb.append(System.lineSeparator());
        sb.append(stage.toString());
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}

