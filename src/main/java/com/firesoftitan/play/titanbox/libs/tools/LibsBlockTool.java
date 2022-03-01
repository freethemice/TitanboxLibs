package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftBlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.firesoftitan.play.titanbox.libs.enums.InventoryTypeEnum;

public class LibsBlockTool {
    private Tools parent;

    public LibsBlockTool(Tools parent) {
        this.parent = parent;
    }

    public void fixSpawnerPlace(Block block, EntityType type)
    {
        CreatureSpawner cs = ((CreatureSpawner) block.getState());
        cs.setSpawnedType(type);
        cs.update(true, false);
    }

    /**
     * Check if the given block contains a vanilla inventory,
     *
     * @param target the block to check
     * @return true if this block holds a vanilla inventory; false otherwise
     */
    public InventoryTypeEnum isInventory(Block target)
    {
        return switch (target.getType()) {
            case CHEST -> InventoryTypeEnum.CHEST;
            case TRAPPED_CHEST -> InventoryTypeEnum.TRAPPED_CHEST;
            case DISPENSER -> InventoryTypeEnum.DISPENSER;
            case HOPPER -> InventoryTypeEnum.HOPPER;
            case DROPPER -> InventoryTypeEnum.DROPPER;
            case FURNACE -> InventoryTypeEnum.FURNACE;
            case BLAST_FURNACE -> InventoryTypeEnum.BLAST_FURNACE;
            case SMOKER -> InventoryTypeEnum.SMOKER;
            case BARREL -> InventoryTypeEnum.BARREL;
            case BREWING_STAND -> InventoryTypeEnum.BREWING_STAND;
            default -> InventoryTypeEnum.NONE;
        };
    }

    /**
     * Get the vanilla inventory for the given block.
     *
     * @param target the block containing the target inventory
     * @return the block's inventory, or null if the block does not have one
     */
    public Inventory getInventory(Block target) {
        Chest c;
        switch (target.getType()) {
            case TRAPPED_CHEST:
            case CHEST:
                c = (Chest) target.getState();
                if (c.getInventory().getHolder() instanceof DoubleChest dc) {
                    return dc.getInventory();
                } else {
                    return c.getBlockInventory();
                }
            case BARREL:
            case DISPENSER:
            case HOPPER:
            case DROPPER:
            case BLAST_FURNACE:
            case SMOKER:
            case FURNACE:
            case BREWING_STAND:
                //case BURNING_FURNACE:
                return ((InventoryHolder) target.getState()).getInventory();
            // any other vanilla inventory types ?
            default:
                return null;
        }
    }

    public net.minecraft.world.level.block.Block getServerBlock(Block block)
    {
        return ((CraftBlockState)block.getState()).getHandle().b();
    }


    public boolean isItemInInventory(Inventory inventory, ItemStack itemStack)
    {
        if (! Tools.tools.getItemStackTool().isEmpty(itemStack)) {
            for (int slot = 0; slot < inventory.getSize(); slot++) {
                ItemStack slotItemStack = inventory.getItem(slot);
                if (! Tools.tools.getItemStackTool().isEmpty(slotItemStack)) {
                    if( Tools.tools.getItemStackTool().isItemEqual(itemStack, slotItemStack))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
