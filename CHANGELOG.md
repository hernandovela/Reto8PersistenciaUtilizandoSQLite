# Historial de versiones

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
