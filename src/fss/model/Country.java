package fss.model;

import java.io.Serializable;

class Country implements Serializable {
    private int id;
    private String name;
    private WorldPart worldPart;

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
