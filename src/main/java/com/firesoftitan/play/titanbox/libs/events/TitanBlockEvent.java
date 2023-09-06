package com.firesoftitan.play.titanbox.libs.events;

import com.firesoftitan.play.titanbox.libs.blocks.TitanBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public record TitanBlockEvent(Player player, Location location, TitanBlock titanBlock) {
}
