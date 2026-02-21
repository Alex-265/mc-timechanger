package at.alex.timechanger;

import net.fabricmc.api.ModInitializer;

public class Timechanger implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}
