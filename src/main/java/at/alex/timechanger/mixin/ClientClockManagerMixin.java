package at.alex.timechanger.mixin;

import at.alex.timechanger.CommonClass;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=26.3 {

/*import net.minecraft.client.ClientClockManager;
@Mixin(net.minecraft.client.ClientClockManager.ClientClockInstance.class)
public class ClientClockManagerMixin {
    @ModifyReturnValue(method = "totalTicks", at = @At("RETURN"))
    private long getTotalTicks(long original) {
        return CommonClass.CONFIG.timeEnabled ? CommonClass.CONFIG.time : original;
    }
}

*///?} else if >=26.1 {

import net.minecraft.client.ClientClockManager;
@Mixin(net.minecraft.client.ClientClockManager.class)
public class ClientClockManagerMixin {
    @ModifyReturnValue(method = "getTotalTicks", at = @At("RETURN"))
    private long getTotalTicks(long original) {
        return CommonClass.CONFIG.timeEnabled ? CommonClass.CONFIG.time : original;
    }
}
//?} else {

/*import net.minecraft.client.multiplayer.ClientLevel;
@Mixin(ClientLevel.ClientLevelData.class)
public class ClientClockManagerMixin {
    @ModifyReturnValue(method = "getDayTime", at = @At("RETURN"))
    private long getTotalTicks(long original) {
        return CommonClass.CONFIG.timeEnabled ? CommonClass.CONFIG.time : original;
    }
}

*///?}
