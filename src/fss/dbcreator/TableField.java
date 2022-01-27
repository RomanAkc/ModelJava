package fss.dbcreator;

public class TableField {
    private String name;
    private FieldType type;

    public TableField(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }
}
