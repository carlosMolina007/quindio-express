package com.uniquindio.edu.model;

import java.util.Objects;

public class Package implements Comparable<Package> {

    private final String code;
    private final String destination;
    private final double weight;
    private final int priority;
    private final int estimatedTime;

    public Package(String code, String destination, double weight, int priority, int estimatedTime) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero");
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Priority must be between 1 and 5");
        }
        if (estimatedTime <= 0) {
            throw new IllegalArgumentException("Estimated time must be greater than zero");
        }

        this.code = code.trim();
        this.destination = destination.trim();
        this.weight = weight;
        this.priority = priority;
        this.estimatedTime = estimatedTime;
    }

    public String getCode() {
        return code;
    }

    public String getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public int getPriority() {
        return priority;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    @Override
    public int compareTo(Package other) {
        return this.code.compareTo(other.code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return Objects.equals(code, aPackage.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("Package[Code=%s, Dest=%s, Weight=%.1fkg, Priority=%d, Time=%dmin]",
                code, destination, weight, priority, estimatedTime);
    }
}
