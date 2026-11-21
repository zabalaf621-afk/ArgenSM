package ar.argensmp;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/** Avisa a todo el server a horas fijas (hora argentina por defecto). */
public final class EventScheduler {

    private final ArgenSMP plugin;
    private BukkitTask tarea;
    private String ultimaClave = "";

    public EventScheduler(ArgenSMP plugin) {
        this.plugin = plugin;
    }

    public void start() {
        stop();
        tarea = Bukkit.getScheduler().runTaskTimer(plugin, this::revisar, 20L, 20L);
    }

    public void stop() {
        if (tarea != null) {
            tarea.cancel();
            tarea = null;
        }
    }

    public void reload() {
        ultimaClave = "";
        start();
    }

    private ZoneId zona() {
        try {
            return ZoneId.of(plugin.getConfig().getString("zona-horaria", "America/Argentina/Buenos_Aires"));
        } catch (Exception e) {
            return ZoneId.of("America/Argentina/Buenos_Aires");
        }
    }

    private void revisar() {
        ZonedDateTime ahora = ZonedDateTime.now(zona());
        String hora = ahora.format(DateTimeFormatter.ofPattern("HH:mm"));
        String clave = ahora.toLocalDate() + " " + hora;

        if (clave.equals(ultimaClave)) {
            return;
        }

        List<Map<?, ?>> eventos = plugin.getConfig().getMapList("eventos");
        for (Map<?, ?> evento : eventos) {
            Object horaEvento = evento.get("hora");
            Object mensaje = evento.get("mensaje");
            if (horaEvento != null && mensaje != null && hora.equals(horaEvento.toString())) {
                Bukkit.broadcast(Msg.parse(mensaje.toString()));
            }
        }
        ultimaClave = clave;
    }
}
