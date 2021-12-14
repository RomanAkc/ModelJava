package fss.model;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class UEFARatingFileSaver implements RatingSaveable {
    UEFARating rating = null;
    FileOutputStream fileStream = null;

    public UEFARatingFileSaver(UEFARating rating, FileOutputStream fileStream) {
        this.rating = rating;
        this.fileStream = fileStream;
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
