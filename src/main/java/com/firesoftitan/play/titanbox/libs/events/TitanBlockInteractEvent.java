package com.firesoftitan.play.titanbox.libs.events;

import com.firesoftitan.play.titanbox.libs.blocks.TitanBlock;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public record TitanBlockInteractEvent(Player player, Location location, TitanBlock titanBlock, Action action, ItemStack item, BlockFace blockFace, EquipmentSlot hand) {
}
