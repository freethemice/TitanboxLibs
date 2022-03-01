package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class LibsFormattingTool
{
    private Tools parent;

    public LibsFormattingTool(Tools parent) {
        this.parent = parent;
    }

    private final NavigableMap<Long, String> suffixes = new TreeMap<>();

    {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");

    }
    public String formatSuffixe(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatSuffixe(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatSuffixe(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        double v = truncated / 10d;
        long l = truncated / 10;
        boolean hasDecimal = truncated < 100 && v != l;
        return hasDecimal ? v + suffix : l + suffix;
    }

    public String formatCommas(Long value)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(value);
    }

    public String formatCommas(double value)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(value);
    }

    public String formatLocation(Location location)
    {
        if (location == null) return null;
        return "(World: " + Tools.tools.getLocationTool().getWorldName(location) + ") " + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }
    public String formatTime(long lastping)
    {
        String time = " Seconds";
        lastping = lastping / 1000;
        if (lastping > 60L)
        {
            time = " Minutes";
            lastping = lastping / 60L;
            if (lastping > 60L)
            {
                time = " Hours";
                lastping = lastping / 60L;
                if (lastping > 24L)
                {
                    time = " Days";
                    lastping = lastping / 24L;
                    if (lastping > 30L)
                    {
                        time = " Months";
                        lastping = lastping / 24L;
                    }
                }
            }
        }
        if (lastping < 0)
        {
            return "400 Years ago in 1972";
        }
        //noinspection ConstantConditions
        if (lastping < 1 && time.equals(" Seconds")) return "< 1 second";
        return lastping + time;
    }

    public String formatTimeFromNow(long lastping)
    {
        long last = System.currentTimeMillis() - lastping;
        String time = " Seconds";
        last = last / 1000;
        if (last > 60)
        {
            time = " Minutes";
            last = last / 60;
            if (last > 60)
            {
                time = " Hours";
                last = last / 60;
            }
        }
        return last + time;
    }
    public String fixCapitalization(String Namespace)
    {
        if (Namespace.length() > 0) {
            String fixing = Namespace.replace("_", " ").toLowerCase();
            return WordUtils.capitalize(fixing);
        }
        return "";
    }
}
