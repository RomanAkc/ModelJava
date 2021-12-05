package fss.model;

interface RatingSaveable {
    boolean SaveRating(Ratingable rating);
}

interface RatingReadable {
    Ratingable ReadRating();
}