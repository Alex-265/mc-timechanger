package at.alex.timechanger.mixin;

import at.alex.timechanger.CommonClass;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.ClientClockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientClockManager.class)
public class ClientClockManagerMixin {
    @ModifyReturnValue(method = "getTotalTicks", at = @At("RETURN"))
    private long getTotalTicks(long original) {
        return CommonClass.CONFIG.timeEnabled ? CommonClass.CONFIG.time : original;
    }
}
