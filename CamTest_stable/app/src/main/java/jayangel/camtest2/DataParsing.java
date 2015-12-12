/*

DataParsing.java

- DataParsing (Regex) searches for date and time patterns using Pattern and Matcher classes
- Returns date and time in the form of an array ---> passed into Calendar intent

Created by Dawn of the Planet of the Apps on 12/11/2015

 */


package jayangel.camtest2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataParsing
{
    public static int [] main(String actualInput)
    {
        String input = actualInput;;
        // string maniulation
        input = input.toLowerCase();
        input= input.replace("pm", " pm");
        input= input.replace("am", " am");
        input= input.replace("noon", "12 am");
        input = input.replace("\n", " ");
        while (input.contains("  "))
        {
            input = input.replace("  ", " ");
        }
        String [] input_array = input.split(" ");

        String month_list = "'january' 'february' 'march' april may june july august september october november december"; //Jan Feb Mar Apr Jun Jul Aug Sept Sep Oct Nov Dec
        String am_pm = "am pm a.m. p.m.";
        String [] month_list_array = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};

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
        String regex_time = "^(1?[0-9]|2[0-3]):[0-5][0-9]$";
        Pattern pattern_time = Pattern.compile(regex_time);


        for(int i = 0; i < array_size; i++)
        {
            String [] each_input = input_array[i].split("");
            for (int j = 0; j < month_list_array.length; j++)
            {
                int index_counter = 0;
                int limit = 0;
                String [] each_month = month_list_array[j].split("");
                if (each_input.length>each_month.length)
                {
                    limit= each_month.length;
                }
                else
                {
                    limit = each_input.length;
                }
                for (int k = 0; k < limit; k++)
                {

                    if ((each_input[k].equals(each_month[k])) && date_bool)
                    {
                        index_counter++;
                    }
                    if ((index_counter == each_input.length) && date_bool)
                    {
                        month = input_array[i];
                        day = input_array[i+1];
                        year = input_array[i+2];
                        for (int h = 0; h < 12; h++)
                        {
                            if (month_list_array[h].contains(month))
                            {
                                month = Integer.toString(h+1);
                            }
                        }
                        date_bool = false;
                    }
                }
            }


            //System.out.println(month);
            // find month, day, and year if format is dec xx xxxx
            /*
            if (month_list.contains(input_array[i]) && date_bool)
            {
                month = input_array[i];
                day = input_array[i+1];
                year = input_array[i+2];
                date_bool = false; // changed to false so that it won't be called again

                for (int h = 0; h < 12; h++)
                {
                    if (month_list_array[h].contains(month))
                    {
                        month = Integer.toString(h+1);
                    }
                }
            }*/

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

            // time
            Matcher matcher_time = pattern_time.matcher(input_array[i]);
            if (matcher_time.matches() && time_bool)
            {
                time_is_am = input_array[i+1];
                String [] time_array = input_array[i].split(":");
                time_hour = time_array[0];
                time_minute = time_array[1];
                time_bool = false;
            }
            /*
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

                }
                else
                {
                    //System.out.println("else statement has been implemented");
                    time_hour = time;
                    time_minute = "00";

                }
                time_bool = false; //changed so that time wont be called again
            }
            */

            if ((time_bool || date_bool) && (i==array_size-1))
            {
                //System.out.println("I can't find one of them");
                time_bool= false;
                date_bool= false;
            }
        }

        //get current date time with Date()
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String [] date_array = dateFormat.format(date).split(" ");
        int asdf = date_array.length;
        String [] day_array = date_array[0].split("/");   // {year,month,day}
        String [] time_array = date_array[1].split(":");  // {hour, minute, second}
        // converting String to integer
        if (month.equals(""))
        {
            month = day_array[1];
        }
        int month_int = Integer.parseInt(month);
        if (day.equals(""))
        {
            day = day_array[2];
        }
        if (day.contains(","))
        {
            String [] temp = day.split(",");
            day = temp[0];
        }
        if (day.contains("th"))
        {
            String [] temp = day.split("th");
            day = temp[0];
        }
        int day_int = Integer.parseInt(day);
        if (year.equals(""))
        {
            year = day_array[0];
        }
        int year_int = Integer.parseInt(year);
        if (time_hour.equals(""))
        {
            time_hour = time_array[0];
        }
        int hour_int = Integer.parseInt(time_hour);
        if (time_minute.equals(""))
        {
            time_minute = time_array[1];
        }
        int minute_int = Integer.parseInt(time_minute);
        if (time_is_am.contains("p"))
        {
            hour_int += 12;
        }

        //******** THIS IS IT **********
        int [] result = {month_int,day_int,year_int,hour_int,minute_int};
        for (int t = 0; t<5; t++)
        {
            System.out.println(result[t]);
        }

        return result;
        //******** THIS IS IT **********
    }
}
