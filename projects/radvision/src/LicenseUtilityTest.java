package com.radvision.vcs.license;

import com.radvision.vcs.license.history.LicenseUsage;
import com.radvision.vcs.license.history.LicenseUsageUtility;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;

public class LicenseUtilityTest
{
  public static void main(String[] argv)
  {
	try
    {
      LicenseUtility u = LicenseUtility.getInstance();
      u.store(" iEgAIAo57jjHeIAAAJCMDuOcGmZ4oX28S", ".", ".");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Done");
  }

  private static void displayUsage()
  {
    File history1 = new File(".", "vcs_trf.exe");
    File history2 = new File(".", "vcs_2we.dll");
    LicenseUsage[] lua1 = LicenseUsageUtility.readUsage1(history1);
    LicenseUsage[] lua2 = LicenseUsageUtility.readUsage2(history2);
    doDisplay("Usage 1", lua1);
    doDisplay("Usage 2", lua2);
  }

  private static void doDisplay(String title, LicenseUsage[] lua)
  {
    System.out.println("---TITLE---" + title);
    for (int i = 0; i < lua.length; i++)
      System.out.println("Lincese: " + lua[i].getLicense() + " Time Installed: " + new Date(lua[i].getTimeInstalled()) + " Time Expired: " + lua[i].getTimeExpired());
  }
}