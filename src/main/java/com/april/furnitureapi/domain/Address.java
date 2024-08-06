package com.april.furnitureapi.domain;

public enum Address {
    FONTENOY_STR("Dublin warehouse", "Fontenoy st", 3, 53.35544810905507, -6.269277760670421);

    private final String displayName;
    private final String street;
    private final Integer houseNo;
    private final double latitude;
    private final double longitude;

    Address(String displayName, String street, Integer houseNo, double latitude, double longitude) {
        this.street = street;
        this.houseNo = houseNo;
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getStreet() {
        return street;
    }

    public Integer getHouseNo() {
        return houseNo;
    }
}
