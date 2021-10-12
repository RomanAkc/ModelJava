package fss.model;

interface Ratingable {
    int getTeamPosition(SimpleTeam team);
}

interface CountryRatingable {
    String getCountryByPosition(int position);
}