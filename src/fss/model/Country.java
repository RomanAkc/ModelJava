package fss.model;

class Country {
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

    public int getId() {
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
