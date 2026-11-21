package ar.argensmp;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ChatListener implements Listener {

    private final ArgenSMP plugin;

    public ChatListener(ArgenSMP plugin) {
        this.plugin = plugin;
    }

    /** Devuelve el prefijo del rango más alto que tenga el jugador (el orden lo define config.yml). */
    private String prefijoDe(Player player) {
        ConfigurationSection rangos = plugin.getConfig().getConfigurationSection("rangos");
        if (rangos == null) {
            return "";
        }
        for (String clave : rangos.getKeys(false)) {
            if (player.hasPermission("argensmp.rango." + clave)) {
                return rangos.getString(clave, "");
            }
        }
        return "";
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        final String prefijo = prefijoDe(event.getPlayer());

        event.renderer((fuente, nombreVisible, mensaje, audiencia) -> Msg.parse(prefijo)
                .append(Component.text(" "))
                .append(nombreVisible)
                .append(Component.text(": ", NamedTextColor.GRAY))
                .append(mensaje.colorIfAbsent(NamedTextColor.WHITE)));
    }
}
