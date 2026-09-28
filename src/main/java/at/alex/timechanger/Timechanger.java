package at.alex.timechanger;

//? if fabric {
import net.fabricmc.api.ModInitializer;

public class Timechanger implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}
//?} elif neoforge {
/*import at.alex.timechanger.command.Command;
import at.alex.timechanger.config.gui.ConfigScreen;
/^? if >=26.3 {^//^import com.mojang.blaze3d.platform.InputConstants;
^//^?} else^/import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class Timechanger {
    static final Lazy<KeyMapping> OPEN_SETTINGS_KEYBIND = Lazy.of(() -> new KeyMapping(
            "key.timechanger.openconfig",
            /^? if >=26.3 {^//^InputConstants.KEY_N,
            ^//^?} else^/GLFW.GLFW_KEY_N,
            new KeyMapping.Category(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "name"))));

    public Timechanger(IEventBus eventBus) {
        CommonClass.init();

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, screen) -> new ConfigScreen(screen));
        ModLoadingContext.get().getActiveContainer().getEventBus().register(KeyMappingListener.class);
        NeoForge.EVENT_BUS.register(Timechanger.class);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (OPEN_SETTINGS_KEYBIND.get().consumeClick()) {
            /^? if >=26.2 {^//^Minecraft.getInstance().setScreenAndShow(new ConfigScreen(Component.empty()));
            ^//^?} else^/Minecraft.getInstance().setScreen(new ConfigScreen(Component.empty()));
        }
    }

    @SubscribeEvent
    public static void onClientCommandRegister(RegisterClientCommandsEvent event) {
        for (Command command : CommonClass.COMMAND_MANAGER.getCommands()) {
            command.register(event.getDispatcher());
        }
    }
}
*///?}
