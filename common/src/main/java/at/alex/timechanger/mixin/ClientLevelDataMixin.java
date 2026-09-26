package at.alex.timechanger.mixin;

import at.alex.timechanger.CommonClass;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin {
    @ModifyReturnValue(method = "getDayTime", at = @At("RETURN"))
    private long getDayTime(long original) {
        return CommonClass.CONFIG.timeEnabled ? CommonClass.CONFIG.time : original;
    }
}
