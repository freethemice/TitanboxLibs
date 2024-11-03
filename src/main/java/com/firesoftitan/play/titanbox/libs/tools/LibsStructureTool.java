package com.firesoftitan.play.titanbox.libs.tools;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructure;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.structure.CraftStructure;
import org.bukkit.structure.Structure;

import java.io.File;
import java.io.IOException;

public class LibsStructureTool {
    private final Tools parent;

    public LibsStructureTool(Tools parent) {
        this.parent = parent;
    }
    public void save(File file, Structure structure)
    {
        try {
            DefinedStructure definedStructure = ((CraftStructure) structure).getHandle();
            NBTTagCompound tagCompound = definedStructure.a(new NBTTagCompound());
            NBTCompressedStreamTools.a(tagCompound, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Structure create(Location locationA, Location locationB)
    {
        DefinedStructure structure = new DefinedStructure();

        Location[] normalized = normalizeEdges(locationA, locationB);
        int[] dimensions = getDimensions(normalized);
        BlockPosition blockPositionA = new BlockPosition(normalized[0].getBlockX(), normalized[0].getBlockY(), normalized[0].getBlockZ());
        BlockPosition blockPositionB = new BlockPosition(dimensions[0], dimensions[1], dimensions[2]);

        WorldServer worldServer = ((CraftWorld) locationA.getWorld()).getHandle();
        structure.a(worldServer, blockPositionA, blockPositionB, true, Blocks.km);
        IRegistryCustom minecraftRegistry = CraftRegistry.getMinecraftRegistry();
        return new CraftStructure(structure, minecraftRegistry);

    }
    private int[] getDimensions(Location[] corners) {
        if (corners.length != 2) throw new IllegalArgumentException("An area needs to be set up by exactly 2 opposite edges!");
        return new int[] { corners[1].getBlockX() - corners[0].getBlockX() + 1, corners[1].getBlockY() - corners[0].getBlockY() + 1, corners[1].getBlockZ() - corners[0].getBlockZ() + 1 };
    }
    private Location[] normalizeEdges(Location startBlock, Location endBlock) {
        int xMin, xMax, yMin, yMax, zMin, zMax;
        if (startBlock.getBlockX() <= endBlock.getBlockX()) {
            xMin = startBlock.getBlockX();
            xMax = endBlock.getBlockX();
        } else {
            xMin = endBlock.getBlockX();
            xMax = startBlock.getBlockX();
        }
        if (startBlock.getBlockY() <= endBlock.getBlockY()) {
            yMin = startBlock.getBlockY();
            yMax = endBlock.getBlockY();
        } else {
            yMin = endBlock.getBlockY();
            yMax = startBlock.getBlockY();
        }
        if (startBlock.getBlockZ() <= endBlock.getBlockZ()) {
            zMin = startBlock.getBlockZ();
            zMax = endBlock.getBlockZ();
        } else {
            zMin = endBlock.getBlockZ();
            zMax = startBlock.getBlockZ();
        }
        return new Location[] { new Location(startBlock.getWorld(), xMin, yMin, zMin), new Location(startBlock.getWorld(), xMax, yMax, zMax) };
    }

}
