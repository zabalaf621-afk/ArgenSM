package ar.argensmp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class JoinListener implements Listener {

    private final ArgenSMP plugin;

    public JoinListener(ArgenSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String nombre = player.getName();

        String global = plugin.getConfig().getString("bienvenida.mensaje-global", "");
        event.joinMessage(Msg.parse(global.replace("%jugador%", nombre)));

        String titulo = plugin.getConfig().getString("bienvenida.titulo", "");
        String subtitulo = plugin.getConfig().getString("bienvenida.subtitulo", "");
        player.showTitle(Title.title(
                Msg.parse(titulo.replace("%jugador%", nombre)),
                Msg.parse(subtitulo.replace("%jugador%", nombre))));

        for (String linea : plugin.getConfig().getStringList("bienvenida.chat")) {
            Component componente = Msg.parse(linea.replace("%jugador%", nombre));
            player.sendMessage(componente);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String salida = plugin.getConfig().getString("bienvenida.mensaje-salida", "");
        event.quitMessage(Msg.parse(salida.replace("%jugador%", event.getPlayer().getName())));
    }
}
