<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 0f4bf89e98b4a53b16a6168a4c0b081357b2be2c


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataParsing {

    public static void main(String [] args){

        String input = "/\n4\n7 - 7 &apos;\nfa, &apos; @\n. 0 are 75&quot;\n&apos; ¥ .5\n5\n5\n*1-\n; ¥£\n, !\n! * -\n6\n¥\n¥ £ i £ ; &apos;\n- rr;&apos;\n!\n- , . /\n- ¥ &apos;\nm 5*9\n)Û\n! !\n- OX { .\n¥\n9\n4\n¥ % OPEN TO EVERYONE\n6\nX ¥ EVERYWHERE\n¥\n&apos;-i\n@ {\nQ if\nE OV W ; = ;112 2015\nICE CREAM SOCIAL\n6- 8pm\nChocolate\nVanilla\nHazelnut\nAnd so much more\nCourtesy of C2C\nCl 2 .C\nƒ";
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
            // find month, day, and year if format is dec xx xxxx
            if (month_list.contains(input_array[i]) && date_bool)
            {
                month = input_array[i];
                day = input_array[i+1];
                year = input_array[i+2];
                date_bool = false; // changed to false so that it won't be called again
                String [] month_list_array = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
                
                for (int h = 0; h < 12; h++)
                {
                  if (month_list_array[h].contains(month))
                  {
                    month = Integer.toString(h+1);
                  }
                }      
            }
            
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
              System.out.println("I can't find one of them");
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
        System.out.println(month);
        System.out.println(day);
        System.out.println(year);
        System.out.println(time_hour);
        System.out.println(time_minute);
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

        //return result;
        //******** THIS IS IT **********
    }
<<<<<<< HEAD
}
=======
=======
>>>>>>> master
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class DataParsing {

    public static void main(String [] args) {
      
      String input = "adsf-sa/d ads-fdf s/dfd 2-3-15 9:30 pm";
      
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
      //******** THIS IS IT **********
    }   
}


<<<<<<< HEAD
>>>>>>> 386ae5d1834b52efbcd9902eb079f1ec104a5455
=======
>>>>>>> master
=======
}
>>>>>>> 0f4bf89e98b4a53b16a6168a4c0b081357b2be2c
