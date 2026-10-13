package com.uniquindio.edu.service;

import com.uniquindio.edu.genericlist.GenericLinkedList;
import com.uniquindio.edu.model.DeliveryPerson;
import com.uniquindio.edu.model.DeliveryPriorityComparator;
import com.uniquindio.edu.model.Package;

import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DistributionCenterService {

    private final Queue<Package> arrivalQueue = new ArrayDeque<>();
    private final Map<String, Package> packageByCodeMap = new HashMap<>();
    private final Set<String> uniqueDestinationsSet = new HashSet<>();
    private final Set<String> sortedDestinationsSet = new TreeSet<>();
    private final PriorityQueue<Package> priorityDispatchQueue = new PriorityQueue<>(new DeliveryPriorityComparator());
    private final Map<String, java.util.List<Package>> packagesByDestinationMap = new HashMap<>();
    private final Map<String, DeliveryPerson> deliveryPersonMap = new HashMap<>();

    //Lista generica propia
    private final GenericLinkedList<Package> deliveryHistory = new GenericLinkedList<>();

    //Registra un paquete
    public boolean registerPackage(Package pkg) {

        if (packageByCodeMap.containsKey(pkg.getCode())) {
            return false;
        }

        packageByCodeMap.put(pkg.getCode(), pkg);
        arrivalQueue.offer(pkg);
        uniqueDestinationsSet.add(pkg.getDestination());
        sortedDestinationsSet.add(pkg.getDestination());
        priorityDispatchQueue.offer(pkg);

        String destinationPkg = pkg.getDestination();
        List<Package> pkgList = packagesByDestinationMap.get(destinationPkg);

        if (pkgList == null) {
            pkgList = new ArrayList<>();
            packagesByDestinationMap.put(destinationPkg, pkgList);
        }

        pkgList.add(pkg);

        return true;
    }

    //intenta registrar un repartidor y retorna un boolean
    public boolean registerDeliveryPerson(DeliveryPerson person) {

        if (deliveryPersonMap.containsKey(person.getId())) {
            return false;
        }
        deliveryPersonMap.put(person.getId(), person);
        return true;
    }

    //Encuentra un paquete, complejidad O(1)
    public Package findPackageByCode(String code) {
        return packageByCodeMap.get(code);
    }

    //Despacha el siguiente paquete utilizando FIFO
    public Package dispatchNextFIFO() {

        Package pkg = arrivalQueue.poll();
        if (pkg != null) {
            priorityDispatchQueue.remove(pkg);
            registerDelivery(pkg);
        }
        return pkg;
    }

    //Despacha el siguiente paquete con prioridad
    public Package dispatchNextPriority() {
        Package pkg = priorityDispatchQueue.poll();
        if (pkg != null) {
            arrivalQueue.remove(pkg);
            registerDelivery(pkg);
        }
        return pkg;
    }

    //Metodo privado auxiliar
    private void registerDelivery(Package pkg) {
        deliveryHistory.add(pkg);
    }

    //Valida que un paquete se pueda asignar a un repartidor
    public boolean assignPackageToDeliveryPerson(String packageCode, String deliveryPersonId) {

        Package pkg = findPackageByCode(packageCode);
        DeliveryPerson person = deliveryPersonMap.get(deliveryPersonId);

        if (pkg == null || person == null) {
            return false;
        }

        if (!person.isAvailable()) {
            return false;
        }

        if (!person.getZone().equalsIgnoreCase(pkg.getDestination())) {
            return false;
        }

        person.setAvailable(false);
        return true;
    }

    // -------------------------------------------------------------
    // METODOS RECURSIVOS
    // -------------------------------------------------------------

    //Retorna el peso total para un destino determinado
    public double calculateTotalWeightRecursive(List<Package> packageList, String destination, int index) {

        //Caso base
        if (index >= packageList.size()) {
            return 0.0;
        }

        Package current = packageList.get(index);
        double currentWeight = current.getDestination().equalsIgnoreCase(destination) ? current.getWeight() : 0.0;

        return currentWeight + calculateTotalWeightRecursive(packageList, destination, index + 1);
    }

    //Retorna cuantos paquetes tienen una prioridad determinada
    public int countPackagesWithMinPriority(List<Package> packageList, int minPriority, int index) {
        // Caso base
        if (index == packageList.size()) {
            return 0;
        }

        Package current = packageList.get(index);

        if (current.getPriority() >= minPriority) {
            return 1 + countPackagesWithMinPriority(packageList, minPriority, index + 1);
        } else {
            return countPackagesWithMinPriority(packageList, minPriority, index + 1);
        }
    }

    //Divide y venceras, retorna el paquete con mayor peso
    public Package findHeaviestPackageDivideAndConquer(List<Package> packageList, int start, int end) {

        //Validaciones
        if (packageList == null || packageList.isEmpty() || start > end) {
            return null;
        }

        //Caso base
        if (start == end) {
            return packageList.get(start);
        }

        int mid = start + (end - start) / 2;

        Package leftMax = findHeaviestPackageDivideAndConquer(packageList, start, mid);
        Package rightMax = findHeaviestPackageDivideAndConquer(packageList, mid + 1, end);

        if (leftMax == null) return rightMax;
        if (rightMax == null) return leftMax;

        return (leftMax.getWeight() >= rightMax.getWeight()) ? leftMax : rightMax;
    }

    // D. Binary Search: Find package by code on a sorted list
    public int binarySearchByCode(List<Package> sortedList, String targetCode) {
        int low = 0;
        int high = sortedList.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = sortedList.get(mid).getCode().compareTo(targetCode);

            if (comparison == 0) {
                return mid; //Encontrado
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1; //No fué encontrado
    }

    // Getters para los reportes
    public Set<String> getUniqueDestinationsSet() {
        return Collections.unmodifiableSet(uniqueDestinationsSet);
    }

    public Set<String> getSortedDestinationsSet() {
        return Collections.unmodifiableSet(sortedDestinationsSet);
    }

    public Map<String, List<Package>> getPackagesByDestinationMap() {
        return Collections.unmodifiableMap(packagesByDestinationMap);
    }

    public GenericLinkedList<Package> getDeliveryHistory() {
        return deliveryHistory;
    }
}
