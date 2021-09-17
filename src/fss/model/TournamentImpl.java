package fss.model;

import java.util.ArrayList;
import java.util.HashMap;

//Турнир - не просто набор стадий
//Это набор стадий и отношений между ними
//В простом варианте можно представить его как связный список
//Где в узлах либо одна стадия, либо несколько
//Например: (Групповой этап, 4 группы, выходят 1 и 2я команды группы)
//      ->(PO 1/4 финала, выходят победители)
//      ->(PO 1/2 финала, выходят победители)
//      ->(PO финал)
//Также возможен вариант дерева
//Где в узлах по-прежнему либо одна стадия, либо несколько
//Но где возможны множественные корни
//Например:
// (Отборочный этап, 5 групп, выходят 1я команда группы в 4й этап, лучшие 4 во 2-й этап)
//      ->(PO, отборочные, выходят победители в 4й этап)
// (Хозяин соревнований, выходит без игр в 4-й этап)
//      ->(Групповой этап, 2 группы по 4 команды, выходят 1 и 2я команды группы)
//      ->(PO 1/2 финала, выходят победители)
//      ->(PO финал)
//Также при определении победителя могут учитываться различные факторы (не только формальные показатели из
//результатов соревнований, но и рейтинги или случайность (если все факторы не позволяют определить победителя))
//Надо подумать о том, как правильно организовать построение турнира
//Мне кажется, турнир должен определяться заранее (его структура)
//Должна быть четкая карта стадий и отношений между ними (кто куда идет, кто откуда берет команды).
//Получается список, где каждый элемент имеет следующий данные:
//0. Порядок стадии в турнире
//1. Откуда берутся команды на стадии (добавляются отдельно, с предыдущей стадии (какой? - берется порядок стадии в турнире))
//2. Тип стадии (плей-офф или групповой)
//Сначала определяем структуру турнира
//Потом последовательно начинаем вычислять стадии
//  -задаем команды
//  -расчитываем
//Как задать команды? Возможно тут и понадобится StagePool, т. к. удобнее будет делать групповой этап в рамках одного пула, чем в рамках турнира
//Каждый пул имеет команды (например групповой этап ЧЕ - 16 команд)
//Наверное, для пула нужна проверка количества команд(?)
//А для трунира при такой схеме можно сделать проверку на ошибки
//Получив команды, пул должен уметь их разбить на группы
//Для этого надо написать отдельный класс, который будет заниматься жеребьевкой
//Класс жеребьевки будет реализовывать некий интерфейс (т. к. жеребьевка разная для разных типов команд)
//Сначала надо написать интерфейс (а может абстрактный класс) и простейший класс жеребьевки
//Класс жеребьевки будет принимать команды List<SimpleTeam> и тип, жеребьевки:
//  -плей-офф
//  -разбить на группы (количество групп?)
//Также, иногда при жеребьевке требуется отобрать лучшие или худшие команды по определенном критерию
//Например, из 9 групп занявшие 2 места играют в плей-офф
//Получается 4 матча, а одна лучшая(худшая) проходит(вылетает) без матча
//Т. е. надо написать класс для определения лучшей, худшей команды
//Или не надо? Наверное, все-таки, правильнее перенести логику определения лучшей/худшей команды на пул стадий

//При исполнении турнир идет по схеме последовательно,
//создает пул стадий для каждой части схемы
//и сразу вычисляет

class TournamentImpl extends Tournament {
    private Scheme scheme = null;
    private Rating rating = null;
    private ArrayList<StagePool> stages = new ArrayList<>();
    private HashMap<Integer, StagePool> stageByID = new HashMap<>();
    private HashMap<Integer, ArrayList<SimpleTeam>> teamsByStageID = new HashMap<>();
    private ArrayList<Table.WinRules> rules = null;

    public TournamentImpl(String name) {
       super(name);
    }

    @Override
    public void addScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    @Override
    public void addTeamsToStage(int nID, ArrayList<SimpleTeam> teams) {
        this.teamsByStageID.put(nID, teams);
    }

    @Override
    public void addRating(Rating rating) {
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
            //Создать набор команд для пула стадий
            var teams = getTeams(part);

            //Создать пул стадий
            StagePool stagePool = null;
            switch (part.stageType) {
                case CIRCLE: {
                    stagePool = new StagePoolImpl(part.stageType, part.name, teams, rating, part.cntRound);
                    stagePool.addWinRules(rules);
                    break;
                }
                case PLAYOFF: {
                    stagePool = new StagePoolImpl(part.stageType, part.name, teams, rating, part.cntRound);
                    break;
                }
                case GROUPS: {
                    stagePool = new StagePoolImpl(part.name, part.cntGroups, teams, rating, part.cntRound);
                    stagePool.addWinRules(rules);
                    break;
                }
            }

            stages.add(stagePool);
            stageByID.put(part.ID, stagePool);
            stagePool.calc();
        }
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
                switch (source.typeSourcePrev) {
                    case WINNWERS: {
                        teams.addAll(prevStage.getWinners());
                        break;
                    }
                    case LOSERS: {
                        teams.addAll(prevStage.getLosers());
                        break;
                    }
                    case N_FIRST: {
                        teams.addAll(prevStage.getFirstN(source.cntTeam));
                        break;
                    }
                    case N_LAST: {
                        teams.addAll(prevStage.getLastN(source.cntTeam));
                        break;
                    }
                    case N_TEAM: {
                        teams.addAll(prevStage.getN(source.teamN));
                        break;
                    }
                }
            }
        }

        return teams;
    }
}



/*
public class Tournament {
    public enum StageType {
        CIRCLE,
        GROUPS,
        PLAYOFF
    }

    public enum TeamSource {
        ALL_TEAMS,
        PREVIOUS_STAGE,
        ADD_TEAMS,
    }

    //Показывает кто идет в какую стадию дальше
    public enum WhoGoes {
        NUM_FIRST, //Первые N команд
        NUM_LAST, //Последние N команд
        WINNERS, //Победители
        LOOSES, //Проигравшие
        N_TEAM //N-я команда
    }

    private class WhoGoesNextStage {
        public WhoGoes whoGoes = null;
        public int cntGoes = 0;

        public WhoGoesNextStage(WhoGoes whoGoes, int cntGoes) {
            this.whoGoes = whoGoes;
            this.cntGoes = cntGoes;
        }
    }

    private class StageRules {
        public StageType stageType = null;
        public TeamSource teamSource = null;
        public WhoGoesNextStage whoGoesNextStage = null;

        public StageRules(StageType stageType, TeamSource teamSource, WhoGoesNextStage whoGoesNextStage) {
            this.stageType = stageType;
            this.teamSource = teamSource;
            this.whoGoesNextStage = whoGoesNextStage;
        }
    }

    private class StagesWithRules {
        public Stage stage;
        public StageRules stageRule;
        public int stageOrder;
        public int stageSourceTeamOrder;
        public int cntTeams;

        public StagesWithRules(Stage stage, StageRules stageRule, int stageOrder, int stageSourceTeamOrder, int cntTeams) {
            this.stage = stage;
            this.stageRule = stageRule;
            this.stageOrder = stageOrder;
            this.stageSourceTeamOrder = stageSourceTeamOrder;
            this.cntTeams = cntTeams;
        }
    }

    private String name = null;
    private ArrayList<SimpleTeam> allTeams = new ArrayList<>();
    private HashMap<Integer, ArrayList<SimpleTeam>> addTeams = new HashMap<>();
    private ArrayList<StagesWithRules> stages = new ArrayList<>();
    private SortProvider sortProvider = null;
    protected boolean alreadyCalculated = false;

    public Tournament(String name) {
        this.name = name;
    }

    public void addTeams(SimpleTeam team) {
        allTeams.add(team);
    }

    public void addTeams(int listNum, SimpleTeam team) {
        if(addTeams.containsKey(listNum)) {
            addTeams.get(listNum).add(team);
        } else {
            var teams = new ArrayList<SimpleTeam>();
            teams.add(team);
            addTeams.put(listNum, teams);
        }
    }

    public void addStageGroups(String name, int cntGroups, int cntRound
            , TeamSource teamSource, int prevSourceShift, WhoGoes whoGoes, int nGoes, int cntTeams) {
        addStageImpl(name, StageType.GROUPS, cntRound, teamSource, prevSourceShift, whoGoes, nGoes, cntGroups, cntTeams);
    }

    //TODO: добавить addStageGroups с более сложным распределением по группам (кол-во команд в группе и кол-во проходящих далее)

    public void addStage(String name, StageType type, int cntRound
            , TeamSource teamSource, int prevSourceShift, WhoGoes whoGoes, int nGoes, int cntTeams) {
        addStageImpl(name, type, cntRound, teamSource, prevSourceShift, whoGoes, nGoes, 0, cntTeams);
    }

    private void addStageImpl(String name, StageType type, int cntRound
            , TeamSource teamSource, int prevSourceShift, WhoGoes whoGoes, int cntGoes
            , int cntGroups, int cntTeams) {
        var stageRule = new StageRules(type, teamSource, new WhoGoesNextStage(whoGoes, cntGoes));
        int stageOrder = (stages.isEmpty() ? 0 : stages.get(stages.size() - 1).stageOrder + 1);
        int stageSourceTeamOrder = stageOrder - prevSourceShift;
        switch (type) {
            case CIRCLE: {
                var stage = new CircleStage(name, cntRound);
                stages.add(new StagesWithRules(stage, stageRule, stageOrder, stageSourceTeamOrder, cntTeams));
                break;
            }
            case PLAYOFF: {
                var stage = new PlayOffStage(name, cntRound != 1);
                stages.add(new StagesWithRules(stage, stageRule, stageOrder, stageSourceTeamOrder, cntTeams));
                break;
            }
            case GROUPS: {
                for(int i = 0; i < cntGroups; ++i) {
                    var nameStage = (name + " Group " + Integer.toString(i));
                    var stage = new CircleStage(nameStage, cntRound);
                    stages.add(new StagesWithRules(stage, stageRule, stageOrder, stageSourceTeamOrder, cntTeams));
                }
                break;
            }
            //TODO: дефолт с исключением?
        }
    }

    private boolean Check() {
        return true; //TODO: написать?
    }

    public int getGoesNextFromStage() {
        return 0;
    }

    public void calc() {
        if(alreadyCalculated) {
            return;
        }

        int curOrder = -1;
        var lastOrderStages = new ArrayList<StagesWithRules>();
        var stagesByOrder = new ArrayList<ArrayList<StagesWithRules>>();

        for(var stageWithRule : stages) {
            if(curOrder != stageWithRule.stageOrder) {
                if(!lastOrderStages.isEmpty()) {
                    stagesByOrder.add(lastOrderStages);
                    lastOrderStages = new ArrayList<>();
                }
            }
            lastOrderStages.add(stageWithRule);
        }

        if(!lastOrderStages.isEmpty()) {
            stagesByOrder.add(lastOrderStages);
        }

        for(int stageIndex = 0; stageIndex < stagesByOrder.size(); ++stageIndex) {
            var arrStages = stagesByOrder.get(stageIndex);
            if(arrStages.isEmpty()) {
                continue;
            }

            //считаем, что источник во всех стадиях с одним порядком одинаков
            var teamSource = arrStages.get(0).stageRule.teamSource;
            switch (teamSource) {
                case ALL_TEAMS: {
                    ArrayList<SimpleTeam> teams = new ArrayList<>();
                    for(var t : allTeams) {
                        teams.add(t);
                    }

                    if(sortProvider != null) {
                        //TODO:
                    } else {
                        Collections.shuffle(teams);
                    }

                    for(int i = 0; i < teams.size(); ++i) {
                        for(var curStage : arrStages) {
                            for(int j = 0; j < curStage.cntTeams; ++j) {
                                curStage.stage.addTeam(teams.get(i));
                            }
                        }
                    }
                }
                case PREVIOUS_STAGE: {
                    if(stageIndex < 1) {
                        break;
                    }

                    if(arrStages.get(0).stageSourceTeamOrder < 0 || arrStages.get(0).stageSourceTeamOrder >= stagesByOrder.size()) {
                        break;
                    }

                    ArrayList<SimpleTeam> teams = new ArrayList<>();
                    var arrPrevStages = stagesByOrder.get(arrStages.get(0).stageSourceTeamOrder);

                    for(var prevStage : arrPrevStages) {
                        switch (prevStage.stageRule.whoGoesNextStage.whoGoes) {
                            case NUM_FIRST: {
                                var stage = (CircleStage)prevStage.stage;
                                teams.addAll(stage.getNLast(prevStage.stageRule.whoGoesNextStage.cntGoes));
                                break;
                            }
                            case NUM_LAST: {
                                var stage = (CircleStage)prevStage.stage;
                                teams.addAll(stage.getNFirst(prevStage.stageRule.whoGoesNextStage.cntGoes));
                                break;
                            }
                            case N_TEAM: {
                                var stage = (CircleStage)prevStage.stage;
                                teams.add(stage.getNTeam(prevStage.stageRule.whoGoesNextStage.cntGoes));
                                break;
                            }
                            case LOOSES: {
                                var stage = (PlayOffStage)prevStage.stage;
                                teams.addAll(stage.getLooses());
                                break;
                            }
                            case WINNERS: {
                                var stage = (PlayOffStage)prevStage.stage;
                                teams.addAll(stage.getWinners());
                                break;
                            }

                        }
                    }
                }
            }


            ;


        }


        //var lastOrderStages = new ArrayList<StagesWithRules>();
        var curOrderStages = new ArrayList<StagesWithRules>();
        boolean isTeamsAdded = false;
        for(var stageWithRule : stages) {
            if(stageWithRule.stageOrder != curOrder) {
                isTeamsAdded = false;
                for(var curStageWithRule : curOrderStages) {
                    if(curStageWithRule.stageRule.teamSource == TeamSource.ALL_TEAMS) {
                        if(!isTeamsAdded) {
                            ArrayList<SimpleTeam> teams = new ArrayList<>();
                            for(var t : allTeams) {
                                teams.add(t);
                            }

                            if(sortProvider != null) {
                                //TODO:
                            } else {
                                Collections.shuffle(teams);
                            }

                            for(int i = 0; i < teams.size(); ++i) {
                                for(var curStage : curOrderStages) {
                                    for(int j = 0; j < curStage.cntTeams; ++j) {
                                        curStage.stage.addTeam(teams.get(i));
                                    }
                                }
                            }
                            isTeamsAdded = true;
                        }

                        curStageWithRule.stage.calc();
                        curOrderStages.add(stageWithRule); //Какая-то фигня - надо перепроверить
                    } else if(curStageWithRule.stageRule.teamSource == TeamSource.PREVIOUS_STAGE) {
                        if(!isTeamsAdded) {
                            ArrayList<SimpleTeam> teams = new ArrayList<>();
                            for(var prevStageWithRule : lastOrderStages) {
                                switch(prevStageWithRule.stageRule.whoGoesNextStage.whoGoes) {
                                    case LOOSES: {
                                        var stage = (PlayOffStage)prevStageWithRule.stage;
                                        teams.addAll(stage.getLooses());
                                        break;
                                    }
                                    case WINNERS: {
                                        var stage = (PlayOffStage)prevStageWithRule.stage;
                                        teams.addAll(stage.getWinners());
                                        break;
                                    }
                                    case NUM_FIRST: {
                                        var stage = (CircleStage)prevStageWithRule.stage;
                                        teams.addAll(stage.getNFirst(prevStageWithRule.stageRule.whoGoesNextStage.cntGoes));
                                        break;
                                    }
                                    case NUM_LAST: {
                                        var stage = (CircleStage)prevStageWithRule.stage;
                                        teams.addAll(stage.getNLast(prevStageWithRule.stageRule.whoGoesNextStage.cntGoes));
                                        break;
                                    }
                                    case N_TEAM: {
                                        var stage = (CircleStage)prevStageWithRule.stage;
                                        teams.add(stage.getNTeam(prevStageWithRule.stageRule.whoGoesNextStage.cntGoes));
                                        break;
                                    }
                                }
                            }

                            if(sortProvider != null) {
                                //TODO:
                            } else {
                                Collections.shuffle(teams);
                            }

                            for(int i = 0; i < teams.size(); ++i) {
                                for(var curStage : curOrderStages) {
                                    for(int j = 0; j < curStage.cntTeams; ++j) {
                                        curStage.stage.addTeam(teams.get(i));
                                    }
                                }
                            }
                            isTeamsAdded = true;
                        }

                        curStageWithRule.stage.calc();
                        curOrderStages.add(stageWithRule);
                    }
                }

                lastOrderStages = curOrderStages;
                curOrderStages = new ArrayList<>();
                curOrderStages.add(stageWithRule);
            }


        }
    }


}*/
