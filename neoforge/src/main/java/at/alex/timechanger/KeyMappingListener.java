package at.alex.timechanger;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import static at.alex.timechanger.Timechanger.OPEN_SETTINGS_KEYBIND;

public class KeyMappingListener {
    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_SETTINGS_KEYBIND.get());
    }
}
