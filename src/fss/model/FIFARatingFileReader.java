package fss.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class FIFARatingFileReader implements RatingReadable {
    private final FileInputStream fileStream;

    public FIFARatingFileReader(FileInputStream fileStream) {
        this.fileStream = fileStream;
    }

    @Override
    public Ratingable ReadRating() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

            HashMap<NationalTeam, Double> ratingByNational = new HashMap<>();
            int size = objectInputStream.readInt();
            for(int i = 0; i < size; ++i) {
                NationalTeam team = (NationalTeam) objectInputStream.readObject();
                double value = objectInputStream.readDouble();
                ratingByNational.put(team, value);
            }

            return new FIFARating(ratingByNational);
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
