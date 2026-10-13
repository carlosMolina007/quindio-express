package com.uniquindio.edu.model;

import java.util.Comparator;

public class DeliveryPriorityComparator implements Comparator<Package> {

    @Override
    public int compare(Package p1, Package p2) {
        //Mayor prioridad, primera regla
        int priorityCompare = Integer.compare(p2.getPriority(), p1.getPriority());
        if (priorityCompare != 0) {
            return priorityCompare;
        }

        //Menor tiempo de entrega, segunda regla
        int timeCompare = Integer.compare(p1.getEstimatedTime(), p2.getEstimatedTime());
        if (timeCompare != 0) {
            return timeCompare;
        }

        //Ordenamiento alfabetico, tercera regla
        return p1.getCode().compareTo(p2.getCode());
    }
}
