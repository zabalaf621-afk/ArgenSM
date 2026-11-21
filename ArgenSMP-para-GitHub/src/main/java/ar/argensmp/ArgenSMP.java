package ar.argensmp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArgenSMP extends JavaPlugin {

    private ScoreboardManager scoreboardManager;
    private EventScheduler eventScheduler;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        if (getCommand("mate") != null) {
            getCommand("mate").setExecutor(new MateCommand(this));
        }
        if (getCommand("argensmp") != null) {
            getCommand("argensmp").setExecutor(this);
        }

        scoreboardManager = new ScoreboardManager(this);
        scoreboardManager.start();

        eventScheduler = new EventScheduler(this);
        eventScheduler.start();

        getLogger().info("ArgenSMP activado. ¡Vamos Argentina!");
    }

    @Override
    public void onDisable() {
        if (scoreboardManager != null) {
            scoreboardManager.stop();
        }
        if (eventScheduler != null) {
            eventScheduler.stop();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("argensmp.admin")) {
                sender.sendMessage(Msg.parse("<red>No tenés permiso para eso, che."));
                return true;
            }
            reloadConfig();
            scoreboardManager.reload();
            eventScheduler.reload();
            sender.sendMessage(Msg.parse("<green>Configuración de ArgenSMP recargada."));
            return true;
        }
        sender.sendMessage(Msg.parse("<yellow>Uso: /argensmp reload"));
        return true;
    }
}
