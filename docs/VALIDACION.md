# Validación de v1.0.1

Verificada con el JDK integrado de Android Studio y el emulador Small_Phone (Android API 37) de la PC.

`gradlew.bat assembleDebug lintDebug connectedDebugAndroidTest`: resultado correcto. Dos pruebas instrumentadas aprobadas: CRUD y persistencia al reabrir SQLite; filtros combinados por nombre y categoría, clasificación múltiple, búsqueda literal de `%` y `_` y parámetros SQL.

Lint no presenta errores. Conserva avisos de SDK objetivo 36 y configuración de transferencia de datos. La aplicación está en español y usa algunos métodos de Android marcados como obsoletos, disponibles en las versiones soportadas.

## Recorrido manual

1. Crear una empresa con todos los campos y dos clasificaciones; comprobar su aparición en el listado.
2. Buscar una parte del nombre y seleccionar cada clasificación; comprobar la combinación de filtros.
3. Editar datos y volver a abrir la aplicación; comprobar que se conservan.
4. Pulsar Eliminar y Cancelar; comprobar que la empresa permanece. Repetir y confirmar la eliminación.
5. Intentar guardar campos vacíos, URL o email inválidos y ninguna clasificación; comprobar los mensajes del formulario.

Las pruebas usan `companies-test.db`; el directorio del usuario usa `companies.db`. GitHub Actions compila, ejecuta lint y prepara el APK de pruebas; las pruebas instrumentadas se ejecutaron localmente en el emulador.
