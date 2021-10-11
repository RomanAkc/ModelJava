package fss.model;

import java.util.*;

public class Sortition {
    public static ArrayList<Meet> playOffSort(ArrayList<SimpleTeam> teams, Ratingable rating, boolean twoMeets) {
        if(teams.size() % 2 != 0)
            return null;

        if(rating == null)
            return getWinMeetsWORating(teams, twoMeets);

        return getWinMeetsWithRating(teams, rating, twoMeets);
    }

    public static ArrayList<ArrayList<SimpleTeam>> groupSort(ArrayList<SimpleTeam> teams, int cntGroup, Ratingable rating) {
        int cntGroupTeams = teams.size() / cntGroup;
        if(cntGroupTeams < 2)
            return null;

        var baskets = divideIntoBaskets(cntGroup, getTeamsByRating(teams, rating));

        var res = new ArrayList<ArrayList<SimpleTeam>>();
        boolean firstBasket = true;
        for(var basket : baskets) {
            Collections.shuffle(basket);

            int index = 0;
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

    private static ArrayList<Meet> getWinMeetsWithRating(ArrayList<SimpleTeam> teams, Ratingable rating, boolean twoMeets) {
        var baskets = divideIntoBaskets(teams.size() / 2, getTeamsByRating(teams, rating));
        Collections.shuffle(baskets.get(0));
        Collections.shuffle(baskets.get(1));

        var res = new ArrayList<Meet>();
        for(int i = 0; i < baskets.get(1).size(); ++i) {
            if(twoMeets) {
                res.add(new WinTwoMeet(baskets.get(1).get(i), baskets.get(0).get(i)));
            } else {
                res.add(new WinMeet(baskets.get(1).get(i), baskets.get(0).get(i)));
            }
        }
        return res;
    }

    private static ArrayList<Meet> getWinMeetsWORating(ArrayList<SimpleTeam> teams, boolean twoMeets) {
        var teamsForShuffle = new ArrayList<>(teams);
        Collections.shuffle(teamsForShuffle);
        return getWinMeets(teamsForShuffle, twoMeets);
    }

    private static ArrayList<Meet> getWinMeets(ArrayList<SimpleTeam> teamsForShuffle, boolean twoMeets) {
        var meets = new ArrayList<Meet>();
        for(int i = 0; i < teamsForShuffle.size(); i = i + 2) {
            if(twoMeets) {
                meets.add(new WinTwoMeet(teamsForShuffle.get(i), teamsForShuffle.get(i + 1)));
            } else {
                meets.add(new WinMeet(teamsForShuffle.get(i), teamsForShuffle.get(i + 1)));
            }
        }
        return  meets;
    }

    static private TreeMap<Integer, SimpleTeam> getTeamsByRating(ArrayList<SimpleTeam> teams, Ratingable rating) {
        var teamsByRating = new TreeMap<Integer, SimpleTeam>();
        for(var team : teams) {
            teamsByRating.put(rating != null ? rating.getTeamPosition(team) : team.getID(), team);
        }
        return teamsByRating;
    }

    private static ArrayList<ArrayList<SimpleTeam>> divideIntoBaskets(int cntGroup, TreeMap<Integer, SimpleTeam> teamsByRating) {
        var baskets = new ArrayList<ArrayList<SimpleTeam>>();
        
        int index = 0;
        for(var team : teamsByRating.values()) {
            if(index >= cntGroup || index == 0) {
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
