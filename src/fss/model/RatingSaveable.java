package fss.model;

import java.io.IOException;

interface RatingSaveable {
    boolean SaveRating(Ratingable rating) throws IOException;
}

interface RatingReadable {
    Ratingable ReadRating();
}