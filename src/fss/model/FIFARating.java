package fss.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

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

    static final HashMap<FIFAMeetImportance, Double> meetCoeff = new HashMap<>() {{
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

    private final HashMap<NationalTeam, Double> ratingByNational;
    private final TreeSet<RatingData> ratingData = new TreeSet<>((lhs, rhs) -> {
        if(lhs == rhs) {
            return 0;
        }

        if(lhs.rating < rhs.rating) {
            return 1;
        } else if(lhs.rating > rhs.rating) {
            return -1;
        }

        if(lhs.team.getID() < rhs.team.getID()) {
            return 1;
        }

        return -1;
    });

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

    private static class RatingData implements Serializable {
        public final double rating;
        public final NationalTeam team;

        public RatingData(double rating, NationalTeam team) {
            this.rating = rating;
            this.team = team;
        }
    }

    public FIFARating(HashMap<NationalTeam, Double> ratingByNational) {
        this.ratingByNational = ratingByNational;

        for(var kv : ratingByNational.entrySet()) {
            ratingData.add(new RatingData(kv.getValue(), kv.getKey()));
        }
    }

    @Override
    public int getTeamPosition(SimpleTeam team) {
        if( !(team instanceof NationalTeam) || !ratingByNational.containsKey(team)) {
            return Integer.MAX_VALUE;
        }

        NationalTeam nationalTeam = (NationalTeam) team;
        double rating = ratingByNational.get(nationalTeam);
        SortedSet<RatingData> head = ratingData.headSet(new RatingData(rating, nationalTeam));
        return head.size() + 1;
    }

    public void addMeet(Gameable meet, FIFAMeetImportance importance) {
        if( !(meet.getTeamHome() instanceof NationalTeam) ) {
            throw new IllegalArgumentException("FIFARating.addMeet teamHome is not NationalTeam");
        }

        if( !(meet.getTeamAway() instanceof NationalTeam) ) {
            throw new IllegalArgumentException("FIFARating.addMeet teamAway is not NationalTeam");
        }

        NationalTeam teamHome = (NationalTeam) meet.getTeamHome();
        NationalTeam teamAway = (NationalTeam) meet.getTeamAway();

        if(!ratingByNational.containsKey(teamHome))
            ratingByNational.put(teamHome, 0.0);

        if(!ratingByNational.containsKey(teamAway))
            ratingByNational.put(teamAway, 0.0);

        if(meet instanceof WinTwoGameable) {
            updateRatingByMeet(meet, importance, true);
            updateRatingByMeet(meet, importance, false);
        } else {
            updateRatingByMeet(meet, importance);
        }
    }

    private void updateRatingByMeet(Gameable meet, FIFAMeetImportance importance) {
        updateRatingByMeet(meet, importance, true);
    }

    private void updateRatingByMeet(Gameable meet, FIFAMeetImportance importance, boolean firstMeet) {
        NationalTeam teamHome = (NationalTeam) meet.getTeamHome();
        NationalTeam teamAway = (NationalTeam) meet.getTeamAway();

        double P_beforeHome = ratingByNational.get(teamHome);
        double P_beforeAway = ratingByNational.get(teamAway);

        double I = meetCoeff.get(importance);

        DoublePair W_e = calcWe(P_beforeHome, P_beforeAway);
        DoublePair W = calcW(meet, firstMeet);

        double PHome = Math.round(P_beforeHome + I * (W.valHome - W_e.valHome));
        double PAway = Math.round(P_beforeAway + I * (W.valAway - W_e.valAway));

        updateRatingData(teamHome, PHome);
        updateRatingData(teamAway, PAway);
    }

    public HashMap<NationalTeam, Double> getRawData() {
        return ratingByNational;
    }

    private DoublePair calcWe(double ratingHome, double ratingAway) {
        return new DoublePair(calcWeOneTeam(ratingHome, ratingAway), calcWeOneTeam(ratingAway, ratingHome));
    }

    private double calcWeOneTeam(double ratingHome, double ratingAway) {
        return 1 / (Math.pow(10.0, -(ratingHome - ratingAway) / 600.0) + 1);
    }

    private DoublePair calcW(Gameable meet, boolean firstMeet) {
        if(meet instanceof WinTwoGameable) {
            return calcWForWinTwoMeet((WinTwoGameable) meet, firstMeet);
        } else if(meet instanceof WinGameable) {
            return calcWForWinMeet((WinGameable) meet);
        }

        return calcWForMeet(meet);
    }

    private DoublePair calcWForMeet(Gameable meet) {
        if(meet.isDraw()) {
            return new DoublePair(0.5, 0.5);
        } else if(meet.isWinnerHomeTeam()) {
            return new DoublePair(1.0, 0.0);
        }

        return new DoublePair(0.0, 1.0);
    }

    private DoublePair calcWForWinMeet(WinGameable meet) {
        if(meet.isWinnerHomeTeamWOPen()) {
            return new DoublePair(1.0, 0.0);
        } else if(meet.isWinnerHomeTeam()) {
            return new DoublePair(0.75, 0.5);
        } else if(meet.isDrawWOPen()) {
            return new DoublePair(0.5, 0.75);
        }

        return new DoublePair(0.0, 1.0);
    }

    private DoublePair calcWForWinTwoMeet(WinTwoGameable meet, boolean firstMeet) {
        if(firstMeet) {
            if(meet.isWinnerHomeTeamFirstMeet()) {
                return new DoublePair(1.0, 0.0);
            } else if(meet.isDrawFirstMeet()) {
                return new DoublePair(0.5, 0.5);
            } else {
                return new DoublePair(0.0, 1.0);
            }
        } else {
            if(meet.isWinnerHomeTeamSecondMeet()) {
                return new DoublePair(1.0, 0.0);
            } else if(meet.isDrawSecondMeet()) {
                return new DoublePair(0.5, 0.5);
            } else {
                return new DoublePair(0.0, 1.0);
            }
        }
    }

    private void updateRatingData(NationalTeam team, double rating) {
        if(ratingByNational.containsKey(team)) {
            double oldRating = ratingByNational.get(team);
            ratingData.removeIf(data -> data.rating == oldRating && data.team == team);
            ratingByNational.remove(team);
        }

        ratingData.add(new RatingData(rating, team));
        ratingByNational.put(team, rating);
    }
}
