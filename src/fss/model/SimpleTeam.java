package fss.model;
import java.io.Serializable;

abstract class SimpleTeam implements Serializable {
    private int id;
    private Country country;
    private int power;
    private int minPower;
    private int maxPower;

    public SimpleTeam(int id, Country country, int power, int minPower, int maxPower) {
        this.id = id;
        this.country = country;
        this.power = power;
        this.minPower = minPower;
        this.maxPower = maxPower;
    }

    public int getID() { return id; }
    public String getName() { return ""; }
    public WorldPart getWorldPart() { return country.getWorldPart(); }
    public int getPower() { return power; }
    public Country getCountry() {
        return country;
    }

    public void setNewPower() {
        power += RandomWrapper.getRandom(-2, 2);
        if(power > maxPower)
            power = maxPower;

        if(power < minPower)
            power = minPower;
    }

    @Override
    public String toString() {
        return Integer.toString(getID());
    }
}

class NationalTeam extends SimpleTeam {
    public NationalTeam(int id, Country country, int power, int minPower, int maxPower) {
        super(id, country, power, minPower, maxPower);
    }

    @Override
    public String getName() {
        return getCountry().getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}

class ClubTeam extends SimpleTeam {
    private String name;

    public ClubTeam(int id, String name, Country country, int power, int minPower, int maxPower) {
        super(id, country, power, minPower, maxPower);
        this.name = name;
    }

    public ClubTeam(int id, String name, Country country, int power) {
        super(id, country, power, power, power);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}


