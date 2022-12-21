package com.firesoftitan.play.titanbox.libs.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class LibsItemStackTool {
    private Tools parent;

    public LibsItemStackTool(Tools parent) {
        this.parent = parent;
    }

    public ItemStack addGlow(ItemStack eggItem) {
        ItemStack addon = eggItem.clone();
        ItemMeta ITM = getItemMeta(addon);
        ITM.addEnchant(Enchantment.MENDING, 1, true);
        ITM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        addon.setItemMeta(ITM);
        return addon.clone();
    }
    public ItemStack insertLore(ItemStack toAdd, List<String> lore)
    {
        return insertLore(false, toAdd, lore);
    }

    public ItemStack insertLore(boolean clear, ItemStack toAdd, String... lores)
    {
        List<String> lore = new ArrayList<>(Arrays.asList(lores));
        ItemStack itemStack = insertLore(clear, toAdd, lore);
        return itemStack.clone();
    }

    public ItemStack insertLore(ItemStack toAdd, String... lores)
    {
        return insertLore(false, toAdd, lores);
    }

    @SuppressWarnings("ConstantConditions")
    public ItemStack insertLore(boolean clear, ItemStack toAdd, List<String> lore)
    {

        ItemMeta ITM = getItemMeta(toAdd);

        if (!clear)
        {
            if (ITM.hasLore())
            {
                List<String> lore2 = new ArrayList<>();
                lore2.addAll(lore);
                lore2.addAll(ITM.getLore());
                lore.clear();
                lore = lore2;
            }
        }
        ITM.setLore(lore);
        toAdd.setItemMeta(ITM.clone());
        return toAdd;
    }
    public String getTitanItemID(ItemStack itemStack)
    {
        if (Tools.tools.getItemStackTool().isEmpty(itemStack)) return null;
        return Tools.tools.getNBTTool().getString(itemStack, "TitanItemID");
    }
    public ItemStack setTitanItemID(ItemStack itemStack, String titanItemID)
    {
        return Tools.tools.getNBTTool().set(itemStack, "TitanItemID" , titanItemID);
    }
    public boolean isPlaceable(ItemStack itemStack)
    {
        if (Tools.tools.getItemStackTool().isEmpty(itemStack)) return true;
        if (!Tools.tools.getNBTTool().containsKey(itemStack, "TitanItemPlaceable")) return true;
        return Tools.tools.getNBTTool().getBoolean(itemStack, "TitanItemPlaceable");
    }
    public ItemStack setPlaceable(ItemStack itemStack, boolean placeable)
    {
        return Tools.tools.getNBTTool().set(itemStack, "TitanItemPlaceable" , placeable);
    }
    @SuppressWarnings("ConstantConditions")
    public ItemStack addLore(boolean clear, ItemStack toAdd, List<String> lore)
    {

        ItemMeta ITM = getItemMeta(toAdd);

        if (!clear)
        {
            if (ITM.hasLore())
            {
                List<String> lore2 = new ArrayList<>();
                lore2.addAll(ITM.getLore());
                lore2.addAll(lore);
                lore.clear();
                lore = lore2;
            }
        }
        ITM.setLore(lore);
        toAdd.setItemMeta(ITM.clone());
        return toAdd;
    }

    public ItemStack addLore(ItemStack toAdd, List<String> lore)
    {
        return addLore(false, toAdd, lore);
    }

    public ItemStack addLore(boolean clear, ItemStack toAdd, String... lores)
    {
        List<String> lore = new ArrayList<>(Arrays.asList(lores));
        ItemStack itemStack = addLore(clear, toAdd, lore);
        return itemStack.clone();
    }

    public ItemStack addLore(ItemStack toAdd, String... lores)
    {
        return addLore(false, toAdd, lores);
    }

    public ItemStack clearLore(ItemStack toAdd)
    {
        List<String> lore = new ArrayList<>();
        ItemMeta ITM = getItemMeta(toAdd);

        ITM.setLore(lore);
        toAdd.setItemMeta(ITM.clone());
        return toAdd;
    }

    public ItemMeta getItemMeta(ItemStack toAdd) {
        if (toAdd.hasItemMeta())
        {
            if (toAdd.getItemMeta() != null) return toAdd.getItemMeta();
        }
        return Bukkit.getItemFactory().getItemMeta(toAdd.getType());

    }

    public Map<Enchantment, Integer> getEnchants(ItemStack toAdd)
    {
        ItemMeta itemMeta =  Tools.tools.getItemStackTool().getItemMeta(toAdd);
        if (!itemMeta.hasEnchants()) return new HashMap<>();
        return itemMeta.getEnchants();

    }
    public ItemStack clearEnchants(ItemStack toAdd)
    {
        Set<Enchantment> all = toAdd.getEnchantments().keySet();
        for(Enchantment enc: all)
        {
            toAdd.removeEnchantment(enc);
        }
        return toAdd;
    }
    @Deprecated
    public ItemStack clearEnchanents(ItemStack toAdd)
    {
        Set<Enchantment> all = toAdd.getEnchantments().keySet();
        for(Enchantment enc: all)
        {
            toAdd.removeEnchantment(enc);
        }
        return toAdd;
    }

    public boolean equalsLore(List<String> lore, List<String> lore2) {
        String string1 = "";
        String string2 = "";
        Iterator<String> var4 = lore.iterator();

        String string;
        while(var4.hasNext()) {
            string = var4.next();
            //noinspection StringConcatenationInLoop
            string1 = string1 + "-NEW LINE-" + string;
        }

        var4 = lore2.iterator();

        while(var4.hasNext()) {
            string = var4.next();
            //noinspection StringConcatenationInLoop
            string2 = string2 + "-NEW LINE-" + string;
        }

        return string1.equals(string2);
    }

    public boolean equalsEnchants(Map<Enchantment, Integer> item1, Map<Enchantment, Integer> item2)
    {
        if (item1 == null && item2 == null) return true;
        if (item1 != null && item2 == null) return false;
        //noinspection ConstantConditions
        if (item2 != null && item1 == null) return false;
        if (item1.size() != item2.size()) return false;
        for(Enchantment e: item1.keySet())
        {
            if (!item2.containsKey(e)) return false;
            //noinspection NumberEquality
            if (item1.get(e) != item2.get(e)) return false;
        }
        return true;
    }
    public ItemStack getItemStackFromBlock(Location block)
    {
        return getItemStackFromBlock(block.getBlock());
    }
    public ItemStack decreaseItem(ItemStack item, int amount) {
        if (item == null) {
            return null;
        } else {
            ItemStack clone = item.clone();
            if (amount < clone.getAmount()) {
                clone.setAmount(clone.getAmount() - amount);
                return clone;
            } else {
                return null;
            }
        }
    }
    public ItemStack getItemStackFromBlock(Block block)
    {
        ItemStack test = block.getState().getData().toItemStack(1);
        return test.clone();
    }
    public boolean isItemEqual(ItemStack itemStackA, ItemStack itemStackB)
    {
        return isItemEqual(itemStackA, itemStackB, true);
    }
    public boolean isItemEqual(ItemStack itemStackA, ItemStack itemStackB, boolean checkEnchants) {
        return isItemEqual(itemStackA, itemStackB, checkEnchants, true);
    }
    public ItemStack getCustomModel(int id) {
        return getCustomModel(Material.COARSE_DIRT, id);
    }
    public ItemStack getCustomModel(Material material, int id) {
        ItemStack button = new ItemStack(material);
        button = this.changeName(button, " ");
        ItemMeta itemMeta;
        itemMeta = button.getItemMeta();
        itemMeta.setCustomModelData(id);
        button.setItemMeta(itemMeta);
        return button.clone();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isItemEqual(ItemStack itemStackA, ItemStack itemStackB, boolean checkEnchants, boolean checkDamage) {
        if (itemStackA == null) return itemStackB == null;
        if (itemStackB == null) return false;


        if (itemStackA.getType() == itemStackB.getType()) {//&& itemStackA.getAmount() >= itemStackB.getAmount()
            if (checkDamage) {
                boolean aContains = Tools.tools.getNBTTool().containsKey(itemStackA, "Damage");
                boolean bContains = Tools.tools.getNBTTool().containsKey(itemStackB, "Damage");
                int aDamage = Tools.tools.getNBTTool().getInteger(itemStackA, "Damage");
                int bDamage = Tools.tools.getNBTTool().getInteger(itemStackB, "Damage");
                if (aContains && bContains)
                    if (aDamage != bDamage) return false;

                if (aContains && !bContains) return false;
                if (!aContains && bContains) return false;
            }

            if (checkEnchants) {
                if (!equalsEnchants(itemStackA.getEnchantments(), itemStackB.getEnchantments())) return false;
            }
            if (itemStackA.hasItemMeta() && itemStackB.hasItemMeta()) {
                ItemMeta a = itemStackA.getItemMeta();
                ItemMeta b = itemStackB.getItemMeta();
                if (a instanceof BannerMeta && b instanceof BannerMeta)
                {
                    if (((BannerMeta)a).numberOfPatterns() !=  ((BannerMeta)b).numberOfPatterns()) return false;
                    if(!((BannerMeta)a).getPatterns().equals(((BannerMeta)b).getPatterns())) return false;
                }
                if (a instanceof EnchantmentStorageMeta && b instanceof EnchantmentStorageMeta)
                {
                    if (a.getEnchants() !=null && b.getEnchants() ==null) return false;
                    if (a.getEnchants() ==null && b.getEnchants() !=null) return false;
                    if (a.getEnchants() !=null && b.getEnchants() !=null) {
                        if (!a.getEnchants().equals(b.getEnchants()))
                            return false;
                    }
                }
                if (a instanceof SkullMeta && b instanceof SkullMeta)
                {
                    if (itemStackA.getType() == Material.PLAYER_HEAD) {
                        if ( Tools.tools.getSkullTool().getSkullTexture(itemStackA) != null &&  Tools.tools.getSkullTool().getSkullTexture(itemStackB) != null) {
                            if (! Tools.tools.getSkullTool().getSkullTexture(itemStackA).equals( Tools.tools.getSkullTool().getSkullTexture(itemStackB)))
                                return false;
                        }
                    }
                }
                if (a instanceof PotionMeta && b instanceof PotionMeta)
                {
                    if(!((PotionMeta)a).getCustomEffects().equals(((PotionMeta)b).getCustomEffects())) return false;
                    if(!((PotionMeta)a).getBasePotionData().equals(((PotionMeta)b).getBasePotionData())) return false;
                    if(!((PotionMeta)a).getBasePotionData().getType().equals(((PotionMeta)b).getBasePotionData().getType())) return false;
                    if (((PotionMeta)a).getColor() !=null && ((PotionMeta)b).getColor() ==null) return false;
                    if (((PotionMeta)a).getColor() ==null && ((PotionMeta)b).getColor() !=null) return false;
                    if (((PotionMeta)a).getColor() !=null && ((PotionMeta)b).getColor() !=null) {
                        if (!((PotionMeta) a).getColor().equals(((PotionMeta) b).getColor())) return false;
                    }
                }
                if (a instanceof BookMeta && b instanceof BookMeta)
                {
                    if(!((BookMeta)a).getAuthor().equals(((BookMeta)b).getAuthor())) return false;
                    if (((BookMeta)a).getGeneration() == null && ((BookMeta)b).getGeneration() != null) return false;
                    if (((BookMeta)a).getGeneration() != null && ((BookMeta)b).getGeneration() == null) return false;
                    if (((BookMeta)a).getGeneration() != null && ((BookMeta)b).getGeneration() != null)
                    {
                        if (!((BookMeta) a).getGeneration().equals(((BookMeta) b).getGeneration())) return false;
                    }
                    if(!((BookMeta)a).getPages().equals(((BookMeta)b).getPages())) return false;
                    if(!((BookMeta)a).getTitle().equals(((BookMeta)b).getTitle())) return false;
                }
                if (a instanceof LeatherArmorMeta && b instanceof LeatherArmorMeta)
                {
                    if(!((LeatherArmorMeta)a).getColor().equals(((LeatherArmorMeta)b).getColor())) return false;
                }
                if (a instanceof FireworkMeta && b instanceof FireworkMeta)
                {
                    if(!((FireworkMeta)a).getEffects().equals(((FireworkMeta)b).getEffects())) return false;
                    if(((FireworkMeta)a).getPower() != ((FireworkMeta)b).getPower()) return false;
                }
                if (a instanceof EnchantmentStorageMeta && b instanceof EnchantmentStorageMeta)
                {
                    if (((EnchantmentStorageMeta)a).getStoredEnchants().size() != ((EnchantmentStorageMeta)b).getStoredEnchants().size()) return false;
                    Map<Enchantment, Integer> aMap = ((EnchantmentStorageMeta)a).getStoredEnchants();
                    Map<Enchantment, Integer> bMap = ((EnchantmentStorageMeta)b).getStoredEnchants();
                    for(Enchantment enchantment: aMap.keySet())
                    {
                        if (!bMap.containsKey(enchantment)) return false;
                        //noinspection NumberEquality
                        if ((bMap.get(enchantment) != aMap.get(enchantment))) return false;
                    }
                }
                if (itemStackA.getItemMeta().hasDisplayName() && itemStackB.getItemMeta().hasDisplayName()) {
                    if (itemStackA.getItemMeta().getDisplayName().equals(itemStackB.getItemMeta().getDisplayName())) {
                        if (itemStackA.getItemMeta().hasLore() && !itemStackB.getItemMeta().hasLore()) {
                            return false;
                        }
                        if (itemStackA.getItemMeta().hasLore() && itemStackB.getItemMeta().hasLore()) {
                            return equalsLore(itemStackA.getItemMeta().getLore(), itemStackB.getItemMeta().getLore());
                        }
                        else return !itemStackA.getItemMeta().hasLore() && !itemStackB.getItemMeta().hasLore();
                    }
                    else return false;
                }
                else if (!itemStackA.getItemMeta().hasDisplayName() && !itemStackB.getItemMeta().hasDisplayName()) {
                    if (itemStackA.getItemMeta().hasLore() && !itemStackB.getItemMeta().hasLore()) {
                        return false;
                    }
                    if (itemStackA.getItemMeta().hasLore() && itemStackB.getItemMeta().hasLore()) {
                        return equalsLore(itemStackA.getItemMeta().getLore(), itemStackB.getItemMeta().getLore());
                    }
                    else return !itemStackA.getItemMeta().hasLore() && !itemStackB.getItemMeta().hasLore();

                }
                else return false;
            }
            else return !itemStackA.hasItemMeta() && !itemStackB.hasItemMeta();
        }
        else return false;

    }

    public boolean isArmor(ItemStack mat)
    {
        return isArmor(mat.getType());
    }

    public boolean isArmor(Material mat)
    {
        try {
            return switch (mat) {
                case NETHERITE_CHESTPLATE, GOLDEN_BOOTS, IRON_BOOTS, LEATHER_BOOTS, ELYTRA, CHAINMAIL_BOOTS, DIAMOND_BOOTS, LEATHER_LEGGINGS, IRON_LEGGINGS, GOLDEN_LEGGINGS, CHAINMAIL_LEGGINGS, DIAMOND_LEGGINGS, GOLDEN_HELMET, CHAINMAIL_HELMET, IRON_HELMET, LEATHER_HELMET, DIAMOND_HELMET, LEATHER_CHESTPLATE, IRON_CHESTPLATE, GOLDEN_CHESTPLATE, CHAINMAIL_CHESTPLATE, DIAMOND_CHESTPLATE, NETHERITE_BOOTS, NETHERITE_LEGGINGS, NETHERITE_HELMET -> true;
                default -> false;
            };
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWeapon(ItemStack mat)
    {
        return isWeapon(mat.getType());
    }

    public boolean hasCustomName(ItemStack mat)
    {
        if (mat.hasItemMeta())
        {
            //noinspection ConstantConditions
            return mat.getItemMeta().hasDisplayName();
        }
        return false;
    }
    public boolean isContainer(ItemStack itemStack)
    {
        Material type = itemStack.getType();
        if (type == Material.CHEST || type == Material.TRAPPED_CHEST || type == Material.BARREL || type == Material.FURNACE || type == Material.DROPPER
                || type == Material.DISPENSER || type == Material.BLAST_FURNACE || type == Material.SMOKER || type == Material.HOPPER
                || type == Material.BREWING_STAND) return true;
        if  (type == Material.SHULKER_BOX || type == Material.BLACK_SHULKER_BOX || type == Material.BLUE_SHULKER_BOX || type == Material.BROWN_SHULKER_BOX
                || type == Material.CYAN_SHULKER_BOX || type == Material.GRAY_SHULKER_BOX || type == Material.GREEN_SHULKER_BOX
                || type == Material.LIGHT_BLUE_SHULKER_BOX || type == Material.LIGHT_GRAY_SHULKER_BOX || type == Material.LIME_SHULKER_BOX
                || type == Material.ORANGE_SHULKER_BOX || type == Material.PINK_SHULKER_BOX || type == Material.PURPLE_SHULKER_BOX
                || type == Material.RED_SHULKER_BOX || type == Material.WHITE_SHULKER_BOX || type == Material.YELLOW_SHULKER_BOX) return  true;
        return false;
    }
    public boolean isVanillaResource(ItemStack itemStack)
    {
        List<Material> checkList = new ArrayList<>();
        for(Material copper: Material.values())
        {
            if (copper.name().toUpperCase().contains("COPPER")) checkList.add(copper);
        }
        checkList.add(Material.RAW_IRON);
        checkList.add(Material.RAW_IRON_BLOCK);
        checkList.add(Material.RAW_GOLD);
        checkList.add(Material.RAW_GOLD_BLOCK);
        checkList.add(Material.COPPER_INGOT);
        checkList.add(Material.COPPER_BLOCK);
        checkList.add(Material.ANCIENT_DEBRIS);
        checkList.add(Material.NETHERITE_SCRAP);
        checkList.add(Material.NETHERITE_INGOT);
        checkList.add(Material.REDSTONE);
        checkList.add(Material.DIAMOND);
        checkList.add(Material.IRON_INGOT);
        checkList.add(Material.GOLD_INGOT);
        checkList.add(Material.EMERALD);
        checkList.add(Material.DIAMOND_BLOCK);
        checkList.add(Material.EMERALD_BLOCK);
        checkList.add(Material.IRON_BLOCK);
        checkList.add(Material.REDSTONE_BLOCK);
        checkList.add(Material.GOLD_BLOCK);
        checkList.add(Material.DEEPSLATE_DIAMOND_ORE);
        checkList.add(Material.DIAMOND_ORE);
        checkList.add(Material.EMERALD_ORE);
        checkList.add(Material.NETHER_GOLD_ORE);
        checkList.add(Material.IRON_ORE);
        checkList.add(Material.DEEPSLATE_IRON_ORE);
        checkList.add(Material.REDSTONE_ORE);
        checkList.add(Material.DEEPSLATE_REDSTONE_ORE);
        checkList.add(Material.DEEPSLATE_GOLD_ORE);
        checkList.add(Material.GOLD_ORE);
        checkList.add(Material.ENDER_EYE);
        checkList.add(Material.ENDER_PEARL);
        checkList.add(Material.BONE_MEAL);
        checkList.add(Material.BONE);
        checkList.add(Material.BONE_BLOCK);
        checkList.add(Material.LAPIS_LAZULI);
        checkList.add(Material.LAPIS_BLOCK);
        checkList.add(Material.DEEPSLATE_LAPIS_ORE);
        checkList.add(Material.LAPIS_ORE);
        checkList.add(Material.LAVA_BUCKET);
        checkList.add(Material.WATER_BUCKET);
        checkList.add(Material.COAL);
        checkList.add(Material.DEEPSLATE_COAL_ORE);
        checkList.add(Material.COAL_ORE);
        checkList.add(Material.COAL_BLOCK);
        checkList.add(Material.CHARCOAL);
        checkList.add(Material.NETHER_QUARTZ_ORE);
        checkList.add(Material.QUARTZ);
        checkList.add(Material.QUARTZ_BLOCK);
        checkList.add(Material.BLAZE_ROD);
        checkList.add(Material.BLAZE_POWDER);
        checkList.add(Material.SUGAR_CANE);
        checkList.add(Material.SUGAR);
        checkList.add(Material.SLIME_BALL);
        checkList.add(Material.SLIME_BLOCK);
        checkList.add(Material.HONEY_BLOCK);
        checkList.add(Material.HONEYCOMB_BLOCK);
        checkList.add(Material.HONEYCOMB);
        checkList.add(Material.HONEY_BOTTLE);

        for(Material m: checkList) {
            if ( Tools.tools.getItemStackTool().isItemEqual(new ItemStack(m), itemStack)) {
                return true;
            }
        }
        return false;
    }
    public boolean isPotion(ItemStack itemStack)
    {
        if (! Tools.tools.getItemStackTool().isEmpty(itemStack)) {
            if (itemStack.hasItemMeta())
            {
                ItemMeta a= itemStack.getItemMeta();
                if (a instanceof PotionMeta)
                {
                    return true;
                }
            }
            return itemStack.getType() == Material.EXPERIENCE_BOTTLE || itemStack.getType() == Material.GLASS_BOTTLE || itemStack.getType() == Material.HONEY_BOTTLE;
        }
        return false;
    }
    public boolean isBook(ItemStack itemStack)
    {
        if (! Tools.tools.getItemStackTool().isEmpty(itemStack)) {
            return itemStack.getType() == Material.BOOK || itemStack.getType() == Material.ENCHANTED_BOOK || itemStack.getType() == Material.WRITABLE_BOOK
                    || itemStack.getType() == Material.WRITTEN_BOOK || itemStack.getType() == Material.BOOKSHELF;
        }
        return false;
    }
    public boolean isStone(ItemStack itemStack)
    {
        if (! Tools.tools.getItemStackTool().isEmpty(itemStack))
        {
            String name = itemStack.getType().name().toUpperCase();
            if (name.contains("COBBLESTONE") || name.contains("SANDSTONE")
                    || name.contains("GRANITE") || name.contains("DIORITE")  || name.contains("ANDESITE")
                    || name.contains("TERRACOTTA") || name.contains("BRICK") || name.contains("PRISMARINE")  || name.contains("BLACKSTONE")
                    || name.contains("CONCRETE") || name.contains("DEEPSLATE") || name.contains("CALCITE") || name.contains("AMETHYST"))
            {
                return true;
            }
            return name.contains("STONE") && !name.contains("RED") && !name.contains("BUTTON")
                    && !name.contains("GLOW") && !name.contains("GRIND") && !name.contains("CUTTER")
                    && !name.contains("SWORD") && !name.contains("SHOVEL") && !name.contains("AXE")
                    && !name.contains("HOE") && !name.contains("PLATE");
        }
        return false;
    }
    public boolean isWood(ItemStack itemStack)
    {
        if (! Tools.tools.getItemStackTool().isEmpty(itemStack))
        {
            String name = itemStack.getType().name().toUpperCase();
            if (name.contains("LOG") || name.contains("OAK")
                    || name.contains("SPRUCE") || name.contains("BIRCH")  || name.contains("JUNGLE")
                    || name.contains("ACACIA") || name.contains("WARPED") || name.contains("CRIMSON") || name.contains("MANGROVE"))
            {
                return !name.contains("SAPLING") && !name.contains("ROOTS") && !name.contains("LEAVES") && !name.contains("FUNGUS") && !name.contains("NYLIUM");
            }
        }
        return itemStack.getType() == Material.LADDER || itemStack.getType() == Material.STICK
                || itemStack.getType() == Material.TORCH || itemStack.getType() == Material.REDSTONE_TORCH;
    }

    public boolean isOcean(ItemStack itemStack)
    {
        return isOcean(itemStack.getType());
    }
    public boolean isOcean(Material mat)
    {
        if (mat == null) return false;
        if (mat.name().toLowerCase().contains("coral"))
        {
            return true;
        }
        if (mat.name().toLowerCase().contains("prismarine"))
        {
            return true;
        }
        return switch (mat) {
            case SEA_LANTERN, SEA_PICKLE, SEAGRASS, HEART_OF_THE_SEA, SPONGE, WET_SPONGE, ICE, BLUE_ICE, FROSTED_ICE, PACKED_ICE, WATER_BUCKET, TURTLE_EGG, TURTLE_HELMET, KELP, KELP_PLANT, DRIED_KELP, DRIED_KELP_BLOCK, TRIDENT -> true;
            default -> false;
        };
    }
    public boolean isTheEnd(ItemStack itemStack)
    {
        return isTheEnd(itemStack.getType());
    }
    public boolean isTheEnd(Material mat)
    {
        if (mat == null) return false;
        if (mat.name().toLowerCase().contains("end"))
        {
            return switch (mat) {
                case ENCHANTED_BOOK, ENDERMAN_SPAWN_EGG, ENDERMITE_SPAWN_EGG -> false;
                default -> true;
            };
        }
        if (mat.name().toLowerCase().contains("purpur")) {
            return true;
        }
        return switch (mat) {
            case OBSIDIAN, CHORUS_FLOWER, CHORUS_PLANT, CHORUS_FRUIT, POPPED_CHORUS_FRUIT, DRAGON_HEAD, DRAGON_EGG, SHULKER_SHELL, ELYTRA -> true;
            default -> false;
        };
    }
    public boolean isNether(ItemStack itemStack)
    {
        return isNether(itemStack.getType());
    }
    public boolean isNether(Material mat)
    {
        if (mat == null) return false;
        if (mat.name().toLowerCase().contains("nether") || mat.name().toLowerCase().contains("blackstone") || mat.name().toLowerCase().contains("crimson") ||
                mat.name().toLowerCase().contains("warped") || mat.name().toLowerCase().contains("soul"))
        {
            return true;
        }
        return switch (mat) {
            case TWISTING_VINES, TWISTING_VINES_PLANT, WEEPING_VINES, WEEPING_VINES_PLANT, LODESTONE, RESPAWN_ANCHOR, SHROOMLIGHT, CHAIN, QUARTZ, QUARTZ_BRICKS, QUARTZ_BLOCK, QUARTZ_PILLAR, QUARTZ_SLAB, QUARTZ_STAIRS, SMOOTH_QUARTZ, GLOWSTONE_DUST, GLOWSTONE, SOUL_SAND, BLAZE_POWDER, BLAZE_ROD, GHAST_TEAR, WITHER_SKELETON_SKULL, MAGMA_BLOCK, ANCIENT_DEBRIS, BASALT, POLISHED_BASALT, CRYING_OBSIDIAN, OBSIDIAN -> true;
            default -> false;
        };
    }
    public boolean isWeapon(Material mat)
    {
        try {
            if (mat == null) return false;
            return switch (mat) {
                case NETHERITE_AXE, TRIDENT, BOW, CROSSBOW, SHIELD, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD -> true;
                default -> false;
            };
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isSpawnEgg(ItemStack itemStack)
    {
        if (! Tools.tools.getItemStackTool().isEmpty(itemStack)) {
            return itemStack.getType().toString().toUpperCase().contains("_SPAWN_EGG");
        }
        return false;
    }
    public boolean isTool(ItemStack mat)
    {
        return isTool(mat.getType());
    }

    public boolean isTool(Material mat)
    {
        return switch (mat) {
            case NETHERITE_PICKAXE, SPYGLASS, FISHING_ROD, FLINT_AND_STEEL, SHEARS, WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, WOODEN_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, STONE_SHOVEL, DIAMOND_SHOVEL, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, WARPED_FUNGUS_ON_A_STICK, CARROT_ON_A_STICK, NETHERITE_HOE, NETHERITE_SHOVEL, NETHERITE_AXE -> true;
            default -> false;
        };
    }
    public ItemStack enchantItem(Material material, Enchantment enchantment, int level)
    {
        return enchantItem(new ItemStack(material).clone(), enchantment, level);
    }
    public ItemStack enchantItem(ItemStack itemStack, Enchantment enchantment, int level)
    {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return itemStack.clone();
    }



    public String getName(ItemStack toName)
    {
        return getName(toName, true);
    }

    public String getName(ItemStack toName, boolean stripcolor)
    {
        String name = toName.getType().name();
        if (toName.hasItemMeta())
        {
            //noinspection ConstantConditions
            if (toName.getItemMeta().hasDisplayName())
            {
                String test = toName.getItemMeta().getDisplayName();
                if (stripcolor) test = ChatColor.stripColor(test);
                if (test.length() > 0)
                {
                    return test;
                }

            }
        }
        return name;
    }
    public boolean isEmpty(ItemStack toCheck)
    {
        if (toCheck == null)
        {
            return  true;
        }
        if (toCheck.getType().equals(Material.AIR) || toCheck.getType().equals(Material.CAVE_AIR) || toCheck.getType().equals(Material.VOID_AIR))
        {
            return  true;
        }
        return toCheck.getAmount() < 1;
    }

    public ItemStack removeLore(ItemStack toAdd, int line)
    {
        ItemMeta ITM = getItemMeta(toAdd);
        if (ITM.hasLore()) {
            List<String> lore = ITM.getLore();
            //noinspection ConstantConditions
            lore.remove(line);
            ITM.setLore(lore);
            toAdd.setItemMeta(ITM.clone());
            return toAdd;
        }

        return toAdd.clone();
    }
    public ItemStack changeName(ItemStack toAdd, String Name) {
        ItemMeta IM =  Tools.tools.getItemStackTool().getItemMeta(toAdd);
        IM.setDisplayName(Name);
        toAdd.setItemMeta(IM);
        return toAdd.clone();
    }
    public boolean hasLore(ItemStack toAdd)
    {
        ItemMeta ITM =  Tools.tools.getItemStackTool().getItemMeta(toAdd);
        if (ITM != null) {
            return ITM.hasLore();
        }

        return false;
    }
    public List<String> getLore(ItemStack toAdd)
    {
        ItemMeta ITM = getItemMeta(toAdd);

        if (ITM.hasLore()) {
            return ITM.getLore();
        }

        return null;
    }

    public int getLoreSize(ItemStack toAdd)
    {
        ItemMeta ITM = getItemMeta(toAdd);

        if (ITM.hasLore()) {
            //noinspection ConstantConditions
            return ITM.getLore().size();
        }

        return -1;
    }
    public ItemStack reduceByOne(ItemStack itemStack) {
        if (itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
            return itemStack.clone();
        } else {
            return null;
        }
    }

}
