package com.uniquindio.edu;

import com.uniquindio.edu.model.DeliveryPerson;
import com.uniquindio.edu.model.Package;
import com.uniquindio.edu.service.DistributionCenterService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DistributionCenterService service = new DistributionCenterService();

    // Lista local con los paquetes registrados (los métodos recursivos y la búsqueda binaria la necesitan)
    private static final List<Package> registeredPackages = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  QUINDÍOEXPRESS - CENTRO DE DISTRIBUCIÓN        ");
        System.out.println("=================================================");

        int option;
        do {
            printMenu();
            option = readInt("Seleccione una opción: ");
            System.out.println();

            switch (option) {
                case 1 -> loadSampleData();
                case 2 -> registerDeliveryPersonOption();
                case 3 -> registerPackageOption();
                case 4 -> findPackageOption();
                case 5 -> destinationsReport();
                case 6 -> packagesByDestinationReport();
                case 7 -> totalWeightOption();
                case 8 -> countPriorityOption();
                case 9 -> heaviestPackageOption();
                case 10 -> binarySearchOption();
                case 11 -> assignPackageOption();
                case 12 -> dispatchFifoOption();
                case 13 -> dispatchPriorityOption();
                case 14 -> showHistoryOption();
                case 0 -> System.out.println("Cerrando el sistema. ¡Hasta pronto!");
                default -> System.out.println("Opción inválida, intente de nuevo.");
            }
        } while (option != 0);

        scanner.close();
    }

    // ------------------------------------------------------------
    // MENÚ
    // ------------------------------------------------------------

    private static void printMenu() {
        System.out.println("\n================== MENÚ PRINCIPAL ==================");
        System.out.println(" 1.  Cargar datos de ejemplo (6 repartidores y 30 paquetes)");
        System.out.println(" 2.  Registrar repartidor");
        System.out.println(" 3.  Registrar paquete");
        System.out.println(" 4.  Buscar paquete por código [HashMap O(1)]");
        System.out.println(" 5.  Reporte de municipios [HashSet / TreeSet]");
        System.out.println(" 6.  Carga de paquetes por destino");
        System.out.println(" 7.  Peso total por destino [Recursivo]");
        System.out.println(" 8.  Contar paquetes con prioridad mínima [Recursivo]");
        System.out.println(" 9.  Paquete más pesado [Divide y Vencerás]");
        System.out.println(" 10. Buscar por código [Búsqueda Binaria]");
        System.out.println(" 11. Asignar paquete a repartidor");
        System.out.println(" 12. Despachar siguiente paquete [FIFO]");
        System.out.println(" 13. Despachar siguiente paquete [Prioridad]");
        System.out.println(" 14. Historial de entregas [GenericLinkedList]");
        System.out.println(" 0.  Salir");
        System.out.println("====================================================");
    }

    // ------------------------------------------------------------
    // OPCIONES
    // ------------------------------------------------------------

    private static void loadSampleData() {
        service.registerDeliveryPerson(new DeliveryPerson("DP01", "Carlos Gomez", "Armenia", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP02", "Ana Martinez", "Salento", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP03", "Luis Rodriguez", "Calarcá", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP04", "Sofia Lopez", "Circasia", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP05", "Jorge Grajales", "Montenegro", true));
        service.registerDeliveryPerson(new DeliveryPerson("DP06", "Maria Fernandez", "Armenia", true));

        int added = 0;
        for (Package pkg : createSamplePackages()) {
            if (service.registerPackage(pkg)) {
                registeredPackages.add(pkg);
                added++;
            }
        }
        System.out.println("✓ Repartidores cargados y " + added + " paquetes nuevos registrados.");
        if (added == 0) {
            System.out.println("  (Los datos de ejemplo ya estaban cargados.)");
        }
    }

    private static void registerDeliveryPersonOption() {
        String id = readLine("ID del repartidor: ");
        String name = readLine("Nombre: ");
        String zone = readLine("Zona (municipio): ");

        boolean ok = service.registerDeliveryPerson(new DeliveryPerson(id, name, zone, true));
        System.out.println(ok ? "✓ Repartidor registrado." : "✗ Ya existe un repartidor con ese ID.");
    }

    private static void registerPackageOption() {
        String code = readLine("Código del paquete: ");
        String destination = readLine("Destino: ");
        double weight = readDouble("Peso (kg): ");
        int priority = readInt("Prioridad (1 a 5): ");
        int extra = readInt("Último dato numérico del paquete (el 5.º parámetro del constructor): ");

        Package pkg = new Package(code, destination, weight, priority, extra);
        if (service.registerPackage(pkg)) {
            registeredPackages.add(pkg);
            System.out.println("✓ Paquete registrado.");
        } else {
            System.out.println("✗ Ya existe un paquete con ese código.");
        }
    }

    private static void findPackageOption() {
        String code = readLine("Código a buscar: ");
        Package found = service.findPackageByCode(code);
        System.out.println(found != null ? "Encontrado: " + found : "No encontrado.");
    }

    private static void destinationsReport() {
        System.out.println("Sin duplicados [HashSet]: " + service.getUniqueDestinationsSet());
        System.out.println("Orden alfabético [TreeSet]: " + service.getSortedDestinationsSet());
    }

    private static void packagesByDestinationReport() {
        if (service.getPackagesByDestinationMap().isEmpty()) {
            System.out.println("No hay paquetes registrados.");
            return;
        }
        service.getPackagesByDestinationMap().forEach((destination, list) ->
                System.out.println("* " + destination + " (" + list.size() + " paquetes)"));
    }

    private static void totalWeightOption() {
        if (noPackages()) return;
        String destination = readLine("Destino: ");
        double total = service.calculateTotalWeightRecursive(registeredPackages, destination, 0);
        System.out.printf("Peso total para %s: %.2f kg%n", destination, total);
    }

    private static void countPriorityOption() {
        if (noPackages()) return;
        int minPriority = readInt("Prioridad mínima: ");
        int count = service.countPackagesWithMinPriority(registeredPackages, minPriority, 0);
        System.out.println("Paquetes con prioridad >= " + minPriority + ": " + count);
    }

    private static void heaviestPackageOption() {
        if (noPackages()) return;
        Package heaviest = service.findHeaviestPackageDivideAndConquer(
                registeredPackages, 0, registeredPackages.size() - 1);
        System.out.println("Paquete de mayor peso: " + heaviest);
    }

    private static void binarySearchOption() {
        if (noPackages()) return;
        String target = readLine("Código a buscar: ");

        // La búsqueda binaria exige la lista ordenada por código
        List<Package> sortedByCode = new ArrayList<>(registeredPackages);
        Collections.sort(sortedByCode);

        int index = service.binarySearchByCode(sortedByCode, target);
        if (index != -1) {
            System.out.println("Encontrado en el índice " + index + ": " + sortedByCode.get(index));
        } else {
            System.out.println("Paquete '" + target + "' no encontrado.");
        }
    }

    private static void assignPackageOption() {
        String code = readLine("Código del paquete: ");
        String personId = readLine("ID del repartidor: ");
        boolean ok = service.assignPackageToDeliveryPerson(code, personId);
        System.out.println(ok ? "✓ Asignación exitosa."
                : "✗ Falló: paquete o repartidor inexistente, repartidor no disponible o zona distinta al destino.");
    }

    private static void dispatchFifoOption() {
        Package pkg = service.dispatchNextFIFO();
        System.out.println(pkg != null ? "Despachado (FIFO): " + pkg : "No hay paquetes en cola.");
    }

    private static void dispatchPriorityOption() {
        Package pkg = service.dispatchNextPriority();
        System.out.println(pkg != null ? "Despachado (prioridad): " + pkg : "No hay paquetes en cola.");
    }

    private static void showHistoryOption() {
        int count = 1;
        for (Package delivered : service.getDeliveryHistory()) {
            System.out.println(count++ + ". Entregado -> " + delivered);
        }
        if (count == 1) {
            System.out.println("El historial está vacío.");
        }
    }

    // ------------------------------------------------------------
    // UTILIDADES DE ENTRADA
    // ------------------------------------------------------------

    private static boolean noPackages() {
        if (registeredPackages.isEmpty()) {
            System.out.println("No hay paquetes registrados. Use la opción 1 o la 3 primero.");
            return true;
        }
        return false;
    }

    private static String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    // ------------------------------------------------------------
    // DATOS PRECARGADOS (30 paquetes)
    // ------------------------------------------------------------

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