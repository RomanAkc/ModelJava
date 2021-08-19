package fss.model;

import java.util.ArrayList;

public class StagePoolImpl extends StagePool {
    private boolean alreadyCalculated = false;
    private ArrayList<Stage> stages = new ArrayList<>();

    public StagePoolImpl(StageType stageType, String name, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(stageType, name, teams, rating, cntRounds);
    }

    public StagePoolImpl(String name, int cntGroups, ArrayList<SimpleTeam> teams, Rating rating, int cntRounds) {
        super(name, cntGroups, teams, rating, cntRounds);
    }

    @Override
    public void calc() {
        if(alreadyCalculated) {
            return;
        }

        //1.Разбить на стадии
        createStages();

        //2.Вычислить
        for(var s : stages) {
            s.calc();
        }

        alreadyCalculated = true;
    }

    @Override
    public ArrayList<SimpleTeam> getWinners() {
        if(!alreadyCalculated) {
            return null;
        }

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
        if(!alreadyCalculated) {
            return null;
        }

        return null;
    }

    @Override
    public ArrayList<SimpleTeam> getFirstN(int cnt) {
        if(!alreadyCalculated) {
            return null;
        }

        return null;
    }

    @Override
    public ArrayList<SimpleTeam> getLastN(int cnt) {
        if(!alreadyCalculated) {
            return null;
        }

        return null;
    }

    @Override
    public SimpleTeam getN(int n) {
        if(!alreadyCalculated) {
            return null;
        }

        return null;
    }

    private void createStages() {
        switch (stageType) {
            case CIRCLE: {
                var stage = new CircleStage(name, cntRounds);
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
                    var stage = new CircleStage(name + ". Group " + Integer.toString(i), cntRounds);
                    stage.addTeams(groups.get(i));
                    stages.add(stage);
                }
                break;
            }
        }
    }
}
