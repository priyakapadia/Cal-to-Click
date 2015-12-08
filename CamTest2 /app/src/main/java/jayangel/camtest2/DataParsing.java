package jayangel.camtest2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParsing {

    public static int [] main(){//String [] args) {

        String input = "adsf-sa/d ads-fdf s/dfd dec 25 2015 9:30 pm";

        // string maniulation
        input = input.toLowerCase();
        input = input.replace("\n", " ");
        while (input.contains("  "))
        {
            input = input.replace("  ", " ");
        }
        String [] input_array = input.split(" ");

        String month_list = "january february march april may june july august september october november december"; //Jan Feb Mar Apr Jun Jul Aug Sept Sep Oct Nov Dec
        String am_pm = "am pm a.m. p.m.";

        //String
        int array_size = input_array.length;
        String month = "";
        String day = "";
        String year = "";
        Boolean date_bool = true;
        String time = "";
        String time_hour = "";
        String time_minute = "";
        String time_is_am = ""; // am is true, pm is false;
        Boolean time_bool = true;

        String regex_backlash = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
        Pattern pattern_backlash = Pattern.compile(regex_backlash);
        String regex_hyphen = "^[0-3]?[0-9]-[0-3]?[0-9]-(?:[0-9]{2})?[0-9]{2}$";
        Pattern pattern_hyphen = Pattern.compile(regex_hyphen);

        for(int i = 0; i < array_size; i++)
        {
            // find month, day, and year if format is (xx/xx/xxxx) or (xx-xx-xxxx)
            Matcher matcher_backlash = pattern_backlash.matcher(input_array[i]);
            Matcher matcher_hyphen = pattern_hyphen.matcher(input_array[i]);
            if (matcher_backlash.matches() && date_bool)
            {
                String [] backlash = input_array[i].split("/");
                month = backlash[0];
                day = backlash[1];
                year = backlash[2];
                date_bool = false;
            }
            else if(matcher_hyphen.matches() && date_bool)
            {
                String [] backlash = input_array[i].split("-");
                month = backlash[0];
                day = backlash[1];
                year = backlash[2];
                date_bool = false;
            }

            // find month, day, and year if format is dec xx xxxx
            if (month_list.contains(input_array[i]) && date_bool)
            {
                month = input_array[i];
                day = input_array[i+1];
                year = input_array[i+2];
                date_bool = false; // changed to false so that it won't be called again
            }

            // time
            if (am_pm.contains(input_array[i]) && time_bool)
            {
                time_is_am = input_array[i]; //add 12 to hour if pm
                //System.out.println("THIS IS TIMEMME: " + time_is_am);
                time = input_array[i-1];
                //System.out.println(time);
                if (time.contains(":"))
                {
                    //System.out.println("IFFF statement has been implemented");
                    String [] time_array = time.split(":");
                    time_hour = time_array[0];
                    time_minute = time_array[1];
                    time_bool = false; //changed so that time wont be called again
                }
                else
                {
                    //System.out.println("else statement has been implemented");
                    time_hour = time;
                    time_minute = "00";
                    time_bool = false; //changed so that time wont be called again
                }
            }
        }
        // ******** THIS IS IT ********
        int month_int = Integer.parseInt(month);
        int day_int = Integer.parseInt(day);
        int year_int = Integer.parseInt(year);
        int hour_int = Integer.parseInt(time_hour);
        int minute_int = Integer.parseInt(time_minute);
        if (time_is_am.contains("p"))
        {
            hour_int += 12;
        }

        int [] result = {month_int,day_int,year_int,hour_int,minute_int};

        return result;
        //******** THIS IS IT **********
    }
}


