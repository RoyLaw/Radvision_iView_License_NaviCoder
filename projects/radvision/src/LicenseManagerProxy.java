package com.radvision.vcms.oss.biz.util;

//import org.apache.log4j.Logger;

public class LicenseManagerProxy
{
  //private static final Logger log = Logger.getLogger(LicenseManagerProxy.class);
  private static LicenseManager licenseManager;

  public static void main(String[] args)
  {
  }

  public static LicenseManager getLicenseManager()
  {
    return licenseManager;
  }

  public static void setLicenseManager(LicenseManager alicenseManager)
  {
    licenseManager = alicenseManager;
  }

  public static void refreshLicenseManager()
  {
    if (licenseManager == null)
    {
      throw new RuntimeException("License has not been initialized!");
    }
    try
    {
      System.out.println("Call LicenseManager init!");
      licenseManager.init();
    }
    catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}