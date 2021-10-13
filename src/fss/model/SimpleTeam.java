package fss.model;
import java.io.Serializable;

public abstract class SimpleTeam implements Serializable {
    public enum WorldPart {
        EUROPE,
        SOUTH_AMERICA,
        NORD_AMERICA,
        AFRICA,
        ASIA,
        OCEANIA
    }

    private int id;
    private String name;
    private WorldPart worldPart;
    private int power;
    private int minPower;
    private int maxPower;

    public SimpleTeam(int id, String name, WorldPart worldPart, int power, int minPower, int maxPower) {
        this.id = id;
        this.name = name;
        this.worldPart = worldPart;
        this.power = power;
        this.minPower = minPower;
        this.maxPower = maxPower;
    }

    public int getID() { return id; }
    public String getName() { return name; }
    public WorldPart getWorldPart() { return worldPart; }
    public int getPower() { return power; }

    public void setNewPower() {
        power += RandomWrapper.getRandom(-2, 2);
        if(power > maxPower)
            power = maxPower;

        if(power < minPower)
            power = minPower;
    }

    @Override
    public String toString() {
        return getName();
    }
}

class NationalTeam extends SimpleTeam {
    public NationalTeam(int id, String name, WorldPart worldPart, int power, int minPower, int maxPower) {
        super(id, name, worldPart, power, minPower, maxPower);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

class ClubTeam extends SimpleTeam {
    private String country;

    public ClubTeam(int id, String name, String country, WorldPart worldPart, int power, int minPower, int maxPower) {
        super(id, name, worldPart, power, minPower, maxPower);
        this.country = country;
    }

    public ClubTeam(int id, String name, String country, WorldPart worldPart, int power) {
        super(id, name, worldPart, power, power, power);
        this.country = country;
    }

    String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}


