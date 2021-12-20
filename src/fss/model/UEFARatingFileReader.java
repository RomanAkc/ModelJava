package fss.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class UEFARatingFileReader implements RatingReadable {
    private FileInputStream fileStream = null;
    public UEFARatingFileReader(FileInputStream fileStream) {
        this.fileStream = fileStream;
    }

    @Override
    public Ratingable ReadRating() {
        ArrayList<UEFARatingData> data = new ArrayList<>();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
            int cnt = objectInputStream.readInt();
            for(int i = 0; i < cnt; ++i) {
                UEFARatingData obj = (UEFARatingData) objectInputStream.readObject();
                data.add(obj);
            }

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

        UEFARating rating = new UEFARating(data);
        return rating;
    }
}
