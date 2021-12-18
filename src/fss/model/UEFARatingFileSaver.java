package fss.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UEFARatingFileSaver implements RatingSaveable {
    private UEFARating rating = null;
    private FileOutputStream fileStream = null;

    public UEFARatingFileSaver(UEFARating rating, FileOutputStream fileStream) {
        this.rating = rating;
        this.fileStream = fileStream;
    }

    public boolean Save() {
        return SaveRating(rating);
    }

    //TODO: добавить класс для проверки сериализации/десириализации.
    //В статье https://habr.com/ru/post/431524/ сказано, что
    //даже если внутри 2-х объектов ссылка на один и тот же
    //объект, то он будет сериализован (и восстановлен) только один раз
    //надо проверить данный факт
    @Override
    public boolean SaveRating(Ratingable rtg) {
        UEFARating rating = (UEFARating)rtg;
        ArrayList<UEFARatingData> data = rating.getRawData();

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
            objectOutputStream.writeInt(data.size());
            for(var obj : data) {
                objectOutputStream.writeObject(obj);
            }
            objectOutputStream.flush();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
