package fss.model;

import java.util.HashMap;

public class UEFARatingCalculator {
    private HashMap<Integer, UEFARatingSchemePart> ratingScheme = new HashMap<>();

    public UEFARatingCalculator() {

    }

    public void addRatingScheme(UEFARatingSchemePart schemePart) {
        ratingScheme.put(schemePart.stageID, schemePart);
    }


}
