package fss.model;

import java.util.ArrayList;

public class PlayOffStagePool extends BasePlayOffStagePool {
    private PlayOffStage stage = null;

    public PlayOffStagePool(String name, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        super(name, teams, rating, cntRounds);
    }

    @Override
    public void calc() {
        stage = new PlayOffStage(getName(), getCntRounds() != 1);
        stage.addTeams(getTeams());
        stage.addMeets(Sortition.playOffSort(getTeams(), getRating(), getCntRounds() != 1));
        stage.calc();
    }

    @Override
    public ArrayList<SimpleTeam> getWinners() {
        return stage.getWinners();
    }

    @Override
    public ArrayList<SimpleTeam> getLosers() {
        return stage.getLosers();
    }

    @Override
    public ArrayList<Gameable> getMeetings() {
        return stage.getMeetings();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Play off stage: " + getName());
        sb.append(System.lineSeparator());
        sb.append(stage.toString());
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}
