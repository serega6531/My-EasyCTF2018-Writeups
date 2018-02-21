package ru.serega6531.ctf;

public class Record {
    private int geoid;
    private int population;
    private int housingUnit;
    private long land;
    private long water;
    private double landSqmi;
    private double waterSqmi;
    private double latitude;
    private double longitude;

    public Record(int geoid, int population, int housingUnit, long land, long water, double landSqmi, double waterSqmi, double latitude, double longitude) {
        this.geoid = geoid;
        this.population = population;
        this.housingUnit = housingUnit;
        this.land = land;
        this.water = water;
        this.landSqmi = landSqmi;
        this.waterSqmi = waterSqmi;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getGeoid() {
        return geoid;
    }

    public int getPopulation() {
        return population;
    }

    public int getHousingUnit() {
        return housingUnit;
    }

    public long getLand() {
        return land;
    }

    public long getWater() {
        return water;
    }

    public double getLandSqmi() {
        return landSqmi;
    }

    public double getWaterSqmi() {
        return waterSqmi;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}