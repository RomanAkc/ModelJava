package fss.model;

public class UEFARatingFileSaver implements RatingSaveable {
    UEFARating rating = null;

    public UEFARatingFileSaver(UEFARating rating) {
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
