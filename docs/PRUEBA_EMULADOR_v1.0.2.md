# Pruebas en el emulador · actualizado a v1.3.1

Última actualización: **14 de octubre de 2026**. Se mantiene el nombre de este archivo para conservar el enlace original. La app instalada es **1.3.1 (versionCode 8)**; esta revisión documental se identifica en Git como `docs-v1.3.1-r1`. El informe original v1.0.2 se conserva más abajo como antecedente.

## Estado actual y mejoras verificadas

| Entrega | Ajuste | Evidencia de validación |
| --- | --- | --- |
| v1.0.3 | Tres clasificaciones visibles y selección múltiple en el formulario | [Requisitos y prueba de CRUD de interfaz](REQUISITOS_v1.0.3.md) |
| v1.1.0 | Listado compacto, Ver detalles, Ver todas las empresas y búsqueda en cualquier campo sin distinguir tildes o mayúsculas | [Listado y búsqueda](LISTADO_BUSQUEDA_v1.1.0.md) |
| v1.2.0 | Logo, icono, colores, contador y tecla Buscar que oculta el teclado | [Identidad visual](IDENTIDAD_v1.2.0.md) |
| v1.3.0 | Detalles separados por campo, acciones en una fila y numeración de las empresas | [Detalle y numeración](DETALLE_v1.3.0.md) |
| v1.3.1 | Barra lateral persistente, arrastre proporcional, pulsación del carril y retorno al inicio al filtrar | [Desplazamiento y capturas de ambos extremos](DESPLAZAMIENTO_v1.3.1.md) |

La numeración 01, 02… representa la posición en el listado filtrado; no es el identificador `_id` de SQLite. La barra lateral pertenece a la app y permanece visible. La barra flotante con micrófono, lupa y emoji pertenece al teclado de Android: se ocultó mediante Buscar; puede reaparecer al escribir y no supone un defecto del directorio.

### Captura actual de la aplicación

Captura nueva del emulador `emulator-5554`, después de detener y reabrir el proceso para obtener una copia consistente de SQLite. El listado muestra **11 empresas**, logo, búsqueda global, numeración y barra lateral, sin la barra flotante del teclado.

![Aplicación v1.3.1 con 11 empresas y barra de desplazamiento](evidencias/sqlite-v1.3.1/app-actual.png)

### Pruebas de la versión vigente

La validación previa de v1.3.1 finalizó con compilación y lint sin errores y **cinco pruebas instrumentadas aprobadas**, según la [salida conservada del ejecutor](evidencias/v1.3.1/pruebas.txt). Cubre CRUD y persistencia, filtros, búsqueda global, CRUD de interfaz con cancelación y confirmación de eliminación, y desplazamiento hasta ambos extremos con reducción posterior del contenido. El [informe de desplazamiento](DESPLAZAMIENTO_v1.3.1.md) detalla el recorrido visual de inicio a final y regreso.

En esta actualización documental se verificaron nuevamente la versión instalada, la lectura de SQLite, la integridad de la copia y el listado tras reabrir la app. No se recompiló ni se repitió la suite instrumentada, porque no se modificó código. Se conservan las limitaciones de un único emulador y las advertencias no bloqueantes de lint.

## SQLite con los datos actuales

Origen: `com.hernandovela.reto8/databases/companies.db`, tabla `companies`, esquema versión 1. Se extrajo una copia con `adb exec-out run-as` después de detener la app y se consultó localmente en modo de solo lectura. La app se volvió a abrir sin borrar ni modificar registros.

La copia contiene **11 registros: diez DEMO y Capgemini**. Los valores reproducen el estado actual, incluidas las ediciones hechas después de la carga inicial; no se sustituyeron por los datos originales. Son datos usados en pruebas y no deben interpretarse como un directorio de contactos comerciales verificados.

| Verificación | Resultado |
| --- | --- |
| `PRAGMA integrity_check` | `ok` |
| Total de empresas | 11 |
| Pertenecen a Consultoría (`categories & 1`) | 5 |
| Pertenecen a Desarrollo a la medida (`categories & 2`) | 7 |
| Pertenecen a Fábrica de software (`categories & 4`) | 6 |

Los conteos por clasificación se superponen: una empresa puede pertenecer a varias. `categories` guarda una máscara: 1 = consultoría, 2 = desarrollo a la medida, 4 = fábrica; por ejemplo, 7 incluye las tres.

### Empresas y clasificaciones

Las siguientes imágenes son **tablas generadas a partir de consultas reales sobre la copia de SQLite**, no capturas del inspector de Android Studio. En conjunto muestran los siete campos almacenados de los once registros.

![Consulta SQLite: identificadores, empresas y clasificaciones](evidencias/sqlite-v1.3.1/01-empresas-clasificacion.png)

### Páginas web, teléfonos y correos

![Consulta SQLite: datos de contacto](evidencias/sqlite-v1.3.1/02-contactos.png)

### Productos y servicios

![Consulta SQLite: productos y servicios](evidencias/sqlite-v1.3.1/03-servicios.png)

Se adjuntan los [registros consultados en CSV](evidencias/sqlite-v1.3.1/registros.csv), las [consultas y definición de la tabla](evidencias/sqlite-v1.3.1/consulta.sql) y la [verificación con huella SHA-256 de la copia](evidencias/sqlite-v1.3.1/verificacion.json). La copia binaria de la base no se incorpora al repositorio ni se incluye como precarga del APK.

Para reproducir las consultas desde Database Inspector, abrir la app de depuración en el emulador, conectar su proceso, seleccionar `companies.db` y ejecutar:

```sql
SELECT _id, name, website, phone, email, services, categories
FROM companies
ORDER BY name COLLATE NOCASE, _id;

SELECT COUNT(*) AS total,
       SUM((categories & 1) != 0) AS consultoria,
       SUM((categories & 2) != 0) AS desarrollo,
       SUM((categories & 4) != 0) AS fabrica
FROM companies;
```

Los resultados cambiarán si se crean, editan o eliminan empresas después de esta captura.

---

# Antecedente histórico: prueba de la entrega v1.0.2

**Las capturas, cantidades y limitaciones de esta sección corresponden a la entrega original del 13 de octubre, no al estado actual.** La confirmación de eliminación que entonces quedó pendiente fue cubierta posteriormente por las pruebas de interfaz de v1.0.3 y entregas siguientes.

## Entorno y alcance

Prueba realizada el 13 de octubre de 2026, zona horaria America/Bogota, en el emulador Android de la PC: `Small_Phone`, identificador `emulator-5554`, Android 17 (API 37). El recorrido visual se realizó sobre el APK v1.0.1 y se entrega como v1.0.2 con esta evidencia; la lógica funcional y el esquema de SQLite se conservan. El reloj de la barra del emulador puede diferir del reloj local de la PC.

Se controló la ventana del emulador mediante la habilidad Computer Use: clics, escritura, desplazamiento y lectura de pantalla. Para comprobar persistencia se reinició el proceso con Android Debug Bridge, sin borrar datos. Las imágenes contienen la ventana del emulador; la barra flotante del teclado de Android aparece en algunas capturas y no forma parte de la aplicación.

## Datos ficticios

| Campo | Valor |
| --- | --- |
| Nombre inicial | Empresa QA Reto8 |
| Nombre después de editar | Empresa QA Actualizada |
| URL | https://example.com |
| Teléfono ficticio | 3001234567 |
| Email | qa@example.com |
| Productos y servicios | Aplicaciones Android y consultoria de software |
| Clasificaciones | Consultoría y Desarrollo a la medida |

## Casos ejecutados y resultados observados

| Caso | Acción | Resultado observado | Estado |
| --- | --- | --- | --- |
| UI-01 | Abrir Nueva empresa y pulsar Guardar con campos vacíos | El formulario permanece abierto, marca errores y solicita al menos una clasificación | Aprobado |
| UI-02 | Completar los cinco campos, seleccionar dos clasificaciones y guardar | Aparece una empresa con los datos ingresados, ambas clasificaciones y mensaje Empresa guardada | Aprobado |
| UI-03 | Buscar QA y filtrar Fábrica de software | No aparecen empresas; mensaje de lista vacía | Aprobado |
| UI-04 | Mantener QA y seleccionar Desarrollo a la medida | Aparece la empresa con sus datos; ambos filtros se aplican juntos | Aprobado |
| UI-05 | Editar el nombre a Empresa QA Actualizada y guardar | Se actualiza la tarjeta existente y no se duplica el registro | Aprobado |
| UI-06 | Pulsar Eliminar | Aparece el diálogo con el nombre de la empresa y botones Cancelar y Eliminar | Aprobado |
| UI-07 | Pulsar Cancelar en el diálogo | La empresa permanece en el listado | Aprobado |
| UI-08 | Detener completamente el proceso y volver a abrir la aplicación | Se conserva el nombre editado, URL, teléfono, email, servicios y ambas clasificaciones; aparece una empresa | Aprobado |
| UI-09 | Editar el email a correo-invalido y pulsar Guardar | El formulario rechaza el valor con Email inválido; al cancelar, permanece qa@example.com | Aprobado |

La confirmación final de eliminación no se pulsó mediante control de Windows. Se solicitó autorización por la política de la habilidad Computer Use, pero no se recibió durante el recorrido. La eliminación de registros sí se verificó mediante la prueba instrumentada `testCrudAndPersistence`. La limpieza posterior del ejecutor de pruebas desinstaló la aplicación y su registro ficticio; esa limpieza no constituye una prueba del botón Eliminar. Al finalizar se reinstaló y abrió v1.0.2, con el directorio vacío.

## Evidencias

### Campos obligatorios y clasificación

![Formulario vacío rechazado](evidencias/01-validacion.png)

### Filtros combinados sin coincidencias

![QA y Fábrica de software sin resultados](evidencias/03-filtro-sin-coincidencias.png)

### Filtros combinados con coincidencia

![QA y Desarrollo a la medida con una empresa](evidencias/04-filtro-combinado.png)

### Edición guardada

![Nombre actualizado en el listado](evidencias/05-empresa-editada.png)

### Confirmación antes de eliminar

![Diálogo de eliminación con el nombre de la empresa](evidencias/06-confirmacion-eliminacion.png)

### Persistencia tras reiniciar el proceso

![Datos conservados después de reiniciar la aplicación](evidencias/07-persistencia-reinicio.png)

### Email inválido rechazado

![Error al intentar guardar un email inválido](evidencias/08-email-invalido.png)

### Entrega v1.0.2 reinstalada

![Aplicación reinstalada después de las pruebas automatizadas](evidencias/09-entrega-instalada.png)

## Validación automatizada de la entrega

`gradlew.bat assembleDebug lintDebug connectedDebugAndroidTest` finalizó con `BUILD SUCCESSFUL`. El reporte [JUnit original](evidencias/junit-v1.0.2.xml) registra dos pruebas, cero fallos, cero errores y cero pruebas omitidas. Su marca de tiempo usa UTC: `2026-10-14T00:49:08`, equivalente al 13 de octubre de 2026 a las 19:49:08 en Colombia.

| Prueba | Resultado |
| --- | --- |
| testCrudAndPersistence | Aprobada |
| testCombinedAndLiteralFilters | Aprobada |

Lint finalizó sin errores; conserva advertencias de SDK objetivo 36 y configuración de extracción de datos. La verificación de GitHub Actions es independiente de estos resultados locales.

## Reproducción

1. Instalar el APK y abrir Directorio de empresas.
2. Ejecutar las acciones UI-01 a UI-09 de la tabla con los datos ficticios.
3. Para repetir la comprobación de persistencia, ejecutar los siguientes comandos con el emulador conectado.

```powershell
adb shell am force-stop com.hernandovela.reto8
adb shell am start -n com.hernandovela.reto8/.MainActivity
```

## Límites de esta evidencia

El recorrido cubre un emulador, una empresa y dos clasificaciones. La cancelación de eliminación se verificó visualmente. Las pruebas instrumentadas complementan el recorrido con creación, consulta, actualización, eliminación y persistencia al reabrir SQLite, además de búsquedas literales y parametrizadas; usan una base de prueba independiente. No se declara una validación en dispositivos físicos ni una revisión de todos los tamaños de pantalla.
