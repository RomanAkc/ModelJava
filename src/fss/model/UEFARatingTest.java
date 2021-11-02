package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class UEFARatingTest extends BaseTest{
    @Test
    public void UEFARatingTest() {
        var teamsWithCountries = generateClubTeams(8, 3);
        var countryWithoutTeam = generateCountryWithID(100500);

        var data = generateUEFARatingData(teamsWithCountries, countryWithoutTeam);
        var rating = new UEFARating(data);

        HashMap<ClubTeam, Double> clubToPoint = getMapClubToPoint(data);
        for(var team1 : teamsWithCountries.keySet()) {
            for(var team2 : teamsWithCountries.keySet()) {
                if(team1 == team2) {
                    continue;
                }

                int pos1 = rating.getTeamPosition(team1);
                int pos2 = rating.getTeamPosition(team2);

                double pnt1 = clubToPoint.get(team1);
                double pnt2 = clubToPoint.get(team2);

                Assert.assertEquals(pos1 < pos2, pnt1 >= pnt2);
            }
        }

        HashMap<Country, Double> countryToPoint = getMapCountryToPoint(data);
        double prevPoint = -1;
        for(int i = 1; i < rating.getAllCountries(); ++i) {
            Country country = rating.getCountryByPosition(i);
            double point = countryToPoint.get(country);
            if(i > 1) {
                Assert.assertTrue(point <= prevPoint);
            }
            prevPoint = point;
        }
    }



    private HashMap<ClubTeam, Double> getMapClubToPoint(ArrayList<UEFARatingData> data) {
        HashMap<ClubTeam, Double> result = new HashMap<>();

        for(var obj : data) {
            if(obj.team == null) {
                continue;
            }

            result.put(obj.team, obj.point);
        }

        return result;
    }

    private HashMap<Country, Double> getMapCountryToPoint(ArrayList<UEFARatingData> data) {
        HashMap<Country, Double> result = new HashMap<>();

        for(var obj : data) {
            if(obj.country == null) {
                continue;
            }

            result.put(obj.country, obj.point);
        }

        return result;
    }

    private ArrayList<UEFARatingData> generateUEFARatingData(HashMap<ClubTeam, Country> teamsWithCountries,
                                                             Country countryWithoutTeam) {
        var data = new ArrayList<UEFARatingData>();

        HashSet<Country> usedCountries = new HashSet<>();
        double pointCountry = 1.0;
        double pointClub = 3.0;
        double addPoint = 0.0;
        for(var obj : teamsWithCountries.entrySet()) {
            if(!usedCountries.contains(obj.getValue())) {
                var currentCountry = obj.getValue();
                usedCountries.add(currentCountry);
                pointCountry = 1.0;
                pointClub = 3.0;
                data.add(new UEFARatingData(2021, currentCountry, pointCountry + addPoint));

            }

            data.add(new UEFARatingData(2021, obj.getKey(), pointClub + addPoint));
            addPoint += 0.1;
        }

        data.add(new UEFARatingData(2021, countryWithoutTeam, 2.22));
        return data;
    }

}