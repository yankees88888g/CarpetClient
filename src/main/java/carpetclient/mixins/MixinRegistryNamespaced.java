package carpetclient.mixins;

import java.util.Map;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistrySimple;
import carpetclient.mixinInterface.AMixinRegistryNamespaced;

@Mixin(RegistryNamespaced.class)
public abstract class MixinRegistryNamespaced<K, V> extends RegistrySimple<K, V> implements AMixinRegistryNamespaced {

    @Shadow
    @Final
    protected IntIdentityHashBiMap<V> underlyingIntegerMap;

    @Shadow
    @Final
    protected Map<V, K> inverseObjectRegistry;

    public void clear() {
        underlyingIntegerMap.clear();
        inverseObjectRegistry.clear();
        registryObjects.clear();
    }
}
