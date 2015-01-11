package meew0.slotrelay;

import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by meew0 on 11.01.15.
 */
public class BlockSlotRelay extends BlockContainer {
    IIcon topBottom, sideDown;

    public BlockSlotRelay() {
        super(new MaterialSlotRelay());

        setBlockName("slotRelay");
        setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySlotRelay();
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerBlockIcons(iconRegister);

        topBottom = iconRegister.registerIcon("slotrelay:slotRelayTopBottom");
        sideDown = iconRegister.registerIcon("slotrelay:slotRelaySideDown");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if((side & 1) == side) { // top or bottom
            return topBottom;
        }
        else return sideDown;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {
        return (side & 1) == side; // top or bottom
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
                                    int side, float dx, float dy, float dz) {
        TileEntitySlotRelay te = (TileEntitySlotRelay) world.getTileEntity(x, y, z);

        if(te == null) {
            SlotRelay.log.warn("TE is null!");
            return false;
        }

        if(side == 0) {
            te.slotInsert = Math.abs(te.slotInsert + ((player.isSneaking()) ? -1 : 1));
            if(!player.worldObj.isRemote)
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal(
                        "msg.insertSlotChanged.name").replace("%n", "" + te.slotInsert)));
        } else {
            te.slotExtract = Math.abs(te.slotExtract + ((player.isSneaking()) ? -1 : 1));
            if(!player.worldObj.isRemote)
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal(
                        "msg.extractSlotChanged.name").replace("%n", "" + te.slotExtract)));
        }
        return true;
    }
}
