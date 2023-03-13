package com.application.alphacapital.superapp.pms.utils;

import java.util.HashMap;

public final class Converter
{
    private static final HashMap<Integer, String> suffixes;
    private static final HashMap<Integer, String> prefixes;

    static
    {
        suffixes = new HashMap<Integer, String>();
        suffixes.put(3, "Hundred ");
        suffixes.put(4, "Thousand ");
        suffixes.put(5, "Thousand ");
        suffixes.put(6, "Lac ");
        suffixes.put(7, "Lacs ");
        suffixes.put(8, "Crore ");
        suffixes.put(9, "Crores ");

        prefixes = new HashMap<Integer, String>();
        prefixes.put(0, "");
        prefixes.put(1, "One ");
        prefixes.put(2, "Two ");
        prefixes.put(3, "Three ");
        prefixes.put(4, "Four ");
        prefixes.put(5, "Five ");
        prefixes.put(6, "Six ");
        prefixes.put(7, "Seven ");
        prefixes.put(8, "Eight ");
        prefixes.put(9, "Nine ");
        prefixes.put(10, "Ten ");
        prefixes.put(11, "Eleven ");
        prefixes.put(12, "Tweleve ");
        prefixes.put(13, "Thirteen ");
        prefixes.put(14, "Fourteen ");
        prefixes.put(15, "Fifteen ");
        prefixes.put(16, "Sixteen ");
        prefixes.put(17, "Seveneteen ");
        prefixes.put(18, "Eighteen ");
        prefixes.put(19, "Nineteen ");

        // decades
        prefixes.put(20, "Twenty ");
        prefixes.put(30, "Thirty ");
        prefixes.put(40, "Fourty ");
        prefixes.put(50, "Fifty ");
        prefixes.put(60, "Sixty ");
        prefixes.put(70, "Seventy ");
        prefixes.put(80, "Eighty ");
        prefixes.put(90, "Ninty ");
    }

    public static String getCountSuffix(int value)
    {
        return suffixes.containsKey(value) ? suffixes.get(value) : "";
    }

    public static String getCountPrefix(String count)
    {
        int value = Integer.valueOf(count);

        if (prefixes.containsKey(value))
        {
            return prefixes.get(value);
        }

        // to decade
        value = (int) Math.floor(value / 10) * 10;

        if (prefixes.containsKey(value))
        {
            return prefixes.get(value);
        }

        return "";
    }

    public static String convertIntoEnglish(int num)
    {
        String number = "" + num;
        int len = number.length();
        String sentence = "";
        String temp = "";
        for (int i = len; i > 0; i--)
        {
            // 00 00 00 000
            int n = len - i;
            if ((i % 2) > 0 && i > 3)
            {
                temp = number.substring(n, (n + 2));
                int v = Integer.parseInt(temp);
                if (v < 10) continue;
                sentence = sentence + getCountPrefix(temp);
                if (v > 19)
                {
                    temp = temp.substring(1);
                    sentence = sentence + getCountPrefix(temp);
                }
                sentence = sentence + getCountSuffix(i);
                i--;
            }
            else if ((i % 2) == 0 && i > 3)
            {
                temp = number.substring(n, (n + 1));
                sentence = sentence + getCountPrefix(temp);
                sentence = sentence + getCountSuffix(i);
            }
            else if (i == 3)
            {
                temp = number.substring(n, (n + 1));
                if (temp.startsWith("0")) continue;
                sentence = sentence + getCountPrefix(temp);
                sentence = sentence + getCountSuffix(3);
            }
            else
            {
                if (i == 2) temp = number.substring(len - 2);
                else temp = number.substring(len - 1);
                int v = Integer.parseInt(temp);
                sentence = sentence + getCountPrefix(temp);
                if (v > 19)
                {
                    temp = temp.substring(1);
                    sentence = sentence + getCountPrefix(temp);
                }
                i--;
            }
        }
        return sentence;
    }
}