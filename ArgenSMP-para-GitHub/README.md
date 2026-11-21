# ArgenSMP

Plugin base para un servidor de Minecraft argentino (Paper 1.21.x, Java 21).

## Qué trae
- Bienvenida con título, mensajes de entrada y salida en argentino
- Chat con rangos: Pibe, Crack, Maestro, Leyenda (por permisos)
- `/mate`: efecto de velocidad con cooldown
- Scoreboard con jugador, online y hora argentina
- Avisos automáticos de eventos a horas fijas (hora de Argentina)
- `/argensmp reload` para recargar la config sin reiniciar

Todo se edita en `plugins/ArgenSMP/config.yml`.

## Cómo compilarlo (necesitás Java 21 y Maven)
1. Descomprimí la carpeta.
2. Abrí una terminal dentro de la carpeta `argensmp`.
3. Ejecutá: `mvn clean package`
4. El plugin queda en `target/ArgenSMP.jar`.

Si no querés instalar nada: abrí la carpeta en IntelliJ IDEA (Community, gratis) y usá Maven > Lifecycle > package.

## Cómo instalarlo
1. Subí `ArgenSMP.jar` a la carpeta `plugins` de tu servidor.
2. Reiniciá el servidor.
3. Para dar rangos: instalá LuckPerms y usá, por ejemplo,
   `/lp user NombreDelJugador permission set argensmp.rango.crack true`

## Si tu servidor no es 1.21.4
Cambiá la versión en `pom.xml` (`paper-api`) y el `api-version` en `plugin.yml`.
