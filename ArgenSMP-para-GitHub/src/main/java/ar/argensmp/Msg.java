package ar.argensmp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/** Utilidad chica para convertir textos con formato MiniMessage (<gold>, <bold>, etc.). */
public final class Msg {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private Msg() {}

    public static Component parse(String texto) {
        if (texto == null) {
            return Component.empty();
        }
        return MM.deserialize(texto);
    }
}
