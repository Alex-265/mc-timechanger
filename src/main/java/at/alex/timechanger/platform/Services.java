package at.alex.timechanger.platform;

import at.alex.timechanger.platform.services.IPlatformHelper;

public class Services {
    public static final IPlatformHelper PLATFORM =
            /*? if fabric {*/ new FabricPlatformHelper();
            /*?} elif neoforge */ //new NeoForgePlatformHelper();

    private Services() {
    }
}
