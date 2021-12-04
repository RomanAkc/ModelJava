package fss.model;

public interface RatingSaveableReabable {
    boolean SaveRating(Ratingable rating);
    Ratingable ReadRating();
}
