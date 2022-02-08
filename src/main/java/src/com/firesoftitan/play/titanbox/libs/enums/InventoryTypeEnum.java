package src.com.firesoftitan.play.titanbox.libs.enums;

public enum InventoryTypeEnum {
    NONE( "None", false, null),
    CHEST( "None", true, "vanilla"),
    TRAPPED_CHEST( "None", true, "vanilla"),
    DISPENSER( "None", true, "vanilla"),
    HOPPER( "None", true, "vanilla"),
    DROPPER( "None", true, "vanilla"),
    FURNACE( "None", true, "vanilla"),
    BLAST_FURNACE( "None", true, "vanilla"),
    SMOKER( "None", true, "vanilla"),
    BARREL( "None", true, "vanilla"),
    BREWING_STAND( "None", true, "vanilla"),
    SLIMEFUN( "Slimefun", true, "slimefun");


    private final String name;
    private final boolean inventory;
    private final String type;
    InventoryTypeEnum(String name, Boolean inventory, String type)
    {
        this.name = name;
        this.inventory = inventory;
        this.type = type;

    }

    public boolean isVanilla() {
        return type.equalsIgnoreCase("vanilla");
    }
    public boolean isSlimefun()
    {
        return type.equalsIgnoreCase("slimefun");
    }
    public boolean isInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

}
