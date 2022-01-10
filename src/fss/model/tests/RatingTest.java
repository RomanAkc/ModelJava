package fss.model.tests;

import fss.model.Ratingable;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RatingTest extends BaseTest {
    protected void saveRating(Ratingable rating, FileOutputStream fileStream) {
        Assert.fail();
    }

    protected void writeRatingToFile(Ratingable rating, String fileName) {
        FileOutputStream fileStream = null;
        try {
            fileStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            Assert.fail();
        }

        saveRating(rating, fileStream);

        try {
            fileStream.close();
        } catch (IOException e) {
            Assert.fail();
        }
    }

    protected Ratingable readRating(FileInputStream fileStream) {
        Assert.fail();
        return null;
    }

    protected Ratingable readRatingFromFile(String fileName) {
        FileInputStream fileStream;
        try {
            fileStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return null;
        }

        Ratingable rating = readRating(fileStream);
        Assert.assertNotNull(rating);

        try {
            fileStream.close();
        } catch (IOException e) {
            Assert.fail();
        }

        return rating;
    }
}
