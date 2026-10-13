# QuindíoExpress — Sistema de Gestión de Operaciones y Centro de Distribución

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)]()
[![Course](https://img.shields.io/badge/Estructuras%20de%20Datos-2026--2-green.svg)]()

Repositorio Parcial 1 desarrollado para la asignatura **Estructuras de Datos** (Universidad del Quindío). La aplicación optimiza la recepción, clasificación, consulta, priorización, asignación para despacho y registro de entregas de paquetes en el departamento del Quindío.

---

## Tabla de Contenidos
- [Descripción del Problema](#-descripción-del-problema)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Estructuras de Datos Utilizadas](#-estructuras-de-datos-utilizadas)
- [Estructura Propia: Lista Simplemente Enlazada](#-estructura-propia-lista-simplemente-enlazada)
- [Algoritmos e Hitos Implementados](#-algoritmos-e-hitos-implementados)
- [Instrucciones de Ejecución](#-instrucciones-de-ejecución)
- [Autores](#-autores)

---

## Descripción del Problema
La empresa **QuindíoExpress** recibe diariamente decenas de paquetes con destino a diversos municipios del Quindío. El sistema automatiza las siguientes operaciones:
1. **Registro y validación:** Garantiza códigos de paquetes e identificaciones de repartidores únicos.
2. **Atención en orden de llegada (FIFO):** Procesamiento respetando estrictamente el orden de registro.
3. **Despacho prioritario:** Gestión de prioridades mediante ordenamiento compuesto (Prioridad $\downarrow$, Tiempo Estimado $\uparrow$, Código alfabético $\uparrow$).
4. **Consultas eficientes:** Búsqueda inmediata de paquetes por su código en tiempo constante $O(1)$.
5. **Historial de entregas:** Almacenamiento en una estructura propia enlazada genérica.
6. **Análisis de carga y reportes:** Métodos recursivos y de Divide y Vencerás para análisis operacional.

---

## Arquitectura del Proyecto

```text
src/
 └── com/
      └── uniquindio/
           └──      edu/
                     ├── model/
                     │    ├── Package.java          # POJO con Comparable<Package>
                     │    ├── DeliveryPerson.java       # POJO Entidad Repartidor
                     │    └── DeliveryPriorityComparator.java # Compara paquetes en base a lo requerido en el documento
                     ├── genericlist/
                     │    ├── CustomList.java       # Interfaz Genérica
                     │    ├── GenericNode.java             # Nodo de la lista enlazada genérico
                     │    └── GenericLinkedList.java # Lista Enlazada Genérica + Iterable
                     ├── service/
                     │    ├── DistributionCenterService.java # Lógica del negocio, cumpliendo los parámetros propuestos a lo largo del documento
                     └── Main.java                  # Datos de prueba (30 paquetes) y demostración
```

---

## Estructuras de Datos Utilizadas

| Necesidad Operativa | Colección Elegida | Operación Principal | Complejidad Esperada |
| :--- | :--- | :--- | :--- |
| **1. Registro FIFO** | `ArrayDeque<Paquete>` | `offer()` / `poll()` | $O(1)$ |
| **2. Búsqueda por código** | `HashMap<String, Paquete>` | `get(code)` | $O(1)$ promedio |
| **3. Conjunto de municipios** | `HashSet<String>` | `add(town)` | $O(1)$ promedio |
| **4. Municipios ordenados** | `TreeSet<String>` | `add(town)` | $O(\log N)$ |
| **5. Despacho Prioritario** | `PriorityQueue<Paquete>` | `offer()` / `poll()` | $O(\log N)$ |
| **6. Historial de entregas** | `GenericLinkedList<T>` | `add(element)` | $O(1)$ |
| **7. Agrupación por destino** | `HashMap<String, List<Paquete>>` | `get(town)` | $O(1)$ promedio |

---

## Estructura Propia: Lista Simplemente Enlazada

La clase `GenericLinkedList<T>` cumple con las siguientes características requeridas por el enunciado:
- **Totalmente genérica:** No depende de la clase `Paquete`.
- **Implementa `CustomList<T>` e `Iterable<T>`:** Permite recorridos mediante ciclos `for-each` usando un `Iterator<T>` personalizado.
- **Sin colecciones integradas de Java:** No utiliza `ArrayList`, `LinkedList` ni arreglos auxiliares internamente.

---

## Algoritmos e Hitos Implementados

1. **Suma Recursiva de Peso por Municipio:** Recorrido recursivo sumando paquetes cuyo destino coincida ($O(N)$).
2. **Conteo Recursivo por Prioridad Mínima:** Filtrado recursivo de paquetes con prioridad superior a un valor ($O(N)$).
3. **Paquete de Mayor Peso (Divide y Vencerás):** División binaria del conjunto y combinación del elemento máximo ($O(N)$).
4. **Búsqueda Binaria por Código:** Búsqueda $O(\log N)$ sobre lista ordenada de paquetes.

---

## Instrucciones de Ejecución

Para compilar y ejecutar el proyecto desde la terminal sin IDE:

```bash
# 1. Clonar el repositorio
git clone https://github.com/carlosMolina007/quindio-express.git
cd quindio-express

# 2. Compilar el código fuente
javac -d bin src/com/uniquindio/edu/*/*.java

# 3. Ejecutar la aplicación principal
java -cp bin com.uniquindio.edu.Main
```

## Autores
- **Angel David Diaz Pinilla** - *Estudiante Ingeniería de Sistemas*
- **Carlos Andres Molina Leal** - *Estudiante Ingeniería de Sistemas*

*Universidad del Quindío — Facultad de Ingeniería — 2026-2*