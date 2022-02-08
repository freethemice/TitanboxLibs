package src.com.firesoftitan.play.titanbox.libs.enums;

public enum  NBTTypeEnum {
    END((byte)0, "End"),
    BYTE((byte)1, "Byte"),
    SHORT((byte)2, "IntArray"),
    INT((byte)3, "IntArray"),
    LONG((byte)4, "Long"),
    FLOAT((byte)5, "Float"),
    DOUBLE((byte)6, "Double"),
    BYTE_ARRAY((byte)7, "ByteArray"),
    STRING((byte)8, "String"),
    LIST((byte)9, "List"),
    COMPOUND((byte)10, "Compound"),
    INT_ARRAY((byte)11, "IntArray"),
    LONG_ARRAY((byte)12, "LongArray");


    private final byte value;
    private final String name;
    NBTTypeEnum(byte value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
    public static NBTTypeEnum getType(byte value)
    {
        for(NBTTypeEnum nbtTypeEnum: NBTTypeEnum.values())
        {
            if (nbtTypeEnum.value == value) return nbtTypeEnum;
        }
        return null;
    }
}
