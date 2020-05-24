package carpetclient.core;

import java.util.Map;
import javax.annotation.Nullable;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(-10000)
@IFMLLoadingPlugin.TransformerExclusions("carpetclient.core.CarpetClientCore")
public class CarpetClientCore implements IFMLLoadingPlugin
{
    private static boolean initialized = false;
    private static boolean loadCore = true;

    public CarpetClientCore()
    {
        initialize();

        MixinBootstrap.init();

        if (loadCore)
        {
            Mixins.addConfiguration("mixins.carpetclient.json");
            Mixins.addConfiguration("mixins.carpetclient.optifinecompat.json");
        }
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

    public static void initialize()
    {
        if (initialized)
        {
            return;
        }

        // TODO add a config option to disable the core mod?

        initialized = true;
    }
}
