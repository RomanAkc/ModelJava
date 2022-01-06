package fss.model;
import java.io.Serializable;

public abstract class SimpleTeam implements Serializable {
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


