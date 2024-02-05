package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.enums.ArmorStandPoseEnum;
import com.firesoftitan.play.titanbox.libs.tools.LibsHologramTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HologramManager {
    private static final HashMap<String, List<HologramManager>> chunkKeySort = new HashMap<String, List<HologramManager>>();
    private static final HashMap<String, Long> chunkKeyTimeLoaded = new HashMap<String, Long>();
    private static final HashMap<UUID, HologramManager> uuidHolder = new HashMap<UUID, HologramManager>();
    private static final HashMap<String, HologramManager> locationHolder = new HashMap<String, HologramManager>();
    private static final HashMap<Entity, HologramManager> entityHolder = new HashMap<Entity, HologramManager>();


    public static void loadAll()
    {
        TitanBoxLibs.tools.getMessageTool().sendMessageSystem("Loading Holograms...");
        SaveManager saveManager = new SaveManager("holograms");
        for (String key: saveManager.getKeys("hologram"))
        {
            for (String subKey: saveManager.getKeys("hologram." + key))
            {
                HologramManager hologramManager = new HologramManager(saveManager.getSaveManager("hologram." + key + "." + subKey));
                addHologramManager(hologramManager);
            }
        }
        TitanBoxLibs.tools.getMessageTool().sendMessageSystem("Holograms Done");
    }
    public static void saveAll()
    {

        SaveManager saveManager = new SaveManager("holograms");
        saveManager.delete("hologram");// clear the old date
        saveManager.save();// re-save empty
        for (String key: chunkKeySort.keySet())
        {
            List<HologramManager> tmp = chunkKeySort.get(key);
            for (HologramManager hologramManager: tmp) {
                SaveManager save = hologramManager.save();
                if (save != null) saveManager.set("hologram." + key + "." + hologramManager.getKey(), save);
                else saveManager.delete("hologram." + key + "." + hologramManager.getKey()); //doesn't work ????
            }
        }
        saveManager.save();
    }
    public static void clearHolograms()
    {
        for (String key: chunkKeySort.keySet()) {
            List<HologramManager> tmp = chunkKeySort.get(key);
            for (HologramManager hologramManager : tmp) {
                hologramManager.deSpawn();
            }
        }
        chunkKeySort.clear();
        for(World world: Bukkit.getWorlds())
        {
            for(Entity e: world.getEntities())
            {
                if (TitanBoxLibs.tools.getHologramTool().isHologram(e)) e.remove();
            }
        }
    }
    public static void checkClear()
    {
        for(World world: Bukkit.getWorlds())
        {
            for(Entity e: world.getEntities())
            {
                if (TitanBoxLibs.tools.getHologramTool().isHologram(e)) {
                    long timeStamp = TitanBoxLibs.tools.getHologramTool().getTimeStamp(e);
                    String key = getKey(e.getLocation());
                    if (!chunkKeySort.containsKey(key))
                        if (System.currentTimeMillis() - timeStamp > 10000)
                            e.remove();
                }
            }
        }
    }

    private static String getKey(Location location)
    {
        if (location == null) return null;
        Chunk chunk = location.getChunk();
        World world = location.getWorld();
        Location key = new Location(world, chunk.getX(), 0, chunk.getZ());
        return TitanBoxLibs.tools.getSerializeTool().serializeLocation(key);
    }
    public static HologramManager getHologramManager(Location location)
    {
        String key = Tools.getSerializeTool(TitanBoxLibs.instants).serializeLocation(location);
        HologramManager hologramManager = locationHolder.get(key);
        if (hologramManager == null)
        {
            List<Entity> nearbyEntities = TitanBoxLibs.workerManager.getCraftWorker(location).getNearbyEntities(1, 1, 1);
            for (Entity e: nearbyEntities)
            {
                if (TitanBoxLibs.tools.getHologramTool().isHologram(e)) {
                    hologramManager = HologramManager.getHologramManager(e);
                }
            }
        }
        return hologramManager;
    }
    public static HologramManager getHologramManager(UUID uuid)
    {
        return uuidHolder.get(uuid);
    }
    public static List<HologramManager> getHologramManagers()
    {
        return new ArrayList<HologramManager>(uuidHolder.values());
    }
    public static HologramManager getHologramManager(Entity entity)
    {
        return entityHolder.get(entity);
    }

    private static void addHologramManager(HologramManager hologramManager)
    {
        String key = getKey(hologramManager.getLocation());
        List<HologramManager> tmp = chunkKeySort.get(key);
        if (tmp == null) tmp = new ArrayList<HologramManager>();
        tmp.add(hologramManager);
        chunkKeySort.put(key, tmp);
        uuidHolder.put(hologramManager.getUUID(), hologramManager);
        String key2 = Tools.getSerializeTool(TitanBoxLibs.instants).serializeLocation(hologramManager.getLocation());
        locationHolder.put(key2, hologramManager);
    }
    private static void removeHologramManager(HologramManager hologramManager)
    {
        String key = getKey(hologramManager.getLocation());
        List<HologramManager> tmp = chunkKeySort.get(key);
        if (tmp == null) tmp = new ArrayList<HologramManager>();
        tmp.remove(hologramManager);
        chunkKeySort.put(key, tmp);
        uuidHolder.remove(hologramManager.getUUID());
        String key2 = Tools.getSerializeTool(TitanBoxLibs.instants).serializeLocation(hologramManager.getLocation());
        locationHolder.remove(key2);
        hologramManager.deSpawn();
    }
    public static void processChunks()
    {
        for(Player player: Bukkit.getOnlinePlayers())
        {
         //   String loadedKey = getKey(player.getLocation());
            Location location = player.getLocation();
            Chunk chunk = location.getChunk();
            World world = location.getWorld();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int x = chunk.getX() + i;
                    int z = chunk.getZ() + j;
                    Location key = new Location(world, x, 0, z);
                    String loadedKey = TitanBoxLibs.tools.getSerializeTool().serializeLocation(key);
                    chunkKeyTimeLoaded.put(loadedKey, System.currentTimeMillis());
                }
            }
        }
    }
    public static void removeOldChunks()
    {
        List<String> remove = new ArrayList<String>();
        for(String key: chunkKeyTimeLoaded.keySet())
        {
            Long aLong = chunkKeyTimeLoaded.get(key);
            if (System.currentTimeMillis() - aLong > 10000)
            {
                if (chunkKeySort.containsKey(key)) {
                    List<HologramManager> tmp = chunkKeySort.get(key);
                    for (HologramManager manager : tmp) {
                        manager.deSpawn();
                    }
                }
                remove.add(key);
            }
        }
        for (String key: remove)
        {
            chunkKeyTimeLoaded.remove(key);
        }
    }
    public static void run()
    {
        for(String key: chunkKeyTimeLoaded.keySet())
        {
            if (chunkKeySort.containsKey(key)) {
                List<HologramManager> tmp = chunkKeySort.get(key);
                for (HologramManager manager : tmp) {
                    manager.spawn();
                }
            }
        }
    }

    private ArmorStand armorStand;
    private final JavaPlugin plugin;
    private boolean deleting = false;
    private Location location;
    private String customName;
    private final UUID uuid;
    private boolean deleted = false;

    private final HashMap<EquipmentSlot, ItemStack> equipment = new HashMap<EquipmentSlot, ItemStack>();
    private final HashMap<ArmorStandPoseEnum, EulerAngle> equipmentAngles = new HashMap<ArmorStandPoseEnum, EulerAngle>();

    public HologramManager(JavaPlugin plugin, @NotNull Location location) {
        this.uuid = UUID.randomUUID();
        this.plugin = plugin;
        this.equipmentAngles.put(ArmorStandPoseEnum.HEAD, EulerAngle.ZERO);
        this.customName = null;
        this.location = location.clone();
        addHologramManager(this);

    }
    public HologramManager(SaveManager saveManager)
    {
        this.plugin = (JavaPlugin)Bukkit.getPluginManager().getPlugin(saveManager.getString("plugin"));
        this.uuid = saveManager.getUUID("uuid");
        this.customName = saveManager.getString("customName");
        this.location = saveManager.getLocation("location");
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack item = saveManager.getItem("equipment." + equipmentSlot.name());
            if (!TitanBoxLibs.tools.getItemStackTool().isEmpty(item)) equipment.put(equipmentSlot, item);
        }
        for(ArmorStandPoseEnum standPoseEnum: ArmorStandPoseEnum.values())
        {
            if (saveManager.contains("equipmentPose." + standPoseEnum.name())) {
                Double aDx = saveManager.getDouble("equipmentPose." + standPoseEnum.name() + ".x");
                Double aDy = saveManager.getDouble("equipmentPose." + standPoseEnum.name() + ".y");
                Double aDz = saveManager.getDouble("equipmentPose." + standPoseEnum.name() + ".z");
                EulerAngle eulerAngle = new EulerAngle(aDx, aDy, aDz);
                this.equipmentAngles.put(standPoseEnum, eulerAngle);
            }
        }

        addHologramManager(this);
    }
    @Deprecated
    public HologramManager(JavaPlugin plugin, ArmorStand armorStand) {
        this.plugin = plugin;
        this.location = armorStand.getLocation().clone();
        this.uuid = armorStand.getUniqueId();
        this.customName = armorStand.getCustomName();
        EntityEquipment equipment = armorStand.getEquipment();
        if (equipment != null) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                ItemStack itemStack = equipment.getItem(equipmentSlot);
                if (!TitanBoxLibs.tools.getItemStackTool().isEmpty(itemStack))
                {
                    this.equipment.put(equipmentSlot, itemStack.clone());
                }
            }
        }
        addHologramManager(this);
    }
    public boolean isSpawned()
    {
        return armorStand != null;
    }
    public SaveManager save()
    {
        if (deleted || deleting) return null;
        SaveManager saveManager = new SaveManager();
        saveManager.set("plugin", plugin.getName());
        saveManager.set("uuid",this.uuid);
        saveManager.set("customName", customName);
        saveManager.set("location", this.location);
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            saveManager.set("equipment." + equipmentSlot.name(), equipment.get(equipmentSlot));
        }
        for(ArmorStandPoseEnum standPoseEnum: equipmentAngles.keySet())
        {
            EulerAngle eulerAngle = equipmentAngles.get(standPoseEnum);
            saveManager.set("equipmentPose." + standPoseEnum.name() + ".x", eulerAngle.getX());
            saveManager.set("equipmentPose." + standPoseEnum.name() + ".y", eulerAngle.getY());
            saveManager.set("equipmentPose." + standPoseEnum.name() + ".z", eulerAngle.getZ());
        }
        return saveManager;
    }

    protected void deSpawn()
    {
        entityHolder.remove(armorStand);
        if (this.armorStand == null) return;
        if (!this.armorStand.isDead()) this.armorStand.remove();
        this.armorStand = null;
    }
    protected void spawn()
    {
        if (deleted || deleting)
        {
            System.out.println("Trying to delete again....");
            HologramManager hologramManager = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    hologramManager.delete();
                }
            }.runTaskLater(TitanBoxLibs.instants, 1);
            return;
        }
        if (this.armorStand == null || this.armorStand.isDead()) {
            armorStand = Objects.requireNonNull(location.getWorld()).spawn(location, ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(false);
            armorStand.setCollidable(false);
            armorStand.setMarker(true);
            armorStand.setFireTicks(1000000);
            armorStand.setGravity(false);
            this.addID();
            this.setText(this.customName);
            for (EquipmentSlot slot : equipment.keySet()) {
                ItemStack itemStack = null;
                if (equipment.containsKey(slot) && !TitanBoxLibs.tools.getItemStackTool().isEmpty(equipment.get(slot))) itemStack = equipment.get(slot).clone();
                this.setEquipment(slot, itemStack);
            }
            for(ArmorStandPoseEnum standPoseEnum: equipmentAngles.keySet())
            {
                poseEquipment(standPoseEnum, equipmentAngles.get(standPoseEnum));
            }
            entityHolder.put(armorStand, this);
        }
    }
    protected String getKey()
    {
        return Tools.getSerializeTool(TitanBoxLibs.instants).serializeLocation(this.location);
    }
    private void addID() {
        String strLocation = getKey();
        String strPlugin = this.plugin.getName();
        Tools.getEntityTool(TitanBoxLibs.instants).setTag(armorStand, "tblHG", strPlugin, strLocation, System.currentTimeMillis() +"", "Version: 2.0.0");
    }
    public long getTimeStamp()
    {
        List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(armorStand, "Tags");
        String strPlugin = plugin.getName();
        for(String tag: tags)
        {
            try{ return Long.parseLong( tag ) ; }catch(Exception ignored){}
        }
        return 0;
    }
    public List<String> getTags()
    {
        return Tools.getNBTTool(TitanBoxLibs.instants).getListString(armorStand, "Tags");
    }
    public JavaPlugin getPlugin() {
        return plugin;
    }
    public void delete()
    {
        if (!deleting) {
            deleted =true;
            removeHologramManager(this);
            deleting = true;
            LibsHologramTool hologramTool = Tools.getHologramTool(plugin);
            if (hologramTool.getHologram(this.getUUID()) != null) hologramTool.removeHologram(this);
            if (armorStand != null) {
                List<Entity> nearbyEntities = armorStand.getNearbyEntities(1, 1, 1);
                List<ArmorStand> armorStands = new ArrayList<ArmorStand>();
                for (Entity entity : nearbyEntities) {
                    if (entity.getType() == EntityType.ARMOR_STAND) {
                        ArmorStand armor = (ArmorStand) entity;
                        List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
                        String strPlugin = plugin.getName();
                        if (tags.contains("tblHG")) {
                            if (tags.contains(strPlugin)) armorStands.add(armor);
                        }
                    }
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        armorStand.remove();
                        for (ArmorStand armorStand : armorStands) {
                            armorStand.remove();
                        }
                    }
                }.runTaskLater(this.getPlugin(), 1);
            }
            deleting = false;
        }

    }

    public void setLocation(Location location)
    {
        removeHologramManager(this);
        this.location = location.clone();
        if (armorStand != null && !armorStand.isDead()) armorStand.teleport(location.clone());
        addHologramManager(this);
    }
    public Location getLocation()
    {
        if (this.location == null) return null;
        return this.location.clone();
    }
    public ArmorStand getArmorStand()
    {
        return armorStand;
    }
    public UUID getUUID()
    {
        return this.uuid;
    }
    public void setText(String text)
    {
        if (text != null && !text.isEmpty()) {
            this.customName = ChatColor.translateAlternateColorCodes('&', text);
            if (armorStand != null && !armorStand.isDead()) {
                armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
                armorStand.setCustomNameVisible(true);
            }
        }
    }
    public void setEquipmentAngles(ArmorStandPoseEnum standPoseEnum, EulerAngle eulerAngle)
    {
        this.equipmentAngles.put(standPoseEnum, eulerAngle);
        poseEquipment(standPoseEnum, eulerAngle);
    }

    private void poseEquipment(ArmorStandPoseEnum standPoseEnum, EulerAngle eulerAngle) {
        if (armorStand != null && !armorStand.isDead()) {
            switch (standPoseEnum)
            {
                case HEAD -> this.armorStand.setHeadPose(eulerAngle);
                case BODY -> this.armorStand.setBodyPose(eulerAngle);
                case LEFT_ARM -> this.armorStand.setLeftArmPose(eulerAngle);
                case RIGHT_ARM -> this.armorStand.setRightArmPose(eulerAngle);
                case LEFT_LEG -> this.armorStand.setLeftLegPose(eulerAngle);
                case RIGHT_LEG -> this.armorStand.setRightLegPose(eulerAngle);
            }
        }
    }

    public void setEquipment(EquipmentSlot equipmentSlot, ItemStack itemStack)
    {
        if (TitanBoxLibs.tools.getItemStackTool().isEmpty(itemStack)) this.equipment.remove(equipmentSlot);
        else this.equipment.put(equipmentSlot, itemStack.clone());
        if (armorStand != null && !armorStand.isDead()) {
            EntityEquipment equipment = armorStand.getEquipment();
            if (equipment == null) return;
            equipment.setItem(equipmentSlot, itemStack.clone());
        }
    }
    public String getText()
    {
        return customName;
    }


}
