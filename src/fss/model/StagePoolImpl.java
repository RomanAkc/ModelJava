package fss.model;

import java.util.ArrayList;

public class StagePoolImpl extends StagePool {
    private ArrayList<Stage> stages = new ArrayList<>();
    private ArrayList<Table.WinRules> rules = null;

    public StagePoolImpl(StageType stageType, String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(stageType, name, teams, rating, cntRounds);
    }

    public StagePoolImpl(String name, int cntGroups, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(name, cntGroups, teams, rating, cntRounds);
    }

    @Override
    public void addWinRules(ArrayList<Table.WinRules> rules) {
        this.rules = rules;
    }

    @Override
    public void calc() {
        createStages();

        for(var s : stages) {
            s.calc();
        }
    }

    @Override
    public ArrayList<SimpleTeam> getWinners() {
        var result = new ArrayList<SimpleTeam>();
        for(var stage : stages) {
            switch (stageType) {
                case PLAYOFF: {
                    result.addAll(((PlayOffStage)stage).getWinners());
                    break;
                }
                case GROUPS:
                case CIRCLE: {
                    result.addAll(((CircleStage)stage).getNFirst(1));
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<SimpleTeam> getLosers() {
        var result = new ArrayList<SimpleTeam>();
        for(var stage : stages) {
            switch (stageType) {
                case PLAYOFF: {
                    result.addAll(((PlayOffStage)stage).getLosers());
                    break;
                }
                case GROUPS:
                case CIRCLE: {
                    result.addAll(((CircleStage)stage).getNLast(1));
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<SimpleTeam> getFirstN(int cnt) {
        var result = new ArrayList<SimpleTeam>();
        for(var stage : stages) {
            switch (stageType) {
                case GROUPS:
                case CIRCLE: {
                    result.addAll(((CircleStage)stage).getNFirst(cnt));
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<SimpleTeam> getLastN(int cnt) {
        var result = new ArrayList<SimpleTeam>();
        for(var stage : stages) {
            switch (stageType) {
                case GROUPS:
                case CIRCLE: {
                    result.addAll(((CircleStage)stage).getNLast(cnt));
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<SimpleTeam> getN(int n) {
        var result = new ArrayList<SimpleTeam>();
        for(var stage : stages) {
            if(stageType == StageType.CIRCLE || stageType == StageType.GROUPS) {
                var team = ((CircleStage)stage).getNTeam(n);
                if(team != null) {
                    result.add(team);
                }
            }
        }
        return result;
    }

    @Override
    public StageType getStageType() {
        return stageType;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for(var stage : stages) {
            sb.append(name);
            sb.append(System.lineSeparator());
            sb.append(stage.toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private void createStages() {
        switch (stageType) {
            case CIRCLE: {
                var stage = new CircleStage(name, cntRounds);
                stage.addWinRules(rules);
                stage.addTeams(teams);
                stages.add(stage);
                break;
            }
            case PLAYOFF: {
                var stage = new PlayOffStage(name, cntRounds != 1);
                stage.addTeams(teams);
                stage.addMeets(Sortition.playOffSort(teams, rating, cntRounds != 1));
                stages.add(stage);
                break;
            }
            case GROUPS: {
                var groups = Sortition.groupSort(teams, cntGroups, rating);
                for(int i = 0; i < groups.size(); ++i) {
                    var stage = new CircleStage(name + ". Group " + Integer.toString(i + 1), cntRounds);
                    stage.addWinRules(rules);
                    stage.addTeams(groups.get(i));
                    stages.add(stage);
                }
                break;
            }
        }
    }
}
