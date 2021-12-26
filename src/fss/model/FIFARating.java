package fss.model;

import java.util.HashMap;
import java.util.Map;

public class FIFARating implements Ratingable {
    /*
    Правила расчета:
    P=P_before+I*(W-W_e), где

    P_before - рейтинг команды до матча

    I — коэффициент важности матча:
    5 — товарищеский матч за пределами «окон» календаря международных матчей
    10 — товарищеский матч в пределах «окон» календаря международных матчей
    15 — матчи групповых турниров Лиг наций
    25 — матчи плей-офф и финалов Лиг наций
    25 — отборочные игры чемпионатов конфедераций и чемпионата мира
    35 — матчи финальных турниров чемпионатов конфедераций до стадии 1/4 финала
    40 — матчи финальных турниров чемпионатов конфедераций начиная с 1/4 финала
    50 — матчи финального турнира чемпионата мира до стадии 1/4 финала
    60 — матчи финального турнира чемпионата мира начиная со стадии 1/4 финала

    W — результат матча:
    0 — поражение в основное или дополнительное время
    0,5 — ничья или поражение в серии послематчевых пенальти
    0,75 — победа в серии послематчевых пенальти
    1 — победа в основное или дополнительное время

    W_e — ожидаемый результат матча: W_e = 1 / (10 ^ (-dR / 600) + 1)
    dr — разница рейтингов соперников перед матчем; из рейтинга команды, для которой проводится подсчёт, вычитается рейтинг её соперника.

    Особые условия:

    Если пенальти назначены после матча, в котором есть победитель (то есть после второго матча двухматчевой серии), то они не учитываются при подсчёте рейтинга, матч рассматривается как обычный.
    Если в матчах стадий плей-офф финальных турниров результат команды окажется хуже ожидаемого (поражение или победа по пенальти над существенно более слабой командой), то её рейтинг не изменяется.
    */

    static HashMap<FIFAMeetImportance, Integer> meetCoeff = new HashMap<>() {{
       put(FIFAMeetImportance.FRIENDLY_OUT, 5);
       put(FIFAMeetImportance.FRIENDLY_IN, 10);
       put(FIFAMeetImportance.NATION_LEAGUE_GROUP, 15);
       put(FIFAMeetImportance.NATION_LEAGUE_PLAYOFF, 25);
       put(FIFAMeetImportance.QUALIFYING, 25);
       put(FIFAMeetImportance.CONFEDERATION_UNDER14, 35);
       put(FIFAMeetImportance.CONFEDERATION_BEGINFROM14, 40);
       put(FIFAMeetImportance.WORLD_UNDER14, 50);
       put(FIFAMeetImportance.WORLD_BEGINFROM14, 60);
    }};

    @Override
    public int getTeamPosition(SimpleTeam team) {
        return 0;
    }

    public void addMeet(Gameable meet, FIFAMeetImportance importance) {

        int I = meetCoeff.get(importance);
    }
}
