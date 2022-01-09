package fss.model.tests;

import fss.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class FIFARatingTest extends BaseTest {
    static final double ratingChange = 8.0;

    private static final class RatingGeneratedData {
        ArrayList<NationalTeam> nationals = null;
        HashMap<NationalTeam, Double> ratingByNational = null;
        FIFARating rating = null;
    }

    @Test
    public void FIFARatingCalculation() {
        RatingGeneratedData genData = generateData(2);

        //Gameable
        genData.rating.addMeet(new MeetTest(genData.nationals.get(0), genData.nationals.get(1), 1, 0)
                , FIFAMeetImportance.WORLD_UNDER14);
        checkCalculatedValue(genData, 482.0, 440.0);

        genData.rating.addMeet(new MeetTest(genData.nationals.get(0), genData.nationals.get(1), 0, 0)
                , FIFAMeetImportance.WORLD_UNDER14);
        checkCalculatedValue(genData, 480.0, 442.0);

        genData.rating.addMeet(new MeetTest(genData.nationals.get(0), genData.nationals.get(1), 0, 1)
                , FIFAMeetImportance.WORLD_UNDER14);
        checkCalculatedValue(genData, 453.0, 469.0);

        //WinGameable
        genData.rating.addMeet(new WinMeetTest(genData.nationals.get(0), genData.nationals.get(1), 1, 0)
                , FIFAMeetImportance.WORLD_BEGINFROM14);
        checkCalculatedValue(genData, 484.0, 438.0);

        genData.rating.addMeet(new WinMeetTest(genData.nationals.get(0), genData.nationals.get(1), 0, 0, 1, 0)
                , FIFAMeetImportance.CONFEDERATION_UNDER14);
        checkCalculatedValue(genData, 500.0, 422.0);

        genData.rating.addMeet(new WinMeetTest(genData.nationals.get(0), genData.nationals.get(1), 0, 0, 0, 0, 6, 5)
                , FIFAMeetImportance.CONFEDERATION_BEGINFROM14);
        checkCalculatedValue(genData, 507.0, 425.0);

        genData.rating.addMeet(new WinMeetTest(genData.nationals.get(0), genData.nationals.get(1), 0, 1)
                , FIFAMeetImportance.WORLD_BEGINFROM14);
        checkCalculatedValue(genData, 472.0, 460.0);

        genData.rating.addMeet(new WinMeetTest(genData.nationals.get(0), genData.nationals.get(1), 0, 0, 0, 1)
                , FIFAMeetImportance.FRIENDLY_IN);
        checkCalculatedValue(genData, 467.0, 465.0);

        genData.rating.addMeet(new WinMeetTest(genData.nationals.get(0), genData.nationals.get(1), 0, 0, 0, 0, 5, 6)
                , FIFAMeetImportance.FRIENDLY_OUT);
        checkCalculatedValue(genData, 467.0, 466.0);

        //WinTwoGameable
        WinTwoMeetTest meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(1, 0).SetSecondMeetResult(0, 1);
        genData.rating.addMeet(meet, FIFAMeetImportance.NATION_LEAGUE_PLAYOFF);
        checkCalculatedValue(genData, 491.0, 442.0);

        meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(1, 0).SetSecondMeetResult(0, 0);
        genData.rating.addMeet(meet, FIFAMeetImportance.NATION_LEAGUE_PLAYOFF);
        checkCalculatedValue(genData, 500.0, 433.0);

        meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(1, 0).SetSecondMeetResult(2, 0);
        genData.rating.addMeet(meet, FIFAMeetImportance.NATION_LEAGUE_PLAYOFF);
        checkCalculatedValue(genData, 496.0, 437.0);

        meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(0, 0).SetSecondMeetResult(1, 0);
        genData.rating.addMeet(meet, FIFAMeetImportance.QUALIFYING);
        checkCalculatedValue(genData, 481.0, 452.0);

        meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(0, 0).SetSecondMeetResult(1, 1).SetAddTimeMeetResult(1, 0);
        genData.rating.addMeet(meet, FIFAMeetImportance.QUALIFYING);
        checkCalculatedValue(genData, 479.0, 454.0);

        meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(0, 0).SetSecondMeetResult(0, 1);
        genData.rating.addMeet(meet, FIFAMeetImportance.QUALIFYING);
        checkCalculatedValue(genData, 490.0, 443.0);

        meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(0, 1).SetSecondMeetResult(0, 2);
        genData.rating.addMeet(meet, FIFAMeetImportance.QUALIFYING);
        checkCalculatedValue(genData, 488.0, 445.0);

        meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(0, 1).SetSecondMeetResult(0, 0);
        genData.rating.addMeet(meet, FIFAMeetImportance.QUALIFYING);
        checkCalculatedValue(genData, 474.0, 459.0);

        meet = new WinTwoMeetTest(genData.nationals.get(0), genData.nationals.get(1))
                .SetFirstMeetResult(0, 1).SetSecondMeetResult(1, 0);
        genData.rating.addMeet(meet, FIFAMeetImportance.QUALIFYING);
        checkCalculatedValue(genData, 449.0, 484.0);
    }

    private void checkCalculatedValue(RatingGeneratedData genData, double actual0, double actual1) {
        genData.ratingByNational = genData.rating.getRawData();
        Assert.assertEquals(actual0, genData.ratingByNational.get(genData.nationals.get(0)), 0);
        Assert.assertEquals(actual1, genData.ratingByNational.get(genData.nationals.get(1)), 0);
    }

    private RatingGeneratedData generateData(int cntNationals) {
        RatingGeneratedData data = new RatingGeneratedData();
        data.nationals = genereateNational(cntNationals);
        data.ratingByNational = getRatingByNational(data.nationals);
        data.rating = new FIFARating(data.ratingByNational);
        return data;
    }

    private HashMap<NationalTeam, Double> getRatingByNational(ArrayList<NationalTeam> nationals) {
        double curValue = 457.0;
        HashMap<NationalTeam, Double> result = new HashMap<>();
        for (NationalTeam national : nationals) {
            result.put(national, curValue);
            curValue += ratingChange;
        }
        return result;
    }

    @Override
    protected void saveRating(Ratingable rating, FileOutputStream fileStream) {
        FIFARatingFileSaver saver = new FIFARatingFileSaver((FIFARating) rating, fileStream);
        saver.Save();
    }

    @Override
    protected Ratingable readRating(FileInputStream fileStream) {
        FIFARatingFileReader reader = new FIFARatingFileReader(fileStream);
        return reader.ReadRating();
    }

    @Test
    public void saveReadFile() {
        RatingGeneratedData genData = generateData(170);
        String fileName = "FIFARatingTestSaveReadFile.dat";

        writeRatingToFile(genData.rating, fileName);
        Assert.assertTrue(Files.exists(Paths.get(fileName)));

        FIFARating readRating = (FIFARating) readRatingFromFile(fileName);
        if(readRating != null) {
            Assert.assertTrue(compareRatingData(genData.rating.getRawData(), readRating.getRawData()));
        } else {
            Assert.fail();
        }

        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    private boolean compareRatingData(HashMap<NationalTeam, Double> oldData, HashMap<NationalTeam, Double> newData) {
        TreeMap<NationalTeam, Double> mapNewData = new TreeMap<>(Comparator.comparing(NationalTeam::getName));
        mapNewData.putAll(newData);

        for(var kv : oldData.entrySet()) {
            if(!mapNewData.containsKey(kv.getKey())) {
                return false;
            }

            if(Math.abs(kv.getValue() - mapNewData.get(kv.getKey())) != 0.0) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void checkRatingPosition() {
        RatingGeneratedData genData = generateData(170);

        for(int i = 1; i < genData.nationals.size(); ++i) {
            int posI = genData.rating.getTeamPosition(genData.nationals.get(i));
            int posI1 = genData.rating.getTeamPosition(genData.nationals.get(i - 1));
            Assert.assertTrue(posI < posI1);
        }
    }
}