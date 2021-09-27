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
    private ArrayList<BaseStagePool> stages = new ArrayList<>();
    private HashMap<Integer, BaseStagePool> stageByID = new HashMap<>();
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
            BaseStagePool stagePool = null;
            switch (part.stageType) {
                case CIRCLE: {
                    stagePool = new RoundRobinStagePool(part.name, teams, rating, part.cntRound);
                    ((AbstractRoundRobinStagePool)stagePool).addWinRules(rules);
                    break;
                }
                case PLAYOFF: {
                    stagePool = new PlayOffStagePool(part.name, teams, rating, part.cntRound);
                    break;
                }
                case GROUPS: {
                    stagePool = new GroupsStagePool(part.name, part.cntGroups, teams, rating, part.cntRound);
                    ((AbstractRoundRobinStagePool)stagePool).addWinRules(rules);
                    break;
                }
            }

            stages.add(stagePool);
            stageByID.put(part.ID, stagePool);
            stagePool.calc();
        }
    }

    @Override
    public int getCntStagePool() {
        return stages.size();
    }

    @Override
    public ArrayList<RoundSystem.Day> getTables(int stageID) {
        if(!stageByID.containsKey(stageID)) {
            return null;
        }

        var stagePool = stageByID.get(stageID);
        if(stagePool.getStageType() != BaseStagePool.StageType.CIRCLE) {
            return null;
        }

        return null;
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

    private BaseStagePool getStage(int stageID) {
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
                    case WINNERS: {
                        teams.addAll(prevStage.getWinners());
                        break;
                    }
                    case LOSERS: {
                        teams.addAll(prevStage.getLosers());
                        break;
                    }
                    case N_FIRST: {
                        if(isCanReceiveNTeam(prevStage)) {
                            teams.addAll(((BaseRoundRobinStagePool)prevStage).getFirstN(source.cntTeam));
                        }
                        break;
                    }
                    case N_LAST: {
                        if(isCanReceiveNTeam(prevStage)) {
                            teams.addAll(((BaseRoundRobinStagePool)prevStage).getLastN(source.cntTeam));
                        }
                        break;
                    }
                    case N_TEAM: {
                        if(isCanReceiveFirstLast(prevStage)) {
                            teams.addAll(((AbstractRoundRobinStagePool)prevStage).getN(source.teamN));
                        }
                        break;
                    }
                }
            }
        }

        return teams;
    }

    private boolean isCanReceiveFirstLast(BaseStagePool stage) {
        return stage instanceof AbstractRoundRobinStagePool;
    }

    private boolean isCanReceiveNTeam(BaseStagePool stage) {
        return stage instanceof BaseRoundRobinStagePool;
    }
}