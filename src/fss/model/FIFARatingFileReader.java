package fss.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FIFARatingFileReader implements RatingReadable {
    private final FileInputStream fileStream;

    public FIFARatingFileReader(FileInputStream fileStream) {
        this.fileStream = fileStream;
    }

    @Override
    public Ratingable ReadRating() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
            return (FIFARating) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
