package org.example.payment.model;

public enum BillType {
    ELECTRIC("ELECTRIC"),
    WATER("WATER"),
    INTERNET("INTERNET");

    BillType(String typeName) {
        name = typeName;
    }

    public String getName() {
        return name;
    }

    private final String name;

    public static BillType getFromTypeName(String typeName) {
        for (BillType t:BillType.values()) {
            if(t.getName().equals(typeName)){
                return t;
            }
        }
        return null;
    }
}
