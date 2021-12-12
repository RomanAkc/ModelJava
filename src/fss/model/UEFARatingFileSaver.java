package fss.model;

import java.util.ArrayList;

public class UEFARatingFileSaver implements RatingSaveable {
    UEFARating rating = null;

    public UEFARatingFileSaver(UEFARating rating) {
        this.rating = rating;
    }

    public boolean Save() {
        return SaveRating(rating);
    }

    @Override
    public boolean SaveRating(Ratingable rtg) {
        UEFARating rating = (UEFARating)rtg;
        ArrayList<UEFARatingData> data = rating.getRawData();

        //Сохранить в файл (откуда взять его имя?)


        return false;
    }
}
