package at.alex.timechanger;

import at.alex.timechanger.config.gui.ConfigScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class Timechanger {
    static final Lazy<KeyMapping> OPEN_SETTINGS_KEYBIND = Lazy.of(() -> new KeyMapping("key.timechanger.openconfig", GLFW.GLFW_KEY_N, new KeyMapping.Category(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "name"))));


    public Timechanger(IEventBus eventBus) {
        CommonClass.init();

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, screen) -> new ConfigScreen(screen));
        ModLoadingContext.get().getActiveContainer().getEventBus().register(KeyMappingListener.class);
        NeoForge.EVENT_BUS.register(Timechanger.class);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (OPEN_SETTINGS_KEYBIND.get().consumeClick()) {
            Minecraft.getInstance().setScreen(new ConfigScreen(Component.empty()));
        }
    }
}
