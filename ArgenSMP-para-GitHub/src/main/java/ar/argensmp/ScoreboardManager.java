package ar.argensmp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ScoreboardManager {

    private final ArgenSMP plugin;
    private final Map<UUID, Scoreboard> tableros = new HashMap<>();
    private BukkitTask tarea;

    public ScoreboardManager(ArgenSMP plugin) {
        this.plugin = plugin;
    }

    public void start() {
        stop();
        if (!plugin.getConfig().getBoolean("scoreboard.activado", true)) {
            return;
        }
        tarea = Bukkit.getScheduler().runTaskTimer(plugin, this::actualizarTodos, 20L, 20L);
    }

    public void stop() {
        if (tarea != null) {
            tarea.cancel();
            tarea = null;
        }
    }

    public void reload() {
        tableros.clear();
        start();
    }

    private void actualizarTodos() {
        // Limpia tableros de jugadores que ya se fueron.
        tableros.keySet().removeIf(id -> Bukkit.getPlayer(id) == null);

        for (Player jugador : Bukkit.getOnlinePlayers()) {
            actualizar(jugador);
        }
    }

    private ZoneId zona() {
        try {
            return ZoneId.of(plugin.getConfig().getString("zona-horaria", "America/Argentina/Buenos_Aires"));
        } catch (Exception e) {
            return ZoneId.of("America/Argentina/Buenos_Aires");
        }
    }

    private void actualizar(Player jugador) {
        List<String> lineas = plugin.getConfig().getStringList("scoreboard.lineas");
        if (lineas.isEmpty()) {
            return;
        }

        Scoreboard tablero = tableros.get(jugador.getUniqueId());
        if (tablero == null) {
            tablero = crear(lineas.size());
            tableros.put(jugador.getUniqueId(), tablero);
            jugador.setScoreboard(tablero);
        }

        String hora = ZonedDateTime.now(zona()).format(DateTimeFormatter.ofPattern("HH:mm"));

        for (int i = 0; i < lineas.size(); i++) {
            Team equipo = tablero.getTeam("linea" + i);
            if (equipo == null) {
                continue;
            }
            String texto = lineas.get(i)
                    .replace("%jugador%", jugador.getName())
                    .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("%hora%", hora);
            equipo.prefix(Msg.parse(texto));
        }
    }

    private Scoreboard crear(int cantidadLineas) {
        Scoreboard tablero = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objetivo = tablero.registerNewObjective(
                "argensmp", "dummy", Msg.parse(plugin.getConfig().getString("scoreboard.titulo", "<aqua>ArgenSMP")));
        objetivo.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < cantidadLineas; i++) {
            // Cada línea usa una entrada invisible única (código de color) y el texto va en el prefijo del equipo.
            String entrada = "§" + Integer.toHexString(i) + "§r";
            Team equipo = tablero.registerNewTeam("linea" + i);
            equipo.addEntry(entrada);
            objetivo.getScore(entrada).setScore(cantidadLineas - i);
        }
        return tablero;
    }
}
