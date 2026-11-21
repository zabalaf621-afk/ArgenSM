package ar.argensmp;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MateCommand implements CommandExecutor {

    private final ArgenSMP plugin;
    private final Map<UUID, Long> ultimoUso = new HashMap<>();

    public MateCommand(ArgenSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando es solo para jugadores.");
            return true;
        }

        long cooldownMs = plugin.getConfig().getLong("mate.cooldown-segundos", 120) * 1000L;
        long ahora = System.currentTimeMillis();
        Long anterior = ultimoUso.get(player.getUniqueId());

        if (anterior != null && ahora - anterior < cooldownMs) {
            long faltan = (cooldownMs - (ahora - anterior)) / 1000L + 1;
            String texto = plugin.getConfig().getString("mate.mensaje-espera",
                    "<red>El agua todavía está caliente, esperá %segundos%s.");
            player.sendMessage(Msg.parse(texto.replace("%segundos%", String.valueOf(faltan))));
            return true;
        }

        ultimoUso.put(player.getUniqueId(), ahora);

        int duracionTicks = plugin.getConfig().getInt("mate.duracion-segundos", 30) * 20;
        int nivel = plugin.getConfig().getInt("mate.nivel-velocidad", 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duracionTicks, nivel));
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.0f);

        player.sendMessage(Msg.parse(plugin.getConfig().getString("mate.mensaje",
                "<green>🧉 Te cebaste un mate. ¡Ahora sí, a darle!")));
        return true;
    }
}
