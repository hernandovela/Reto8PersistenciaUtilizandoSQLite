# Reto 8 · Persistencia utilizando SQLite

Aplicación Android en Java para gestionar un directorio de empresas de desarrollo de software. Guarda los datos localmente mediante `SQLiteOpenHelper`, sin conexión a Internet.

## Funcionalidades

- Crear, listar, consultar, editar y eliminar empresas.
- Nombre, URL, teléfono, email, productos y servicios y una o varias clasificaciones: consultoría, desarrollo a la medida y fábrica de software.
- Listado compacto para explorar empresas sin conocer su nombre, con Ver detalles y Ver todas las empresas.
- Búsqueda en todos los campos, sin distinguir mayúsculas ni tildes, combinada con el filtro de clasificación.
- Confirmación antes de eliminar y validación de campos obligatorios.
- Datos persistentes al cerrar y volver a abrir la aplicación.

## Abrir en Android Studio

Abre esta carpeta como proyecto y sincroniza Gradle. Usa el JDK integrado de Android Studio (Java 17 o superior), Android SDK 36 y un dispositivo Android 8.0 (API 26) o superior. `local.properties` contiene la ruta del SDK de cada equipo y no se publica.

```powershell
.\gradlew.bat assembleDebug lintDebug
.\gradlew.bat connectedDebugAndroidTest
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`. Las pruebas instrumentadas requieren un emulador o dispositivo conectado.

## Versiones

La entrega actual `v1.3.1` corrige el [desplazamiento con una barra siempre visible](docs/DESPLAZAMIENTO_v1.3.1.md). Se conservan los informes de [detalle y numeración v1.3.0](docs/DETALLE_v1.3.0.md), [identidad visual v1.2.0](docs/IDENTIDAD_v1.2.0.md), [listado y búsqueda global v1.1.0](docs/LISTADO_BUSQUEDA_v1.1.0.md), [requisitos v1.0.3](docs/REQUISITOS_v1.0.3.md) y [pruebas v1.0.2](docs/PRUEBA_EMULADOR_v1.0.2.md).

Cada entrega utiliza una etiqueta Git `vMAJOR.MINOR.PATCH`, un registro en `CHANGELOG.md` y `versionName`/`versionCode` en `app/build.gradle`. Las correcciones incrementan PATCH, las funcionalidades MINOR y los cambios incompatibles MAJOR. La versión de la base de datos se incrementa solo al cambiar el esquema, con migraciones que preserven los datos.
