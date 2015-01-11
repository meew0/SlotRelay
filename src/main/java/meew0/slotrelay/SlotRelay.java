package meew0.slotrelay;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

@Mod(modid = SlotRelay.MODID, version = SlotRelay.VERSION, name = SlotRelay.NAME)
public class SlotRelay
{
    public static final String MODID = "slotrelay";
    public static final String VERSION = "1.0.2";
    public static final String NAME = "Slot Relay";

    public static Configuration config;
    public static boolean ignoreStackValidity;

    public static Logger log;

    public static Block slotRelay;

    public static void debug(String d) {
        //log.info(d);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();

        config = new Configuration(event.getSuggestedConfigurationFile());
        ignoreStackValidity = config.getBoolean("ignoreStackValidity", "slotRelay", false,
                "Ignore if the stack is actually valid for the insertion slot (e.g. furnace fuel). " +
                        "May cause problems with some mods. Be advised that mods can also do the " +
                        "checks themselves if something is inserted into the slot.");

        slotRelay = new BlockSlotRelay();

        GameRegistry.registerBlock(slotRelay, "slotRelay");

        GameRegistry.addShapedRecipe(new ItemStack(slotRelay), "OHO", "ORO", "OHO", 'O', Blocks.obsidian,
                'H', Blocks.hopper, 'R', Items.redstone);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
