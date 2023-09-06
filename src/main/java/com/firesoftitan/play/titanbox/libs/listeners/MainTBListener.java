package com.firesoftitan.play.titanbox.libs.listeners;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.blocks.TitanBlock;
import com.firesoftitan.play.titanbox.libs.events.TitanBlockEvent;
import com.firesoftitan.play.titanbox.libs.events.TitanBlockInteractEvent;
import com.firesoftitan.play.titanbox.libs.managers.TitanBlockManager;
import com.firesoftitan.play.titanbox.libs.tools.LibsProtectionTool;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import static com.firesoftitan.play.titanbox.libs.TitanBoxLibs.instants;

public class MainTBListener implements Listener {
    public MainTBListener() {
        PluginManager pm = instants.getServer().getPluginManager();
        pm.registerEvents(this, instants);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onPlayerInteractEvent(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();
            if (clickedBlock != null) {
                Location location = clickedBlock.getLocation();
                if (TitanBlockManager.isTitanBlock(location)) {
                    if (LibsProtectionTool.canPlaceBlock(player, location)) {
                        TitanBlock titanBlock = TitanBlockManager.getTitanBlock(location);
                        if (titanBlock != null) {
                            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (!player.isSneaking() || TitanBoxLibs.tools.getItemStackTool().isEmpty(player.getInventory().getItemInMainHand()))
                                    || (event.getAction() == Action.LEFT_CLICK_BLOCK && !titanBlock.isBreakBlockAllowed())) {
                                TitanBlockListener titanBlockListener = TitanBlockManager.getTitanBlockListener(titanBlock);
                                if (titanBlockListener != null) {
                                    EquipmentSlot hand = event.getHand();
                                    if (hand == null) hand = EquipmentSlot.HAND;
                                    titanBlockListener.onPlayerInteract(new TitanBlockInteractEvent(player, location, titanBlock, event.getAction(), player.getInventory().getItem(hand), event.getBlockFace(), hand));
                                }
                                event.setCancelled(true);
                                ItemStack itemStack = titanBlock.getItemStack();
                                if (!TitanBoxLibs.tools.getItemStackTool().isEmpty(itemStack)) {
                                    if (itemStack.getType().isBlock()) {
                                        try {
                                            if (clickedBlock.getBlockData() instanceof Directional directional) {
                                                BlockFace facing = directional.getFacing();
                                                updateBlock(itemStack, clickedBlock);
                                                directional.setFacing(facing);
                                                clickedBlock.setBlockData(directional);
                                            } else updateBlock(itemStack, clickedBlock);
                                        } catch (Exception ignored) {

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

    private static void updateBlock(ItemStack itemStack, Block clickedBlock) {
        TitanBlock titanBlock = TitanBlockManager.getTitanBlock(clickedBlock.getLocation());
        if (titanBlock == null || !titanBlock.isRedrawBlock()) return;
        if (itemStack.getType() == Material.PLAYER_HEAD || itemStack.getType() == Material.PLAYER_WALL_HEAD) {
            TitanBoxLibs.tools.getSkullTool().placeSkull(clickedBlock, itemStack);
        } else {
            clickedBlock.setType(itemStack.getType());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onBlockBreakEvent(BlockBreakEvent event) {
        Block clickedBlock = event.getBlock();
        Player player = event.getPlayer();
        Location location = clickedBlock.getLocation();
        if (TitanBlockManager.isTitanBlock(location)) {
            event.setCancelled(true);
            if (LibsProtectionTool.canPlaceBlock(player, location)) {
                TitanBlock titanBlock = TitanBlockManager.getTitanBlock(clickedBlock.getLocation());
                if (titanBlock != null) {
                    TitanBlockListener titanBlockListener = TitanBlockManager.getTitanBlockListener(titanBlock);
                    if (titanBlockListener != null) titanBlockListener.onBlockBreak(new TitanBlockEvent(player, location, titanBlock));
                    if (titanBlock.isBreakBlockAllowed()) {
                        TitanBlock.breakBlock(titanBlock);
                    }
                }
            }

        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onBlockPlaceEvent(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();
        Player player = event.getPlayer();
        Block clickedBlock = event.getBlock();Location location = clickedBlock.getLocation();

        String titanItemID = TitanBoxLibs.tools.getItemStackTool().getTitanItemID(itemInHand);
        if (titanItemID != null && !titanItemID.isBlank() && !titanItemID.isEmpty()) {
            ItemStack itemStack = TitanBlockManager.getItemStack(titanItemID);
            if (!TitanBoxLibs.tools.getItemStackTool().isEmpty(itemStack))
            {
                if (LibsProtectionTool.canPlaceBlock(player, location)) {
                    TitanBlockManager.createTitanBlock(itemStack, clickedBlock.getLocation());
                    TitanBlock titanBlock = TitanBlockManager.getTitanBlock(clickedBlock.getLocation());
                    if (titanBlock != null) {
                        TitanBlockListener titanBlockListener = TitanBlockManager.getTitanBlockListener(titanBlock);
                        if (titanBlockListener != null)
                            titanBlockListener.onBlockPlace(new TitanBlockEvent(player, location, titanBlock));
                    }
                }
                else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
