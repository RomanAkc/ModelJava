package fss.model.tests;

import fss.model.*;
import fss.model.tests.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class UEFARatingTest extends BaseTest {
    @Test
    public void UEFARatingTestFiveYears() {
        HashMap<ClubTeam, Country> teamsWithCountries = generateClubTeamsWithCountries(8, 3);
        ArrayList<UEFARatingData> data = generateRatingDataForYears(teamsWithCountries, 2016, 2021);
        UEFARating rating = new UEFARating(data);

        checkRatingAndData(teamsWithCountries, data, rating);

        rating.recalcWithChangeData(generateRatingDataForYears(teamsWithCountries, 2022, 2022));
        checkRatingAndData(teamsWithCountries, rating.getRawData(), rating);
    }

    private void checkRatingAndData(HashMap<ClubTeam, Country> teamsWithCountries, ArrayList<UEFARatingData> data, UEFARating rating) {
        HashMap<ClubTeam, Double> clubToPoint = getMapClubToPoint(data);
        for (var team1 : teamsWithCountries.keySet()) {
            for (var team2 : teamsWithCountries.keySet()) {
                if (team1 == team2) {
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
        for (int i = 1; i < rating.getAllCountries(); ++i) {
            Country country = rating.getCountryByPosition(i);
            double point = countryToPoint.get(country);
            if (i > 1) {
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

            double points = 0.0;
            if(result.containsKey(obj.team)) {
                points = result.get(obj.team);
            }

            result.put(obj.team, points + obj.point);
        }

        return result;
    }

    private HashMap<Country, Double> getMapCountryToPoint(ArrayList<UEFARatingData> data) {
        HashMap<Country, Double> result = new HashMap<>();

        for(var obj : data) {
            if(obj.country == null) {
                continue;
            }

            double points = 0.0;
            if(result.containsKey(obj.country)) {
                points = result.get(obj.country);
            }

            result.put(obj.country, points + obj.point);
        }

        return result;
    }

    private static class CountryPointData
    {
        public double points;
        public int cntTeams;

        public CountryPointData(double points, int cntTeams) {
            this.points = points;
            this.cntTeams = cntTeams;
        }
    }

    private ArrayList<UEFARatingData> generateRatingDataForYears(HashMap<ClubTeam, Country> teamsWithCountries, int yearStart, int yearEnd) {
        var data = new ArrayList<UEFARatingData>();

        for(int year = yearStart; year <= yearEnd; ++year) {
            HashMap<Country, CountryPointData> countriesPoints = new HashMap<>();

            for(var kv : teamsWithCountries.entrySet()) {
                ClubTeam team = kv.getKey();
                double teamPoints = (double) RandomWrapper.getRandom(0, 20000) / 1000.0;
                data.add(new UEFARatingData(year, team, teamPoints));

                Country country = kv.getValue();
                if(!countriesPoints.containsKey(country)) {
                    countriesPoints.put(country, new CountryPointData(teamPoints, 1));
                } else {
                    CountryPointData countryPoints = countriesPoints.get(country);
                    countryPoints.cntTeams++;
                    countryPoints.points += teamPoints;
                }
            }

            for(var countryPoints : countriesPoints.entrySet()) {
                data.add(new UEFARatingData(year, countryPoints.getKey(), getSumCountryPoints(countryPoints.getValue().cntTeams, countryPoints.getValue().points)));
            }
        }

        return data;
    }

    private double getSumCountryPoints(int cntClubs, double countryPoints) {
        return (cntClubs == 0 ? 0.0 : countryPoints / (double) cntClubs) * 0.2;
    }

    @Test
    public void UEFARatingTestSaveReadFile() {
        HashMap<ClubTeam, Country> teamsWithCountries = generateClubTeamsWithCountries(8, 3);
        ArrayList<UEFARatingData> data = generateRatingDataForYears(teamsWithCountries, 2016, 2021);
        UEFARating rating = new UEFARating(data);

        String fileName = "UEFARatingTestSaveReadFile.dat";
        writeRatingToFile(rating, fileName);

        Assert.assertTrue(Files.exists(Paths.get(fileName)));

        UEFARating readRating = (UEFARating) readRatingFromFile(fileName);
        if(readRating != null) {
            compareRatingData(rating.getRawData(), readRating.getRawData());
        } else {
            Assert.fail();
        }

        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Override
    protected void saveRating(Ratingable rating, FileOutputStream fileStream) {
        UEFARatingFileSaver saver = new UEFARatingFileSaver((UEFARating) rating, fileStream);
        saver.Save();
    }

    @Override
    protected Ratingable readRating(FileInputStream fileStream) {
        UEFARatingFileReader reader = new UEFARatingFileReader(fileStream);
        return reader.ReadRating();
    }

    private static class CompareUEFARatingData implements Comparator<UEFARatingData> {
        @Override
        public int compare(UEFARatingData lhs, UEFARatingData rhs) {
            if(lhs.year != rhs.year) {
                return lhs.year < rhs.year ? -1 : 1;
            }

            if(lhs.team != null && rhs.team != null) {
                if(lhs.team == rhs.team)
                    return 0;

                return lhs.team.getID() < rhs.team.getID() ? -1 : 1;
            }

            if(lhs.team != null)
                return 1;

            if(rhs.team != null)
                return -1;

            if(lhs.country == rhs.country)
                return 0;

            return lhs.country.getID() < rhs.country.getID() ? -1 : 1;
        }
    }

    private void compareRatingData(ArrayList<UEFARatingData> d1, ArrayList<UEFARatingData> d2) {
        Assert.assertEquals(d1.size(), d2.size());

        ArrayList<UEFARatingData> data1 = new ArrayList<>(d1);
        ArrayList<UEFARatingData> data2 = new ArrayList<>(d2);

        data1.sort(new CompareUEFARatingData());
        data2.sort(new CompareUEFARatingData());

        int size = data1.size();
        for(int i = 0; i < size; ++i) {
            UEFARatingData obj1 = data1.get(i);
            UEFARatingData obj2 = data2.get(i);

            Assert.assertEquals(obj1.year, obj2.year);
            Assert.assertEquals(obj1.point, obj2.point, 3);

            Assert.assertEquals(obj1.team != null, obj2.team != null);
            if(obj1.team != null) {
                Assert.assertEquals(obj1.team.getID(), obj2.team.getID());
                Assert.assertEquals(obj1.team.getName(), obj2.team.getName());
            }

            Assert.assertEquals(obj1.country != null, obj2.country != null);
            if(obj1.country != null) {
                Assert.assertEquals(obj1.country.getID(), obj2.country.getID());
                Assert.assertEquals(obj1.country.getName(), obj2.country.getName());
            }
        }
    }
}