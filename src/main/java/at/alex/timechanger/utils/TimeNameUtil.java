package at.alex.timechanger.utils;

public class TimeNameUtil {
    public static String getNameOfTime(int time) {
        if(time<=1000)
            return "Sunrise";
        else if(time <= 12000)
            return "Daytime";
        else if(time <= 13000)
            return "Sunset";
        else if(time <=23000)
            return "Night";
        else if(time <= 24000)
            return "Sunrise";
        return "Midnight";
    }
}
