# Reto 8 · Persistencia utilizando SQLite

Aplicación Android en Java para gestionar un directorio de empresas de desarrollo de software. Guarda los datos localmente mediante `SQLiteOpenHelper`, sin conexión a Internet.

## Funcionalidades

- Crear, listar, consultar, editar y eliminar empresas.
- Nombre, URL, teléfono, email, productos y servicios y una o varias clasificaciones: consultoría, desarrollo a la medida y fábrica de software.
- Filtros combinados por nombre y clasificación.
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

Cada entrega utiliza una etiqueta Git `vMAJOR.MINOR.PATCH`, un registro en `CHANGELOG.md` y `versionName`/`versionCode` en `app/build.gradle`. Las correcciones incrementan PATCH, las funcionalidades MINOR y los cambios incompatibles MAJOR. La versión de la base de datos se incrementa solo al cambiar el esquema, con migraciones que preserven los datos.
