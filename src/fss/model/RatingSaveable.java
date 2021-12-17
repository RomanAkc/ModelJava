package fss.model;

import java.io.IOException;

interface RatingSaveable {
    boolean SaveRating(Ratingable rating);
}

interface RatingReadable {
    Ratingable ReadRating();
}