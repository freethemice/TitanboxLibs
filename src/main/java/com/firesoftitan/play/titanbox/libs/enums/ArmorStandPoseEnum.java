package com.firesoftitan.play.titanbox.libs.enums;

public enum ArmorStandPoseEnum {
    HEAD("head"),
    BODY("body"),
    LEFT_ARM("left arm"),
    RIGHT_ARM("right arm"),
    LEFT_LEG("left leg"),
    RIGHT_LEG("right leg");

    private final String name;
    ArmorStandPoseEnum(String value) {
        this.name = value;
    }
    public String getName() {
        return name;
    }
    public static ArmorStandPoseEnum getPose(String value)
    {
        for(ArmorStandPoseEnum standPoseEnum: ArmorStandPoseEnum.values())
        {
            if (standPoseEnum.getName().equalsIgnoreCase(value)) return standPoseEnum;
        }
        return ArmorStandPoseEnum.HEAD;
    }

}
