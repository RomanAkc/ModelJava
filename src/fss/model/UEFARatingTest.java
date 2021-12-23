package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UEFARatingTest extends BaseTest{
    @Test
    public void UEFARatingTestOneYear() {
        var teamsWithCountries = generateClubTeams(8, 3);

        var data = generateRatingDataOneYear(teamsWithCountries);
        var rating = new UEFARating(data);

        checkRatingAndData(teamsWithCountries, data, rating);
    }

    @Test
    public void UEFARatingTestFiveYears() {
        HashMap<ClubTeam, Country> teamsWithCountries = generateClubTeams(8, 3);
        ArrayList<UEFARatingData> data = generateRatingDataFiveYears(teamsWithCountries);
        UEFARating rating = new UEFARating(data);

        checkRatingAndData(teamsWithCountries, data, rating);

        rating.recalcWithChangeData(generateRatingDataOneYear(teamsWithCountries));
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

    private ArrayList<UEFARatingData> generateRatingDataOneYear(HashMap<ClubTeam, Country> teamsWithCountries) {
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

        return data;
    }

    private class CountryPointData
    {
        public double points = 0.0;
        public int cntTeams = 0;

        public CountryPointData(double points, int cntTeams) {
            this.points = points;
            this.cntTeams = cntTeams;
        }
    }

    private ArrayList<UEFARatingData> generateRatingDataFiveYears(HashMap<ClubTeam, Country> teamsWithCountries) {
        var data = new ArrayList<UEFARatingData>();

        for(int year = 2016; year <= 2020; ++year) {
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
        HashMap<ClubTeam, Country> teamsWithCountries = generateClubTeams(8, 3);
        ArrayList<UEFARatingData> data = generateRatingDataFiveYears(teamsWithCountries);
        UEFARating rating = new UEFARating(data);

        String fileName = "UEFARatingTestSaveReadFile.dat";
        writeRatingToFile(rating, fileName);

        Assert.assertTrue(Files.exists(Paths.get(fileName)));

        UEFARating readedRating = readRatingFromFile(fileName);
        compareRatingData(rating.getRawData(), readedRating.getRawData());
    }

    private void writeRatingToFile(UEFARating rating, String fileName) {
        FileOutputStream fileStream = null;
        try {
            fileStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            Assert.assertTrue(false);
        }

        UEFARatingFileSaver saver = new UEFARatingFileSaver(rating, fileStream);
        saver.Save();

        try {
            fileStream.close();
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
    }

    private UEFARating readRatingFromFile(String fileName) {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            Assert.assertTrue(false);
            return null;
        }

        UEFARatingFileReader reader = new UEFARatingFileReader(fileStream);
        UEFARating rating = (UEFARating)reader.ReadRating();
        Assert.assertTrue(rating != null);

        try {
            fileStream.close();
        } catch (IOException e) {
            Assert.assertTrue(false);
        }

        return rating;
    }

    private void compareRatingData(ArrayList<UEFARatingData> data1, ArrayList<UEFARatingData> data2) {
        Assert.assertEquals(data1.size(), data2.size());
    }
}