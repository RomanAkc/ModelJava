package fss.model;

import java.io.Serializable;

public class ClubTeam extends SimpleTeam implements Serializable {
    private final String name;

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
