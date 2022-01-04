package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class FIFARatingTest extends BaseTest {
    @Test
    public void FIFARatingTestCalculation() {
        ArrayList<NationalTeam> nationals = genereateNational(2);
        HashMap<NationalTeam, Double> ratingByNational = new HashMap<>() {{
            put(nationals.get(0), 457.0);
            put(nationals.get(1), 465.0);
        }};

        FIFARating rating = new FIFARating(ratingByNational);

        rating.addMeet(new MeetTest(nationals.get(0), nationals.get(1), 1, 0), FIFAMeetImportance.WORLD_UNDER14);

        ratingByNational = rating.getRawData();

        Assert.assertEquals(ratingByNational.get(nationals.get(0)), 482.0, 0);
        Assert.assertEquals(ratingByNational.get(nationals.get(1)), 440.0, 0);
    }

}