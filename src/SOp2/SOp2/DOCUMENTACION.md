# Reporte Técnico: Simulador de Sistema de Archivos Concurrente (VFS)

Este documento detalla la arquitectura, lógica de negocio y componentes técnicos del simulador desarrollado para la asignatura de Sistemas Operativos. El sistema emula el comportamiento de un Sistema de Archivos real, gestionando hardware simulado, procesos concurrentes y algoritmos de optimización de disco.

---

## 1. Fundamentos y Restricciones Técnicas
El pilar fundamental del proyecto es la **autonomía estructural**. Siguiendo la restricción crítica, se ha evitado el uso de cualquier colección del framework de Java (`java.util.ArrayList`, `Vector`, `Queue`, etc.).

### Estructuras de Datos Propias
- **`CustomLinkedList<T>`**: Una lista doblemente enlazada genérica. Implementa la interfaz `Iterable`, permitiendo el uso de bucles `for-each`. Es la base para almacenar hijos de directorios, bloques de archivos y entradas de log.
- **`CustomQueue<T>`**: Una cola manual basada en la lista enlazada, utilizada para la gestión de estados de procesos y colas de solicitudes de E/S.
- **`Node<T>`**: La unidad básica de almacenamiento que permite la navegación bidireccional en las estructuras.

---

## 2. Core del Sistema (Hardware Simulado)
El sistema simula un **Disco de Almacenamiento (SD)** dividido en sectores físicos (bloques).

- **Asignación Encadenada (Linked Allocation)**: 
  - Los archivos no requieren bloques contiguos. 
  - Cada bloque (`Block`) contiene un puntero `nextBlockId`. 
  - El sistema rastrea el `startBlockId` y recorre la cadena para leer o escribir.
  - **Visualización**: Cada archivo genera un color RGB aleatorio único. Los bloques en el mapa del disco se pintan de este color para evidenciar visualmente la fragmentación y la asignación.
- **Gestión de Espacio**: Un array de booleanos rastrea la disponibilidad de bloques, permitiendo una búsqueda eficiente de espacio libre.

---

## 3. Jerarquía y Sistema de Roles
El simulador maneja una estructura de árbol para organizar los datos.

- **VDirectory**: Contenedor que puede albergar otros directorios o archivos.
- **VFile**: Almacena metadatos (dueño, permisos, tamaño en bloques, color) y la lista de bloques asignados.
- **Roles (Seguridad)**:
  - **Administrador**: Posee permisos totales (CRUD). Puede crear, modificar y eliminar cualquier recurso.
  - **Usuario**: Modo de solo lectura. La interfaz bloquea dinámicamente los botones de edición y deshabilita la manipulación del árbol de archivos.

---

## 4. Concurrencia y Planificación de Disco
Para simular un entorno multi-proceso, se implementaron mecanismos de control de acceso y optimización de hardware.

### LockManager (Sincronización)
Implementa el algoritmo de **Lectores-Escritores**:
- **Locks Compartidos**: Permiten múltiples lecturas simultáneas.
- **Locks Exclusivos**: Garantizan que solo un proceso escriba a la vez, evitando condiciones de carrera en el disco.

### Algoritmos de Planificación (Disk Scheduling)
El simulador permite comparar la eficiencia de 4 políticas de movimiento del cabezal:
1. **FIFO (First-In, First-Out)**: Orden de llegada simple.
2. **SSTF (Shortest Seek Time First)**: Minimiza el movimiento buscando la petición más cercana al cabezal actual.
3. **SCAN (Elevador)**: El cabezal recorre el disco en una dirección y luego cambia al llegar al final.
4. **C-SCAN (Circular SCAN)**: Proporciona un tiempo de espera más uniforme al volver al inicio tras cada recorrido.

---

## 5. Journaling y Tolerancia a Fallos
Se ha implementado un sistema de **Log de Transacciones** para prevenir la corrupción de datos.

- **Ciclo de Vida**: `PENDING` -> `COMMITTED` / `ABORTED`.
- **Recuperación (UNDO)**: Al presionar "Simular Crash", el sistema activa un protocolo de recuperación. Si una operación de creación se interrumpe (queda en estado PENDING), el sistema libera automáticamente los bloques huérfanos y limpia la jerarquía para devolver el sistema a un estado consistente.

---

## 6. Análisis Detallado del Código

### A. Lógica de Negocio (FileSystemManager)
Utiliza el patrón **Singleton** para asegurar que solo exista una instancia del sistema de archivos coordinando el disco y el Journal.
- **`createFile(String name, int numBlocks, VDirectory parent)`**: Este método orquestra la creación atómica. Primero registra en el Journal, adquiere un Lock exclusivo, asigna bloques en el `SimulatedDisk` (pintándolos del color del archivo) y finalmente actualiza la jerarquía. Si algo falla en la asignación de bloques, invoca un `abort()` en el Journal.
- **`deleteResource(FileSystemItem item, VDirectory parent)`**: Implementa **recursividad pura**. Si el item es un directorio, recorre sus hijos y llama a `deleteResource` para cada uno de ellos antes de eliminarse a sí mismo, asegurando que todos los bloques de todos los archivos contenidos sean liberados.

### B. Implementación de Algoritmos (SSTF, SCAN, C-SCAN)
Cada algoritmo implementa la interfaz `DiskScheduler`.
- **`SSTFScheduler`**: Utiliza un bucle que busca en la `CustomLinkedList` de peticiones aquella con la distancia absoluta mínima respecto a la posición actual del cabezal.
- **`SCANScheduler`**: Realiza un ordenamiento (Bubble Sort manual) de las peticiones. Divide la lista en dos grupos (superiores e inferiores al cabezal) y las recorre en sentido ascendente y luego descendente.

### C. Visualización Dinámica (DiskVisualizer)
Extiende de `JPanel` y sobreescribe `paintComponent(Graphics g)`.
- El código calcula una cuadrícula de 10x10.
- Itera sobre el array `blockColors` del `SimulatedDisk`.
- Utiliza `g.fillRect` con el color específico de cada bloque, permitiendo que el usuario vea la "fragmentación" de los archivos en tiempo real.

### D. Persistencia Manual (PersistenceManager)
Dado que no se permiten colecciones estándar, la serialización a JSON se realiza mediante un **recorrido recursivo del árbol**. 
- El método `serializeItem` construye manualmente la cadena JSON concatenando nombres, dueños y la lista de bloques de cada `VFile`, asegurando que el estado del disco pueda ser reconstruido fielmente.

---

## 7. Guía de Uso
- **Crear Archivo**: Requiere nombre y cantidad de bloques. Se asigna un color único.
- **Modo Admin**: Activa/Desactiva botones de edición.
- **Verificar Planificación**: Genera un reporte comparativo de eficiencia de los 4 algoritmos principales.
- **Simular Crash**: Demuestra la robustez del Journaling revirtiendo operaciones incompletas.

---
**Desarrollado como proyecto integral para la asignatura de Sistemas Operativos.**
