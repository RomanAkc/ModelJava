package fss.model;
import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {

        var teams = new ArrayList<SimpleTeam>();
        teams.add(new NationalTeam(1, "Italy", SimpleTeam.WorldPart.EUROPE, 30, 0, 0));
        teams.add(new NationalTeam(2, "USA", SimpleTeam.WorldPart.NORD_AMERICA, 27, 0, 0));
        teams.add(new NationalTeam(3, "Russia", SimpleTeam.WorldPart.EUROPE, 26, 0, 0));
        teams.add(new NationalTeam(4, "South Korea", SimpleTeam.WorldPart.ASIA, 24, 0, 0));

        var days = RoundSystem.fillDays(teams);

        for(var day : days) {
            day.calc();
            System.out.println(day.getString());
        }

        /*for(int i = 0; i < 100; ++i) {
            int powerHome = 30;
            int powerAway = 30;

            var res = ResultCalculator.calc(powerHome,powerAway);
            if(res.isDraw()) {
                var resAdd = ResultCalculator.calcAddTime(powerHome, powerAway);

                if(resAdd.isDraw()) {
                    var resPen = ResultCalculator.calcPen();
                    System.out.println(res.getGoalHome() + " " + res.getGoalAway()
                            + " Add: " + resAdd.getGoalHome() + " " + resAdd.getGoalAway()
                            + " Pen: " + resPen.getGoalHome() + " " + resPen.getGoalAway());
                }
                else {
                    System.out.println(res.getGoalHome() + " " + res.getGoalAway() + " Add: " + resAdd.getGoalHome() + " " + resAdd.getGoalAway());
                }
            } else {
                System.out.println(res.getGoalHome() + " " + res.getGoalAway());
            }
        }*/
    }
}
