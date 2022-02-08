package src.com.firesoftitan.play.titanbox.libs.managers;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsItemStackTool;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsNBTTool;

import java.util.List;
import java.util.Random;

public class BarcodeManager
{
    private final SaveManager barcodes = new SaveManager(  "barcodes");

    public BarcodeManager() {

    }

    public void save()
    {
        barcodes.save();
    }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasBarcode(ItemStack toBarcode)
    {
        if (!LibsItemStackTool.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                //noinspection ConstantConditions
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = LibsItemStackTool.getName(toBarcode);
                    String barcode = this.getBarcode(toBarcode);
                    return barcodes.contains(name + "." + barcode);
                }
            }
        }
        return false;
    }
    public void setBarcodeTrue(ItemStack toBarcode, Player player)
    {

        if (!LibsItemStackTool.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                //noinspection ConstantConditions
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = LibsItemStackTool.getName(toBarcode);
                    String barcode = this.getBarcode(toBarcode);
                    List<String> check = toBarcode.getItemMeta().getLore();
                    int line = 0;
                    if (barcodes.contains(name + "." + barcode)) {
                        barcodes.set(name + "." + barcode, true);
                        barcodes.set(name + ".info." + barcode + ".time" , System.currentTimeMillis());
                        barcodes.set(name + ".info." + barcode + ".user" , player.getName());
                        barcodes.set(name + ".info." + barcode + ".item" , toBarcode.getItemMeta().getDisplayName());
                        if (check != null) {
                            for (String s : check) {
                                barcodes.set(name + ".info." + barcode + ".line" + line, s);
                                line++;
                            }
                        }
                    }
                }
            }
        }
    }
    public String getBarcode(ItemStack toBarcode)
    {
        if (!LibsItemStackTool.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                //noinspection ConstantConditions
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = LibsItemStackTool.getName(toBarcode);
                    if(LibsNBTTool.hasNBTTag(toBarcode, "barcode"))
                    {
                        NBTTagCompound nbtTagCompound =  LibsNBTTool.getNBTTag(toBarcode);
                        return nbtTagCompound.l("barcode"); //getString
                    }

                    List<String> check = toBarcode.getItemMeta().getLore();
                    if (check != null) {
                        for (String s : check) {
                            if (s.startsWith(ChatColor.MAGIC + "barcode:")) {
                                return s.replace(ChatColor.MAGIC + "barcode:", "");
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    public String scanBarcode(ItemStack toBarcode)
    {
        if (!LibsItemStackTool.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                //noinspection ConstantConditions
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = LibsItemStackTool.getName(toBarcode);
                    String barcode = getBarcode(toBarcode);
                    if (barcodes.contains(name + "." + barcode)) {
                        return barcodes.getBoolean(name + "." + barcode) + "";
                    }
                }
            }
        }
        return null;
    }
    public ItemStack getNewBarcode(ItemStack toBarcode)
    {
        if (!LibsItemStackTool.isEmpty(toBarcode)) {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random(System.currentTimeMillis());
            while (salt.length() < 36) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();

            String name = LibsItemStackTool.getName(toBarcode);
            if (barcodes.contains(name + "." + saltStr)) {
                return getNewBarcode(toBarcode);
            }
            barcodes.set(name + "." + saltStr, false);
            NBTTagCompound nbtTagCompound = LibsNBTTool.getNBTTag(toBarcode);
            nbtTagCompound.a("barcode", saltStr); //setString
            toBarcode = LibsNBTTool.setNBTTag(toBarcode, nbtTagCompound);
        }
        return toBarcode;
    }
}
