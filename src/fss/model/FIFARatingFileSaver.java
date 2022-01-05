package fss.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FIFARatingFileSaver implements RatingSaveable {
    private final FIFARating rating;
    private final FileOutputStream fileStream;

    public FIFARatingFileSaver(FIFARating rating, FileOutputStream fileStream) {
        this.rating = rating;
        this.fileStream = fileStream;
    }

    public boolean Save() {
        return SaveRating(rating);
    }

    @Override
    public boolean SaveRating(Ratingable rtg) {
        FIFARating rating = (FIFARating)rtg;

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
            HashMap<NationalTeam, Double> data = rating.getRawData();

            objectOutputStream.writeInt(data.size());
            for(var obj : data.entrySet()) {
                objectOutputStream.writeObject(obj.getKey());
                objectOutputStream.writeDouble(obj.getValue());
            }

            objectOutputStream.flush();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
