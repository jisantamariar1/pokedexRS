# Pasos para configurar la Pokédex correctamente

## Configuración de Lombok (para las anotaciones `@Getter`, `@Setter`, etc.)

1. **Abrir el Menú Principal**:
    - Ve al menú principal y selecciona `File > Settings`.
    - También puedes usar el atajo de teclado `Ctrl + Alt + S`.

2. **Acceder a la configuración del compilador**:
    - En la sección de configuración, busca y selecciona `Build, Execution, Deployment`.
    - Luego, elige la opción `Compiler`.

3. **Habilitar el procesamiento de anotaciones**:
    - En el menú del lado izquierdo, selecciona `Annotation Processors`.
    - Verás una lista con el proyecto y tres opciones:
      - `Default`
      - `Annotation profile for pokedexRoseroSantamaria`
      - `pokedexRS`

4. **Configurar las opciones para cada perfil**:
    - Haz clic en `Default`. En el lado derecho, asegúrate de marcar las casillas:
      - `Enable annotation processing`
      - `Obtain processors from project classpath`
    - Repite este proceso para `Annotation profile for pokedexRoseroSantamaria`.

5. **Finalización**:
    - Una vez hecho esto, Lombok estará configurado correctamente.

---

## Configuración de la librería JavaFX

1. **Descargar JavaFX**:
    - Ve a [https://openjfx.io/](https://openjfx.io/) y descarga la versión `javafx-sdk-23.0.2`.

2. **Preparar la librería**:
    - Descomprime el archivo descargado.
    - Mueve la carpeta descomprimida dentro de tu proyecto.

3. **Agregar JavaFX al proyecto**:
    - Abre el Menú Principal y selecciona `File > Project Structure`.
    - También puedes usar el atajo de teclado `Ctrl + Alt + Shift + S`.

4. **Configurar la librería**:
    - En la ventana de `Project Structure`, selecciona la opción `Libraries` en el menú lateral.
    - Haz clic en el botón `+` ubicado en el lado derecho.
    - Navega hasta la carpeta de JavaFX que descomprimiste. IntelliJ te da la facilidad de ir a la ruta de tu proyecto con el icono de una carpeta.
    - Busca la ruta: `javafx-sdk-23.0.2/lib`.
    - Selecciona la carpeta `lib` y haz clic en `Aceptar`.

5. **Ejemplo de ruta**:
    - Así debería verse la dirección configurada:
      ```
      C:\Users\usuario\Downloads\Proyecto_prototipo\pokedexRS\javafx-sdk-23.0.2\lib.
      ```

---

## Consideraciones finales

1. **Cargar los datos en la base de datos**:
    - La primera vez que ejecutes el programa, los datos se cargarán automáticamente en la base de datos.
    
2. **Evitar recargar los datos en ejecuciones posteriores**:
    - Comenta la siguiente línea en el método `CommandLineRunner`:
      ```java
      // pokemonApiClient.fetch(pokemonInfo);
      ```
