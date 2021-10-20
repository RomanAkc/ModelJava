package fss.model;

import java.util.ArrayList;
import java.util.HashMap;

import static fss.model.StageType.NOTHING;

class Tournament extends BaseTournament {
    private Scheme scheme = null;
    private Ratingable rating = null;
    private ArrayList<StagePool> stages = new ArrayList<>();
    private HashMap<Integer, StagePool> stageByID = new HashMap<>();
    private HashMap<StagePool, Integer> idByStage = new HashMap<>();
    private HashMap<Integer, ArrayList<SimpleTeam>> teamsByStageID = new HashMap<>();
    private ArrayList<Table.WinRules> rules = null;

    public Tournament(String name) {
       super(name);
    }

    @Override
    public void addScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    @Override
    public void addTeamsToStage(int stageID, ArrayList<SimpleTeam> teams) {
        this.teamsByStageID.put(stageID, teams);
    }

    @Override
    public void addRating(Ratingable rating) {
        this.rating = rating;
    }

    @Override
    public void addWinRules(ArrayList<Table.WinRules> rules) {
        this.rules = rules;
    }

    @Override
    public void calc() {
        if(!scheme.check()) {
            return;
        }

        for(var part : scheme) {
            //Create teams pool for stage pool
            var teams = getTeams(part);

            //Create stage pool
            StagePool stagePool = null;
            var ratingForUse = getRatingForUse(part.ratingType, teams);
            switch (part.stageType) {
                case CIRCLE: {
                    stagePool = new RoundRobinStagePool(part.name, teams, ratingForUse, part.cntRound);
                    ((CommonRoundRobinGroupsStagePool)stagePool).addWinRules(rules);
                    break;
                }
                case PLAYOFF: {
                    stagePool = new PlayOffStagePool(part.name, teams, ratingForUse, part.cntRound);
                    break;
                }
                case GROUPS: {
                    stagePool = new GroupsStagePool(part.name, part.cntGroups, teams, ratingForUse, part.cntRound);
                    ((CommonRoundRobinGroupsStagePool)stagePool).addWinRules(rules);
                    break;
                }
            }

            stages.add(stagePool);
            stageByID.put(part.ID, stagePool);
            idByStage.put(stagePool, part.ID);
            stagePool.calc();
        }
    }

    @Override
    public int getCntStagePool() {
        return stages.size();
    }

    @Override
    public int getStageID(int stagePoolIndex) {
        if(stagePoolIndex < 0 || stagePoolIndex >= stages.size())
            return 0;

        return idByStage.get(stages.get(stagePoolIndex));
    }

    @Override
    public StageType getStageType(int stagePoolIndex) {
        if(stagePoolIndex < 0 || stagePoolIndex >= stages.size())
            return StageType.NOTHING;

        return stages.get(stagePoolIndex).getStageType();
    }

    @Override
    public ArrayList<Table.Row> getFinalTableRows(int stageID) {
        if(!stageByID.containsKey(stageID)) {
            return null;
        }

        var stagePool = stageByID.get(stageID);
        if(stagePool.getStageType() != StageType.CIRCLE) {
            return null;
        }

        return ((RoundRobinStagePool)stagePool).getFinalTableRows();
    }

    private ArrayList<SimpleTeam> getStageTeams(int stageID, TypeSource typeSource, int cntTeamOrNTeam) {
        var stage = getStage(stageID);
        if(stage == null) {
            return new ArrayList<SimpleTeam>();
        }

        return fillTeamsBySourceFromStage(stage, typeSource, cntTeamOrNTeam);
    }

    @Override
    public ArrayList<SimpleTeam> getFirstStageTeams(int stageID, int cntTeam) {
        return getStageTeams(stageID, TypeSource.N_FIRST, cntTeam);
    }

    @Override
    public ArrayList<SimpleTeam> getLastStageTeams(int stageID, int cntTeam) {
        return getStageTeams(stageID, TypeSource.N_LAST, cntTeam);
    }

    @Override
    public ArrayList<SimpleTeam> getNTeamStageTeams(int stageID, int nTeam) {
        return getStageTeams(stageID, TypeSource.N_TEAM, nTeam);
    }

    @Override
    public ArrayList<SimpleTeam> getWinnersStageTeams(int stageID) {
        return getStageTeams(stageID, TypeSource.WINNERS, 0);
    }

    @Override
    public ArrayList<SimpleTeam> getLosersStageTeams(int stageID) {
        return getStageTeams(stageID, TypeSource.LOSERS, 0);
    }

    @Override
    public ArrayList<SimpleTeam> getAllTournamentTeams() {
        var allTeams = new ArrayList<SimpleTeam>();
        for(var teams : teamsByStageID.values())
            allTeams.addAll(teams);
        return allTeams;
    }

    @Override
    public ArrayList<Meet> getStageMeetings(int stageID) {
        var stage = getStage(stageID);
        if(stage == null) {
            return null;
        }
        return stage.getMeetings();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(String.format("Tournament %s", name));
        sb.append(System.lineSeparator());
        for(var stage : stages) {
            sb.append(stage.toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private Ratingable getRatingForUse(RatingType ratingType, ArrayList<SimpleTeam> teams) {
        if(ratingType == RatingType.NO) {
            return null;
        }

        if(ratingType == RatingType.STANDART) {
            return rating;
        }

        return new RatingByTeamOrder(teams);
    }

    private StagePool getStage(int stageID) {
        if(stageByID.containsKey(stageID)) {
            return stageByID.get(stageID);
        }
        return null;
    }

    private ArrayList<SimpleTeam> getTeams(SchemePart part) {
        var teams = new ArrayList<SimpleTeam>();

        for(var source : part.teamSources) {
            if(source.source == SchemePart.Source.FROM_OUT) {
                if(teamsByStageID.containsKey(part.ID)) {
                    teams.addAll(teamsByStageID.get(part.ID));
                }
            } else if(source.source == SchemePart.Source.PREV_STAGE) {
                var prevStage = getStage(source.sourcePrevID);
                int cntTeamOrNTeam = source.typeSource == TypeSource.N_TEAM ? source.teamN - 1 : source.cntTeam;
                teams.addAll(fillTeamsBySourceFromStage(prevStage, source.typeSource, cntTeamOrNTeam));
            }
        }

        return teams;
    }

    private ArrayList<SimpleTeam> fillTeamsBySourceFromStage(StagePool stage, TypeSource source, int cntTeamOrNTeam) {
        var teams = new ArrayList<SimpleTeam>();
        switch (source) {
            case WINNERS: {
                teams.addAll(stage.getWinners());
                break;
            }
            case LOSERS: {
                teams.addAll(stage.getLosers());
                break;
            }
            case N_FIRST: {
                if(isCanReceiveFirstLast(stage)) {
                    teams.addAll(((BaseRoundRobinStagePool)stage).getFirstN(cntTeamOrNTeam));
                }
                break;
            }
            case N_LAST: {
                if(isCanReceiveFirstLast(stage)) {
                    teams.addAll(((BaseRoundRobinStagePool)stage).getLastN(cntTeamOrNTeam));
                }
                break;
            }
            case N_TEAM: {
                if(isCanReceiveNTeam(stage)) {
                    teams.addAll(((CommonRoundRobinGroupsStagePool)stage).getN(cntTeamOrNTeam));
                }
                break;
            }
        }
        return teams;
    }

    private boolean isCanReceiveFirstLast(StagePool stage) {
        return stage instanceof BaseRoundRobinStagePool;
    }

    private boolean isCanReceiveNTeam(StagePool stage) {
        return stage instanceof CommonRoundRobinGroupsStagePool;
    }
}