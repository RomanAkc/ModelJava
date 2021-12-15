package fss.model;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UEFARatingFileSaver implements RatingSaveable {
    UEFARating rating = null;
    FileOutputStream fileStream = null;

    public UEFARatingFileSaver(UEFARating rating, FileOutputStream fileStream) {
        this.rating = rating;
        this.fileStream = fileStream;
    }

    public boolean Save() throws IOException {
        return SaveRating(rating);
    }

    //Прочитать про слово throws
    @Override
    public boolean SaveRating(Ratingable rtg) throws IOException {
        UEFARating rating = (UEFARating)rtg;
        ArrayList<UEFARatingData> data = rating.getRawData();

        fileStream.write(Integer.toString(data.size()).getBytes());


        return false;
    }
}
