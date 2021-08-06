package fss.model;

import java.util.*;

public class Sortition {
    static public ArrayList<SimpleTeam> playOffSort(ArrayList<SimpleTeam> teams, Rating rating) {
        if(teams.size() % 2 != 0)
            return null;

        if(rating == null) {
            var res = new ArrayList<SimpleTeam>(teams);
            Collections.shuffle(res);
            return  res;
        }

        var teamsByRating = getTeamsByRating(teams, rating);

        var baskets = divideIntoBaskets(teams.size() / 2, teamsByRating);
        var firstPart = baskets.get(0);
        var secondPart = baskets.get(1);

       /* var firstPart = new ArrayList<SimpleTeam>();
        var secondPart = new ArrayList<SimpleTeam>();

        int size = teamsByRating.size();
        int index = 0;
        for(var team : teamsByRating.values()) {
            if(index < size / 2) {
                firstPart.add(team);
            } else {
                secondPart.add(team);
            }
            index++;
        }*/

        Collections.shuffle(firstPart);
        Collections.shuffle(secondPart);

        //TODO: сравнить fistPart.size == second.part.Size, если не совпадают - выыбросить исключение

        var res = new ArrayList<SimpleTeam>();
        for(int i = 0; i < secondPart.size(); ++i) {
            res.add(secondPart.get(i));
            res.add(firstPart.get(i));
        }

        return res;
    }

    static public ArrayList<ArrayList<SimpleTeam>> groupSort(ArrayList<SimpleTeam> teams, int cntGroup, Rating rating) {
        int cntGroupTeams = teams.size() / cntGroup;
        if(cntGroupTeams < 2)
            return null;

        var res = new ArrayList<ArrayList<SimpleTeam>>();

        if(rating == null) {
            var tmp = new ArrayList<SimpleTeam>(teams);
            Collections.shuffle(tmp);

            int index  = 0;
            for(var team : tmp) {
                if(index < cntGroupTeams) {
                    var group = new ArrayList<SimpleTeam>();
                    group.add(team);
                    res.add(group);
                } else {
                    res.get(index % cntGroupTeams).add(team);
                }
                index++;
            }

            return res;
        }

        var teamsByRating = getTeamsByRating(teams, rating);

        ArrayList<ArrayList<SimpleTeam>> baskets = divideIntoBaskets(cntGroup, teamsByRating);
        int index;

        boolean firstBasket = true;
        for(var basket : baskets) {
            Collections.shuffle(basket);

            index = 0;
            for(var team : basket) {
                ArrayList<SimpleTeam> group = null;
                if(firstBasket) {
                    group = new ArrayList<>();
                    res.add(group);
                } else {
                    group = res.get(index);
                }

                group.add(team);
                index++;
            }

            firstBasket = false;
        }

        return res;
    }

    static private TreeMap<Integer, SimpleTeam> getTeamsByRating(ArrayList<SimpleTeam> teams, Rating rating) {
        var teamsByRating = new TreeMap<Integer, SimpleTeam>();
        for(var team : teams) {
            teamsByRating.put(rating.getTeamPosition(team), team);
        }
        return teamsByRating;
    }

    private static ArrayList<ArrayList<SimpleTeam>> divideIntoBaskets(int cntGroup, TreeMap<Integer, SimpleTeam> teamsByRating) {
        var baskets = new ArrayList<ArrayList<SimpleTeam>>();
        int index = 0;

        for(var team : teamsByRating.values()) {
            if(index >= cntGroup) {
                index = 0;
                var basket = new ArrayList<SimpleTeam>();
                baskets.add(basket);
            }

            var basket = baskets.get(baskets.size() - 1);
            basket.add(team);

            index++;
        }
        return baskets;
    }

}
