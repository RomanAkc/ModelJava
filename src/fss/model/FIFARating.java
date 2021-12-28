package fss.model;

import java.util.HashMap;

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

    private static class DoublePair {
        public double valHome = 0.0;
        public double valAway = 0.0;

        public DoublePair() {
        }

        public DoublePair(double valHome, double valAway) {
            this.valHome = valHome;
            this.valAway = valAway;
        }
    }

    private HashMap<NationalTeam, Double> ratingByNational = new HashMap<>();

    static HashMap<FIFAMeetImportance, Double> meetCoeff = new HashMap<>() {{
       put(FIFAMeetImportance.FRIENDLY_OUT, 5.0);
       put(FIFAMeetImportance.FRIENDLY_IN, 10.0);
       put(FIFAMeetImportance.NATION_LEAGUE_GROUP, 15.0);
       put(FIFAMeetImportance.NATION_LEAGUE_PLAYOFF, 25.0);
       put(FIFAMeetImportance.QUALIFYING, 25.0);
       put(FIFAMeetImportance.CONFEDERATION_UNDER14, 35.0);
       put(FIFAMeetImportance.CONFEDERATION_BEGINFROM14, 40.0);
       put(FIFAMeetImportance.WORLD_UNDER14, 50.0);
       put(FIFAMeetImportance.WORLD_BEGINFROM14, 60.0);
    }};

    @Override
    public int getTeamPosition(SimpleTeam team) {
        return 0;
    }

    public void addMeet(Gameable meet, FIFAMeetImportance importance) {
        if( !(meet.getTeamHome() instanceof NationalTeam) ) {
            throw new IllegalArgumentException("FIFARating add meet teamHome is not NationalTeam");
        }

        if( !(meet.getTeamAway() instanceof NationalTeam) ) {
            throw new IllegalArgumentException("FIFARating add meet bad teamAway is not NationalTeam");
        }

        if(!ratingByNational.containsKey(meet.getTeamHome()))
            ratingByNational.put((NationalTeam) meet.getTeamHome(), 0.0);

        if(!ratingByNational.containsKey(meet.getTeamAway()))
            ratingByNational.put((NationalTeam) meet.getTeamAway(), 0.0);

        double P_beforeHome = ratingByNational.get(meet.getTeamHome());
        double P_beforeAway = ratingByNational.get(meet.getTeamAway());

        double I = meetCoeff.get(importance);

        DoublePair W_e = calcWe(P_beforeHome, P_beforeAway);
        DoublePair W = calcW(meet);

        double PHome = P_beforeHome + I * (W.valHome - W_e.valHome);
        double PAway = P_beforeAway + I * (W.valAway - W_e.valAway);

        ratingByNational.put((NationalTeam) meet.getTeamHome(), PHome);
        ratingByNational.put((NationalTeam) meet.getTeamAway(), PAway);
    }

    private DoublePair calcWe(double ratingHome, double ratingAway) {
        return new DoublePair(calcWeOneTeam(ratingHome, ratingAway), calcWeOneTeam(ratingAway, ratingHome));
    }

    private double calcWeOneTeam(double ratingHome, double ratingAway) {
        return 1 / (Math.pow(10.0, -(ratingHome - ratingAway) / 600.0) + 1);
    }

    private DoublePair calcW(Gameable meet) {
        if(meet instanceof Meet) {
            return calcWForMeet((Meet) meet);
        } else if(meet instanceof WinMeet) {
            return calcWForWinMeet((WinMeet) meet);
        } else if(meet instanceof WinTwoMeet) {
            return calcWForWinTwoMeet((WinTwoMeet) meet);
        }

        throw new IllegalArgumentException("FIFARating calcW meet's instance is not support");
    }

    private DoublePair calcWForMeet(Meet meet) {
        if(meet.isDraw()) {
            return new DoublePair(0.5, 0.5);
        } else if(meet.isWinnerHomeTeam()) {
            return new DoublePair(1.0, 0.0);
        }

        return new DoublePair(0.0, 1.0);
    }

    private DoublePair calcWForWinMeet(WinMeet meet) {
        if(meet.isWinnerHomeTeamWOPen()) {
            return new DoublePair(1.0, 0.0);
        } else if(meet.isWinnerHomeTeam()) {
            return new DoublePair(0.75, 0.5);
        } else if(meet.isDrawWOPen()) {
            return new DoublePair(0.5, 0.75);
        }

        return new DoublePair(0.0, 1.0);
    }

    private DoublePair calcWForWinTwoMeet(WinTwoMeet meet) {
        DoublePair result = new DoublePair();

        if(meet.isWinnerHomeTeamFirstMeet()) {
            result.valHome += 1.0;
        } else if(meet.isDrawFirstMeet()) {
            result.valHome += 0.5;
            result.valAway += 0.5;
        } else {
            result.valAway += 1.0;
        }

        if(meet.isWinnerHomeTeamSecondMeet()) {
            result.valAway += 1.0;
        } else if(meet.isDrawSecondMeet()) {
            result.valHome += 0.5;
            result.valAway += 0.5;
        } else {
            result.valHome += 1.0;
        }

        return result;
    }
}
