package com.uniquindio.edu;

import com.uniquindio.edu.model.DeliveryPerson;
import com.uniquindio.edu.model.Package;
import com.uniquindio.edu.service.DistributionCenterService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        DistributionCenterService service = new DistributionCenterService();

        System.out.println("=================================================");
        System.out.println("  QUINDÍOEXPRESS - CENTRO DE DISTRIBUCIÓN        ");
        System.out.println("=================================================\n");

        //Registro de los 6 repartidores mínimo
        System.out.println(">>> 1. Registrando repartidores...");
        service.registerDeliveryPerson(new DeliveryPerson("DP01", "Carlos Gomez", "Armenia", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP02", "Ana Martinez", "Salento", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP03", "Luis Rodriguez", "Calarcá", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP04", "Sofia Lopez", "Circasia", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP05", "Jorge Grajales", "Montenegro", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP06", "Maria Fernandez", "Armenia", true));
        System.out.println("✓ Repartidores registrados correctamente.\n");

        //Registro de los 30 paquetes como mínimo
        System.out.println(">>> 2. Registrando 30 paquetes de la jornada...");
        List<Package> packageList = createSamplePackages();
        for (Package pkg : packageList) {
            service.registerPackage(pkg);
        }
        System.out.println("✓ Total paquetes registrados: " + packageList.size() + "\n");

        //Consulta rápida por código con HashMap O(1)
        System.out.println(">>> 3. Consulta rápida por código: ");
        String searchCode = "PQ731";
        Package found = service.findPackageByCode(searchCode);
        System.out.println("   Búsqueda de '" + searchCode + "': " + (found != null ? found : "No encontrado"));

        //Municipíos ordenados por el alfabeto y sin duplicar
        System.out.println("\n>>> 4. Reporte de Municipios Destino:");
        System.out.println("   - Sin duplicados [HashSet]: " + service.getUniqueDestinationsSet());
        System.out.println("   - Orden alfabético [TreeSet]: " + service.getSortedDestinationsSet());

        //Agrupación de paquetes por destino
        System.out.println("\n>>> 5. Carga de paquetes por destino [HashMap<String, List<Package>>]:");
        service.getPackagesByDestinationMap().forEach((destination, list) -> {
            System.out.println("   * " + destination + " (" + list.size() + " paquetes)");
        });

        //Metodos recursivos
        System.out.println("\n>>> 6. Métodos Recursivos:");
        double totalArmeniaWeight = service.calculateTotalWeightRecursive(packageList, "Armenia", 0);
        System.out.printf("   - Peso total para Armenia: %.2f kg\n", totalArmeniaWeight);

        int minPriority = 4;
        int countHighPriority = service.countPackagesWithMinPriority(packageList, minPriority, 0);
        System.out.println("   - Paquetes con prioridad >= " + minPriority + ": " + countHighPriority);

        //Divide y venceras para buscar el paquete más pesado
        System.out.println("\n>>> 7. Divide y Vencerás:");
        Package heaviest = service.findHeaviestPackageDivideAndConquer(packageList, 0, packageList.size() - 1);
        System.out.println("   - Paquete de mayor peso: " + heaviest);

        //Busqueda binaria O(log N)
        System.out.println("\n>>> 8. Búsqueda Binaria sobre lista ordenada por Código:");
        List<Package> sortedByCodeList = new ArrayList<>(packageList);
        Collections.sort(sortedByCodeList);

        String binaryTarget = "PQ942";
        int index = service.binarySearchByCode(sortedByCodeList, binaryTarget);
        if (index != -1) {
            System.out.println("   - Paquete '" + binaryTarget + "' encontrado en índice " + index + ": " + sortedByCodeList.get(index));
        } else {
            System.out.println("   - Paquete '" + binaryTarget + "' no encontrado.");
        }

        //Asignando paquete a repartidor
        System.out.println("\n>>> 9. Asignación de Paquete a Repartidor:");
        boolean assigned = service.assignPackageToDeliveryPerson("PQ731", "DP01");
        System.out.println("   - Asignación PQ731 a repartidor DP01 (Armenia): " + (assigned ? "Éxito" : "Falló"));

        //Despacho por prioridad y evidencia del uso de nuestra propia lista genérica
        System.out.println("\n>>> 10. Despachando paquetes por Prioridad ( PriorityQueue ):");
        System.out.println("   1° Despachado: " + service.dispatchNextPriority());
        System.out.println("   2° Despachado: " + service.dispatchNextPriority());
        System.out.println("   3° Despachado: " + service.dispatchNextPriority());

        System.out.println("\n>>> 11. Historial de Entregas [GenericLinkedList PROPIA]:");
        System.out.println("   Recorriendo historial con for-each (probando Iterator/Iterable)...");
        int count = 1;
        for (Package deliveredPkg : service.getDeliveryHistory()) {
            System.out.println("   " + count++ + ". Entregado -> " + deliveredPkg);
        }

        System.out.println("\n=================================================");
        System.out.println("  EJECUCIÓN COMPLETADA EXITOSAMENTE             ");
        System.out.println("=================================================");
    }

    //Datos precargados, 30 paquetes
    private static List<Package> createSamplePackages() {
        List<Package> list = new ArrayList<>();
        list.add(new Package("PQ731", "Armenia", 4.5, 5, 45));
        list.add(new Package("PQ105", "Salento", 3.0, 5, 25));
        list.add(new Package("PQ942", "Armenia", 7.2, 4, 20));
        list.add(new Package("PQ318", "Calarcá", 1.5, 2, 15));
        list.add(new Package("PQ567", "Salento", 5.0, 3, 30));
        list.add(new Package("PQ829", "Armenia", 2.3, 1, 40));
        list.add(new Package("PQ101", "Circasia", 8.1, 4, 35));
        list.add(new Package("PQ204", "Montenegro", 6.0, 5, 50));
        list.add(new Package("PQ309", "Calarcá", 2.0, 2, 20));
        list.add(new Package("PQ415", "Armenia", 12.0, 3, 25));

        list.add(new Package("PQ512", "Salento", 4.2, 4, 15));
        list.add(new Package("PQ618", "Circasia", 3.5, 1, 60));
        list.add(new Package("PQ723", "Montenegro", 9.4, 5, 10));
        list.add(new Package("PQ834", "Armenia", 1.1, 2, 18));
        list.add(new Package("PQ945", "Calarcá", 5.5, 3, 30));
        list.add(new Package("PQ111", "Salento", 2.8, 5, 22));
        list.add(new Package("PQ222", "Circasia", 11.2, 4, 40));
        list.add(new Package("PQ333", "Montenegro", 3.9, 2, 25));
        list.add(new Package("PQ444", "Armenia", 6.7, 3, 35));
        list.add(new Package("PQ555", "Calarcá", 4.0, 1, 50));

        list.add(new Package("PQ666", "Salento", 1.8, 5, 12));
        list.add(new Package("PQ777", "Circasia", 7.5, 4, 28));
        list.add(new Package("PQ888", "Montenegro", 10.0, 2, 45));
        list.add(new Package("PQ999", "Armenia", 2.6, 3, 19));
        list.add(new Package("PQ123", "Calarcá", 8.3, 5, 33));
        list.add(new Package("PQ234", "Salento", 3.1, 1, 27));
        list.add(new Package("PQ345", "Circasia", 5.8, 4, 21));
        list.add(new Package("PQ456", "Montenegro", 1.9, 2, 16));
        list.add(new Package("PQ568", "Armenia", 6.1, 3, 38));
        list.add(new Package("PQ679", "Calarcá", 4.7, 5, 29));

        return list;
    }
}