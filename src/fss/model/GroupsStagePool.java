package fss.model;

import java.util.ArrayList;
import java.util.Objects;

public class GroupsStagePool extends BaseGroupsStagePool {
    private ArrayList<CircleStage> stages = null;

    public GroupsStagePool(String name, int cntGroups, ArrayList<SimpleTeam> teams, Ratingable rating, int cntRounds) {
        super(name, cntGroups, teams, rating, cntRounds);
    }

    @Override
    public void calc() {
        createStages();
        for (var s : stages) {
            s.calc();
        }
    }

    private void createStages() {
        stages = new ArrayList<>();
        var groups = Sortition.groupSort(getTeams(), cntGroups, getRating());
        for (int i = 0; i < Objects.requireNonNull(groups, "GroupsStagePool.createStages").size(); ++i) {
            var stage = new CircleStage(getName() + ". Group " + (i + 1), getCntRounds());
            stage.addWinRules(rules);
            stage.addTeams(groups.get(i));
            stages.add(stage);
        }
    }

    @Override
    public ArrayList<SimpleTeam> getWinners() {
        var res = new ArrayList<SimpleTeam>();
        for (var stage : stages) {
            res.addAll(stage.getNFirst(1));
        }
        return res;
    }

    @Override
    public ArrayList<SimpleTeam> getLosers() {
        var res = new ArrayList<SimpleTeam>();
        for (var stage : stages) {
            res.addAll(stage.getNLast(1));
        }
        return res;
    }

    @Override
    public ArrayList<SimpleTeam> getN(int n) {
        var res = new ArrayList<SimpleTeam>();
        for (var stage : stages) {
            var team = stage.getNTeam(n);
            if (team != null) {
                res.add(team);
            }
        }
        return res;
    }

    @Override
    public ArrayList<Gameable> getMeetings() {
        var result = new ArrayList<Gameable>();
        for (var stage : stages)
            result.addAll(stage.getMeetings());
        return result;
    }

    @Override
    public ArrayList<ArrayList<SimpleTeam>> getGroupTeams() {
        var groups = new ArrayList<ArrayList<SimpleTeam>>();
        for (var stage : stages) {
            groups.add(new ArrayList<>(stage.getTeams()));
        }
        return groups;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Group stage: ").append(getName());
        for (var stage : stages) {
            sb.append(System.lineSeparator());
            sb.append(stage.toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
