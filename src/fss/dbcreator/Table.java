package fss.dbcreator;

import java.util.ArrayList;

public class Table {

    public static abstract class TableField {
        private String name;

        public TableField(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract FieldType getType();
    }

    public static class IntegerField extends TableField {
        private int defaultValue = 0;

        public IntegerField(String name) {
            super(name);
        }

        public IntegerField(String name, int defaultValue) {
            super(name);
            this.defaultValue = defaultValue;
        }

        @Override
        public FieldType getType() {
            return FieldType.INTEGER;
        }

        public void setDefaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
        }

        public int getDefaultValue() {
            return defaultValue;
        }
    }

    public static class StringField extends TableField {
        private String defaultValue = "";

        public StringField(String name) {
            super(name);
        }

        @Override
        public FieldType getType() {
            return FieldType.STRING;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    private String name;
    private ArrayList<TableField> fields = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    public void addField(TableField field) {
        fields.add(field);
    }

    public String getName() {
        return name;
    }

    public ArrayList<TableField> getFields() {
        return fields;
    }
}
