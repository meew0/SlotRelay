package meew0.slotrelay;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * Created by miras on 11.01.15.
 */
public class MaterialSlotRelay extends Material {
    public MaterialSlotRelay() {
        super(MapColor.blueColor);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
