package fss.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FIFARatingFileReader implements RatingReadable {
    private FileInputStream fileStream;

    public FIFARatingFileReader(FileInputStream fileStream) {
        this.fileStream = fileStream;
    }

    @Override
    public Ratingable ReadRating() {
        FIFARating rating = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
            rating = (FIFARating) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

        return rating;
    }
}
