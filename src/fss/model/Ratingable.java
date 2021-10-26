package fss.model;

interface Ratingable {
    int getTeamPosition(SimpleTeam team);
}

interface CountryRatingable {
    Country getCountryByPosition(int position);
    int getAllCountries();
}
