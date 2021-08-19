package fss.model;

import org.junit.Assert;
import org.junit.Test;

public class SortitionTest extends BaseTest {
    @Test
    public void playOffSortCntResultTeamWORating() {
       var teams = generateTeams(4);
       var meets = Sortition.playOffSort(teams, null, false);
       Assert.assertEquals(meets.size(), teams.size() / 2);
    }

    @Test
    public void playOffSortWithRating() {
        int cntTeams = 8;
        var teams = generateTeams(cntTeams);
        var meets = Sortition.playOffSort(teams, new TestRating(teams), false);

        var head = teams.subList(0, teams.size() / 2);
        var tail = teams.subList(teams.size() / 2, teams.size());

        for(var meet : meets) {
            Assert.assertTrue(tail.contains(meet.getTeamHome()));
            Assert.assertTrue(head.contains(meet.getTeamAway()));
        }
    }

    @Test
    public void playOffSortBadCntTeams() {
        var teams = generateTeams(3);
        var sortedTeam = Sortition.playOffSort(teams, null, false);
        Assert.assertEquals(sortedTeam, null);
    }

    @Test
    public void groupSortWORatingNotPrecise() {
        groupSortWORating(22, 4);
    }

    @Test
    public void groupSortWORatingPrecise() {
        groupSortWORating(24, 4);
    }

    @Test
    public void groupSortWithRatingNotPrecise() {
        groupSortWithRating(22, 4);
    }

    @Test
    public void groupSortWithRatingPrecise() {
        groupSortWithRating(24, 4);
    }

    @Test
    public void groupSortBadCnt() {
        var teams = generateTeams(7);
        var groups = Sortition.groupSort(teams, 4,  null);
        Assert.assertEquals(groups, null);
    }

    private void groupSortWORating(int cntTeams, int cntGroups) {
        var teams = generateTeams(cntTeams);
        var groups = Sortition.groupSort(teams, cntGroups,  null);

        int numTeamInGroup = cntTeams / cntGroups;
        int rest = cntTeams % cntGroups;

        for(var group : groups) {
            int numTeam = numTeamInGroup;
            if(rest > 0) {
                numTeam += 1;
                rest -= 1;
            }

            Assert.assertEquals(numTeam, group.size());
        }
    }

    private void groupSortWithRating(int cntTeams, int cntGroups) {
        var teams = generateTeams(cntTeams);
        var groups = Sortition.groupSort(teams, cntGroups, new TestRating(teams));

        int index = 0;
        while (true) {
            var endIndex = index + cntGroups;
            if(endIndex > teams.size())
                endIndex = teams.size();
            var tmpBasket = teams.subList(index, endIndex);

            for(var group : groups) {
                var prevSize = group.size();
                if(prevSize == 0) {
                    break;
                }

                group.removeAll(tmpBasket);
                Assert.assertEquals(group.size(), prevSize - 1);
            }

            index = endIndex;
            if(index >= teams.size())
                break;
        }
    }
}