package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SortitionTest {

    private ArrayList<SimpleTeam> generateTeams(int cnt) {
        var teams = new ArrayList<SimpleTeam>();
        for(int i = 0; i < cnt; ++i) {
            teams.add(new ClubTeam(i, String.format("%d", i), String.format("%d", i), SimpleTeam.WorldPart.EUROPE, 30, 30, 30));
        }
        return teams;
    }

    @Test
    public void playOffSortCntResultTeamWORating() {
       var teams = generateTeams(4);
       var sortedTeam = Sortition.playOffSort(teams, null);
       Assert.assertEquals(sortedTeam.size(), teams.size());
    }

    @Test
    public void divideIntoBasketTest() {

    }

/*public void playOffSortCntResultTeamWRating() {
        var teams = generateTeams(16);
        var sortedTeam = Sortition.playOffSort(teams, new TestRating(teams));
        Assert.assertEquals(sortedTeam.size(), teams.size());
    }*/

    @Test
    public void playOffSortBadCntTeams() {
        var teams = generateTeams(3);
        var sortedTeam = Sortition.playOffSort(teams, null);
        Assert.assertEquals(sortedTeam, null);
    }

    @Test
    public void groupSort() {
        Assert.assertEquals(0, 0);
    }
}