package meew0.slotrelay;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by miras on 11.01.15.
 */
public class TileEntitySlotRelay extends TileEntity {
    public int slotExtract, slotInsert;

    public TileEntitySlotRelay() {
        slotExtract = slotInsert = 0;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }


    private ItemStack addItemStacks(ItemStack stack1, ItemStack stack2) {
        ItemStack newStack = stack1.copy();
        newStack.stackSize += stack2.stackSize;
        return newStack;
    }

    @Override
    public void updateEntity() {
        try {
            SlotRelay.debug("update");
            // Get tile entity to extract from
            TileEntity extractTE = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);

            // Make sure it's an inventory
            if (extractTE instanceof IInventory) {
                SlotRelay.debug("extractTE instanceof IInventory");
                IInventory extractInventory = (IInventory) extractTE;

                // Get the stack and make sure it exists
                ItemStack stack = extractInventory.getStackInSlot(slotExtract);
                if (stack == null || stack.stackSize == 0) {
                    SlotRelay.debug("extractStack is empty");
                    return;
                }

                // Get tile entity to insert to
                TileEntity insertTE = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
                if (insertTE instanceof IInventory) {
                    SlotRelay.debug("insertTE instanceof IInventory");
                    IInventory insertInventory = (IInventory) insertTE;

                    ItemStack stackInInsert = insertInventory.getStackInSlot(slotInsert);

                    // Make sure the stack is valid for the output slot (or the check is disabled in the config)
                    if (insertInventory.isItemValidForSlot(slotInsert, stack) || SlotRelay.ignoreStackValidity) {
                        SlotRelay.debug("item valid for slot");
                        if (stackInInsert == null || stackInInsert.stackSize == 0) {
                            SlotRelay.debug("insertStack is empty");
                            // If the stack to insert to doesn't exist, just put the stack to extract from into it...
                            insertInventory.setInventorySlotContents(slotInsert, stack);

                            // ...and delete the items in the extract stack
                            extractInventory.setInventorySlotContents(slotExtract, null);
                        } else if (stack.isItemEqual(stackInInsert)) {
                            SlotRelay.debug("items are equal");
                            // If the items are equal, then make sure the stack to insert to isn't full
                            if (stackInInsert.stackSize < stackInInsert.getMaxStackSize()) {
                                SlotRelay.debug("insertStack isn't full");
                                // If the stack to insert to isn't full, then...
                                if (stackInInsert.stackSize + stack.stackSize < stackInInsert.getMaxStackSize()) {
                                    SlotRelay.debug("both together aren't full");
                                    // If both stacks together wouldn't make the stack full, then just
                                    // add the two stacks...
                                    insertInventory.setInventorySlotContents(slotInsert,
                                            addItemStacks(stack, stackInInsert));

                                    // ...and delete the stack in the extract slot
                                    extractInventory.setInventorySlotContents(slotExtract, null);
                                } else {
                                    SlotRelay.debug("both together are full, complicated logic ahead");
                                    // Otherwise remove some items from the extract slot and add them to the insert stack
                                    insertInventory.setInventorySlotContents(slotInsert, addItemStacks(stackInInsert,
                                            extractInventory.decrStackSize(slotExtract, stackInInsert.getMaxStackSize() -
                                                    stackInInsert.stackSize)));
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            SlotRelay.log.error("An error has been encountered while processing item transfer! Ignoring");
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        slotExtract = nbt.getInteger("slotExtract");
        slotInsert = nbt.getInteger("slotInsert");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("slotExtract", slotExtract);
        nbt.setInteger("slotInsert", slotInsert);
    }
}
