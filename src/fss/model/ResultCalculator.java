package fss.model;
import java.util.concurrent.Callable;

public class ResultCalculator {
    public static Result calcUseOwner(int powerHome, int powerAway) {
        return calculate(powerHome, powerAway, true);
    }

    public static Result calc(int powerHome, int powerAway) {
        return calculate(powerHome, powerAway, false);
    }

    public static Result calcAddTime(int powerHome, int powerAway) {
        return calculate(powerHome, powerAway, false, true);
    }

    public static Result calcPen() {
        int shotHome = 0;
        int shotAway = 0;
        int goalHome = 0;
        int goalAway = 0;
        while (true) {
            shotHome++;
            if (getPenResult()) {
                goalHome++;
            }

            if(needEndPen(shotAway, goalHome, goalAway)) {
                break;
            }

            shotAway++;
            if (getPenResult()) {
                goalAway++;
            }

            if(needEndPen(shotHome, goalAway, goalHome)) {
                break;
            }

            if(shotHome >= countPen && goalHome != goalAway) {
                break;
            }
        }
        
        return new Result(goalHome, goalAway);
    }

    private static boolean needEndPen(int shotSecond, int goalFirst, int goalSecond) {
        return shotSecond < countPen && goalFirst > goalSecond + (countPen - shotSecond);
    }

    private static boolean getPenResult() {
        return RandomWrapper.getRandom(percent100) < penaltyPercentRealization;
    }

    private static Result calculate(int powerHome, int powerAway, boolean useOwner) {
        return calculate(powerHome, powerAway, useOwner, false);
    }

    private static Result calculate(int powerHome, int powerAway, boolean useOwner, boolean addTime) {
        if(useOwner) {
            powerHome = (int) (powerHome * (1 + howeAddPower));
        }

        int powerDif = powerHome - powerAway;
        int cntMoments = getMinMoments(powerDif, addTime);
        int dangerousMomentHome = powerDif > 0 ? cntMoments + Math.abs(powerDif) : cntMoments;
        int dangerousMomentAway = powerDif > 0 ? cntMoments : cntMoments + Math.abs(powerDif);

        return new Result(getGoals(dangerousMomentHome), getGoals(dangerousMomentAway));
    }

    private static int getGoals(int cntMoments) {
        int goals = 0;
        for(int i = 0; i < cntMoments; ++i) {
            if(RandomWrapper.getRandom(percent100) < momentRealizationPercent) {
                goals++;
            }
        }
        return goals;
    }

    private static int getMinMoments(int powerDif, boolean addTime) {
        int addCntMomentsBorderThere = addCntMomentsBorder;
        int minCntMomentsThere = minCntMoments;
        if(addTime) {
            addCntMomentsBorderThere = (int)(addCntMomentsBorderThere * addTimeReduceModificator);
            minCntMomentsThere = (int)(minCntMomentsThere * addTimeReduceModificator);
        }
        int minCntMomentsMod = (addCntMomentsBorderThere - powerDif);
        if(minCntMomentsMod < 0 || minCntMomentsMod > addCntMomentsBorderThere) {
            minCntMomentsMod = 0;
        }
        return RandomWrapper.getRandom(minCntMomentsThere + minCntMomentsMod);
    }

    private static int countPen = 5; //Количество пенальти в серии пенальти
    private static int percent100 = 100; //100%
    private static int penaltyPercentRealization = 79; //Процент реализации пенальти
    private static double addTimeReduceModificator = 0.33; //Модификатор уменьшения времени в случае доп. времени
    // (при расчете опасных моментов становится меньше)
    private static int addCntMomentsBorder = 9; //Максимальное число опасных моментов, которое будет добавлено к minCntMoments
    // , на этой сумме будет вычислено минимальное число опасных моментов в матче
    private static int minCntMoments = 5; //Минимальное число опасных моментов в матче
    private static double momentRealization = 0.2; //Вероятность, что в опасном моменте забьют гол
    private static int momentRealizationPercent = (int)(momentRealization * 100);
    private static double howeAddPower = 0.05; //модификатор силы для владельца поля
    //private static int dangerousMoment


    private static double[] arrWin = {2500000.0, 8500000.0, 9800000.0, 9999989.0};
    private static double[] arrDraw = {5000000.0, 1000000.0, 100000.0, 10.0};
    //private static int[] arrLose = {2500000, 500000, 100000, 1};

    private static int getWinner(int powerHome, int powerAway) {
        double powerDif = (double)(powerHome - powerAway);

        boolean invertResult = powerDif < 0;
        if(invertResult)
            powerDif = (double)(powerAway - powerHome);

        double winChanceCutOff;
        double drawChanceCutOff;

        if(powerDif <= 10.0) {
            double stepWin = (arrWin[1] - arrWin[0]) / 10.0;
            winChanceCutOff = arrWin[0] + stepWin * powerDif;

            double stepDraw = (arrDraw[0] - arrDraw[1]) / 10.0;
            drawChanceCutOff = arrDraw[0] - stepDraw * powerDif;
        }
        else if(powerDif <= 20.0) {
            double stepWin = (arrWin[2] - arrWin[1]) / 10;
            winChanceCutOff = arrWin[1] + stepWin * (powerDif - 10.0);

            double stepDraw = (arrDraw[1] - arrDraw[2]) / 10.0;
            drawChanceCutOff = arrDraw[1] - stepDraw * (powerDif - 10.0);
        }
        else {
            double mult = (powerDif > 30.0) ? 10.0 : (powerDif - 20.0);

            double stepWin = (arrWin[3] - arrWin[2]) / 10.0;
            winChanceCutOff = arrWin[2] + stepWin * mult;

            double stepDraw = (arrDraw[2] - arrDraw[3]) / 10.0;
            drawChanceCutOff = arrDraw[2] - stepDraw * mult;
        }

        double random = (double)RandomWrapper.getRandom(9999999);
        
        int result = 0;
        if(random < winChanceCutOff) {
            if(invertResult)
                result = 2;
            else 
                result = 1;
        }
        else if(random >= drawChanceCutOff) {
            if(invertResult)
                result = 1;
            else 
                result = 2;
        }

        return  result;
    }
}
