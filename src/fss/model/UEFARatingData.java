package fss.model;

class UEFARatingData {
    public int year = 0;
    public ClubTeam team = null;
    public Country country = null;
    public double point = 0.0;

    public UEFARatingData(int year, Country country, double point) {
        this.country = country;
        setInitData(year, point);
    }

    public UEFARatingData(int year, ClubTeam team, double point) {
        this.team = team;
        setInitData(year, point);
    }

    private void setInitData(int year, double point) {
        this.year = year;
        this.point = point;
    }
}
