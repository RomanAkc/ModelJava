package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FIFARatingTest extends BaseTest {
    @Test
    public void FIFARatingTestCalculation() {
        ArrayList<NationalTeam> nationals = genereateNational(2);
        HashMap<NationalTeam, Double> ratingByNational = getRatingByNational(nationals);
        FIFARating rating = new FIFARating(ratingByNational);

        rating.addMeet(new MeetTest(nationals.get(0), nationals.get(1), 1, 0), FIFAMeetImportance.WORLD_UNDER14);

        ratingByNational = rating.getRawData();

        Assert.assertEquals(ratingByNational.get(nationals.get(0)), 482.0, 0);
        Assert.assertEquals(ratingByNational.get(nationals.get(1)), 440.0, 0);
    }

    private HashMap<NationalTeam, Double> getRatingByNational(ArrayList<NationalTeam> nationals) {
        HashMap<NationalTeam, Double> ratingByNational = new HashMap<>() {{
            put(nationals.get(0), 457.0);
            put(nationals.get(1), 465.0);
        }};
        return ratingByNational;
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
    public void SaveReadFile() {
        ArrayList<NationalTeam> nationals = genereateNational(2);
        HashMap<NationalTeam, Double> ratingByNational = getRatingByNational(nationals);
        FIFARating rating = new FIFARating(ratingByNational);

        String fileName = "FIFARatingTestSaveReadFile.dat";

        writeRatingToFile(rating, fileName);

        Assert.assertTrue(Files.exists(Paths.get(fileName)));

        FIFARating readedRating = (FIFARating) readRatingFromFile(fileName);
        if(readedRating != null) {
            Assert.assertTrue(compareRatingData(rating.getRawData(), readedRating.getRawData()));
        } else {
            Assert.fail();
        }

        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    private boolean compareRatingData(HashMap<NationalTeam, Double> data1, HashMap<NationalTeam, Double> data2) {
        return true;
    }

}