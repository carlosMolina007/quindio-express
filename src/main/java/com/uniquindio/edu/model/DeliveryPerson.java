package com.uniquindio.edu.model;

import java.util.Objects;

public class DeliveryPerson {

    private final String id;
    private final String name;
    private final String zone;
    private boolean available;

    public DeliveryPerson(String id, String name, String zone, boolean available) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (zone == null || zone.trim().isEmpty()) {
            throw new IllegalArgumentException("Zone cannot be null or empty");
        }

        this.id = id.trim();
        this.name = name.trim();
        this.zone = zone.trim();
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getZone() {
        return zone;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryPerson dPerson = (DeliveryPerson) o;
        return Objects.equals(id, dPerson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("DeliveryPerson[ID=%s, Name=%s, Zone=%s, Available=%b]",
                id, name, zone, available);
    }
}
