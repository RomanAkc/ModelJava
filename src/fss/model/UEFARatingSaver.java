package fss.model;

public class UEFARatingSaver implements RatingSaveable {
    UEFARating rating = null;

    public UEFARatingSaver(UEFARating rating) {
        this.rating = rating;
    }

    public boolean Save() {
        return SaveRating(rating);
    }

    @Override
    public boolean SaveRating(Ratingable rating) {
        return false;
    }
}
