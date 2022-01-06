package fss.model;

import java.io.Serializable;

public class NationalTeam extends SimpleTeam implements Serializable {
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
