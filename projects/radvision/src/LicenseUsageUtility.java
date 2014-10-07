package com.radvision.vcs.license.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LicenseUsageUtility
{
  public static LicenseUsage[] merge(LicenseUsage[] lua1, LicenseUsage[] lua2)
  {
    Map map = new HashMap();
    Set keys = new HashSet();
    for (int i = 0; i < lua1.length; i++)
    {
      if (keys.add(lua1[i].getLicense()))
        map.put(lua1[i].getLicense(), lua1[i]);
      else {
        merge((LicenseUsage)map.get(lua1[i].getLicense()), lua1[i]);
      }
    }
    for (int i = 0; i < lua2.length; i++)
    {
      if (keys.add(lua2[i].getLicense()))
        map.put(lua2[i].getLicense(), lua2[i]);
      else {
        merge((LicenseUsage)map.get(lua2[i].getLicense()), lua2[i]);
      }
    }
    LicenseUsage[] lu = new LicenseUsage[map.size()];
    Iterator iter = map.values().iterator();
    int i = 0;
    while (iter.hasNext())
      lu[(i++)] = ((LicenseUsage)iter.next());
    return lu;
  }

  public static void merge(LicenseUsage lu1, LicenseUsage lu2)
  {
    lu1.setTimeExpired(Math.max(lu1.getTimeExpired(), lu2.getTimeExpired()));
    lu1.setTimeInstalled(Math.min(lu1.getTimeInstalled(), lu2.getTimeInstalled()));
  }

  public static LicenseUsage[] readUsage1(File historyFile1)
  {
    LicenseUsageObfuscator luo = new LicenseUsageObfuscator();
    String history = read(historyFile1);
    return history.length() == 0 ? new LicenseUsage[0] : luo.obf1(history);
  }

  public static LicenseUsage[] readUsage2(File historyFile2)
  {
    LicenseUsageObfuscator luo = new LicenseUsageObfuscator();
    String history = read(historyFile2);
    return history.length() == 0 ? new LicenseUsage[0] : luo.obf2(history);
  }

  public static void writeUsage1(LicenseUsage[] lua, File file) throws IOException
  {
    LicenseUsageObfuscator luo = new LicenseUsageObfuscator();
    FileWriter out = new FileWriter(file);
    out.write(luo.obf1(lua));
    out.flush();
    out.close();
  }

  public static void writeUsage2(LicenseUsage[] lua, File file) throws IOException
  {
    LicenseUsageObfuscator luo = new LicenseUsageObfuscator();
    FileWriter out = new FileWriter(file);
    out.write(luo.obf2(lua));
    out.flush();
    out.close();
  }

  private static String read(File file)
  {
    try
    {
      StringBuffer sb = new StringBuffer();
      BufferedReader br = new BufferedReader(new FileReader(file));
      char[] buf = new char[1024];
      int r = 0;
      while ((r = br.read(buf)) > 0)
        sb.append(buf, 0, r);
      br.close();
      return sb.toString();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }return "";
  }

  private static void write(File file, String s)
  {
    try
    {
      Writer writer = new FileWriter(file);
      writer.write(s);
      writer.flush();
      writer.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}