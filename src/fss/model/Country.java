package fss.model;

import java.io.Serializable;

public class Country implements Serializable {
    private final int id;
    private final String name;
    private final WorldPart worldPart;

    public Country(int id, String name, WorldPart worldPart) {
        this.id = id;
        this.name = name;
        this.worldPart = worldPart;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public WorldPart getWorldPart() {
        return worldPart;
    }

    @Override
    public String toString() {
        return name;
    }
}
