package fss.model;

import java.util.ArrayList;

class RoundRobinStagePool extends BaseRoundRobinStagePool {
    private CircleStage stage = null;

    public RoundRobinStagePool(String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(name, teams, rating, cntRounds);
    }

    @Override
    public void calc() {
        stage = new CircleStage(name, cntRounds);
        stage.addWinRules(rules);
        stage.addTeams(teams);
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
    public ArrayList<Meet> getMeetings() {
        return stage.getMeetings();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Round-robin stage: " + name);
        sb.append(System.lineSeparator());
        sb.append(stage.toString());
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}

class PlayOffStagePool extends BasePlayOffStagePool {
    private PlayOffStage stage = null;

    public PlayOffStagePool(String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(name, teams, rating, cntRounds);
    }

    @Override
    public void calc() {
        stage = new PlayOffStage(name, cntRounds != 1);
        stage.addTeams(teams);
        stage.addMeets(Sortition.playOffSort(teams, rating, cntRounds != 1));
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
    public ArrayList<Meet> getMeetings() {
        return stage.getMeetings();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Play off stage: " + name);
        sb.append(System.lineSeparator());
        sb.append(stage.toString());
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}

class GroupsStagePool extends BaseGroupsStagePool {
    private ArrayList<CircleStage> stages = null;

    public GroupsStagePool(String name, int cntGroups, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(name, cntGroups, teams, rating, cntRounds);
    }

    @Override
    public void calc() {
        createStages();
        for(var s : stages) {
            s.calc();
        }
    }

    private void createStages() {
        stages = new ArrayList<>();
        var groups = Sortition.groupSort(teams, cntGroups, rating);
        for(int i = 0; i < groups.size(); ++i) {
            var stage = new CircleStage(name + ". Group " + Integer.toString(i + 1), cntRounds);
            stage.addWinRules(rules);
            stage.addTeams(groups.get(i));
            stages.add(stage);
        }
    }

    @Override
    public ArrayList<SimpleTeam> getWinners() {
        var res = new ArrayList<SimpleTeam>();
        for(var stage : stages) {
            res.addAll(stage.getNFirst(1));
        }
        return res;
    }

    @Override
    public ArrayList<SimpleTeam> getLosers() {
        var res = new ArrayList<SimpleTeam>();
        for(var stage : stages) {
            res.addAll(stage.getNLast(1));
        }
        return res;
    }

    @Override
    public ArrayList<SimpleTeam> getN(int n) {
        var res = new ArrayList<SimpleTeam>();
        for(var stage : stages) {
            var team = stage.getNTeam(n);
            if(team != null) {
                res.add(team);
            }
        }
        return res;
    }

    @Override
    public ArrayList<Meet> getMeetings() {
        var result = new ArrayList<Meet>();
        for(var stage : stages)
            result.addAll(stage.getMeetings());
        return result;
    }

    @Override
    public ArrayList<ArrayList<SimpleTeam>> getGroupTeams() {
        var groups = new ArrayList<ArrayList<SimpleTeam>>();
        for(var stage : stages) {
            groups.add(new ArrayList<>(stage.getTeams()));
        }
        return groups;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Group stage: " + name);
        for(var stage : stages) {
            sb.append(System.lineSeparator());
            sb.append(stage.toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}