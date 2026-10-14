# Historial de versiones

## Documentación v1.3.1-r2 · 2026-10-14

- Sustituye la sección de evidencias antiguas del informe principal por dos capturas nuevas del formulario v1.3.1: validación y campos inferiores, con las tres clasificaciones visibles.
- Traslada el informe original v1.0.2 a un archivo histórico separado, con enlaces de ida y vuelta.
- Prueba visual sin guardar datos: se conservan las once empresas. Etiqueta docs-v1.3.1-r2; APK sin cambios.

## Documentación v1.3.1-r1 · 2026-10-14

- Actualización del enlace histórico PRUEBA_EMULADOR_v1.0.2.md con las mejoras y evidencias hasta la app 1.3.1.
- Captura actual del emulador y tres imágenes de consultas SQLite con los once registros actuales; CSV, SQL y verificación de integridad adjuntos.
- Se conserva el informe original y se distingue la barra del teclado de la barra de desplazamiento de la app.
- Revisión identificada por la etiqueta Git docs-v1.3.1-r1; sin cambios del APK, versionCode ni esquema SQLite.

## 1.3.1

- Barra lateral persistente con carril visible e indicador arrastrable, sincronizado con la altura real del listado.
- Navegación pulsando el carril, arrastrando el indicador o usando teclado y acciones de accesibilidad.
- Recalcula el recorrido al expandir detalles o cambiar el tamaño del contenido; permanece visible en gris si no hay desplazamiento disponible.
- Las búsquedas y cambios de clasificación vuelven al inicio de los resultados.
- Cinco pruebas instrumentadas aprobadas, incluida navegación en ambos sentidos y reducción del contenido.

## 1.3.0

- Detalle de empresa organizado en bloques independientes con etiqueta y valor para página web, teléfono, correo y productos/servicios.
- Mayor ancho útil de las tarjetas y tipografía ajustada para web y correo; datos seleccionables para copiar.
- Numeración visible 01, 02, 03… según la posición en el listado filtrado; no modifica identificadores SQLite.
- Acciones Editar y Eliminar en una fila, con color diferenciado para la eliminación.
- Compilación y lint aprobados, cuatro pruebas instrumentadas aprobadas y revisión visual en el emulador.

## 1.2.0

- Logo vectorial original de dos edificios en el encabezado e icono adaptativo de inicio.
- Paleta azul oscuro y verde, acción principal destacada, tarjetas redondeadas y barra de estado con mejor contraste.
- Datos de contacto seleccionables para copiar, tecla Buscar para ocultar el teclado y mensajes diferenciados para directorio vacío y búsquedas sin coincidencias.
- Contador con singular y plural correctos.
- Compilación, lint y cuatro pruebas instrumentadas aprobadas; diez empresas DEMO conservadas.

## 1.1.0

- Directorio compacto al abrir la app, con nombre, clasificación y opción Ver detalles para cada empresa.
- Botón Ver todas las empresas que limpia la búsqueda y la clasificación, oculta el teclado y vuelve al inicio del listado.
- Búsqueda por nombre, web, teléfono, email, productos y servicios y clasificación, sin distinguir mayúsculas ni tildes.
- Cuatro pruebas instrumentadas aprobadas; actualización instalada conservando las diez empresas DEMO.

## 1.0.3

- Corrección de visibilidad: las tres clasificaciones permanecen fuera del área desplazable de los campos del formulario, tanto al crear como al editar.
- Prueba instrumentada de interfaz para visibilidad completa, selección de las tres clasificaciones, creación, consulta, edición y eliminación con cancelación y confirmación.
- Matriz de cumplimiento de requisitos y capturas del emulador integrado en Android Studio.
- Pruebas ejecutadas mediante ADB sin desinstalar la aplicación. SQLite conserva el esquema versión 1.

## 1.0.2

- Prueba visual de la aplicación en el emulador de la PC con datos ficticios.
- Informe reproducible y capturas de validación, filtros combinados, edición, confirmación de eliminación y persistencia tras reiniciar el proceso.
- Incremento de versión de entrega; se conserva el esquema de SQLite.

## 1.0.1

- Corrección del filtro de todas las clasificaciones mediante conversión explícita del parámetro SQLite.
- Pruebas instrumentadas AndroidX con base de datos aislada de los datos del usuario.
- Verificación de compilación y lint en GitHub Actions; APK como artefacto descargable.
- Recurso de texto para el contador de empresas.

## 1.0.0

- Directorio SQLite con CRUD, filtros combinados, clasificación múltiple y confirmación de eliminación.
- Proyecto Android Studio con Gradle Wrapper y validación del formulario.
