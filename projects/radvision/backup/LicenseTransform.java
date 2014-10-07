package com.visionnex.utility.server;

import com.radvision.vcs.license.License;
import com.radvision.vcs.license.LicenseUtility;
import java.io.PrintStream;

public class LicenseTransform
{
  public static LicenseInfo transform(License license)
  {
    LicenseInfo licenseInfo = new LicenseInfo();
    licenseInfo.setDemo(license.isDemo());
    licenseInfo.setEvaluationCount(license.getEvaluationCount());
    licenseInfo.setLicenseDays(license.getLicenseDays());
    licenseInfo.setMacId(license.getMacId());
    licenseInfo.setMaxCalls(license.getMaxCalls());
    licenseInfo.setMaxMembers(license.getMaxMembers());
    licenseInfo.setNonRV(license.isNonRV());
    licenseInfo.setOutlook(license.isOutlook());
    licenseInfo.setResources(license.isResources());
    licenseInfo.setScheduler(license.isScheduler());
    licenseInfo.setSerial(license.getSerial());
    licenseInfo.setVcsType(license.getVcsType());
    licenseInfo.setVerified(license.isVerified());
    licenseInfo.setVersion(license.getVersion());
    licenseInfo.setImFirst(license.isImFirst());
    //licenseInfo.setP2pCalls(license.getP2pCalls());
    //licenseInfo.setSpecialp2p(license.isSpecialp2p());
    //licenseInfo.setNmsTerminals(license.getNmsTerminals());
    //licenseInfo.setSpecialnms(license.isSpecialnms());
    //licenseInfo.setMobileLicense(license.getMobileLicense());
    //licenseInfo.setSpecialMobile(license.isSpecialMobile());

    int ds = licenseInfo.getLicenseDays();
    licenseInfo.setDaysLeft(ds == 0 ? -1 : ds);

    return licenseInfo;
  }

  public static void main(String[] argvs)
  {
    try
    {
      LicenseUtility u = LicenseUtility.getInstance();
      License license = u.validateLicense("iLdBn5ehQyW80KLy4CYFRYMASwAEQARAnAIAlAAAAAIAAAAAAkAEAAAAAAAAA95PuVn5L");
      LicenseInfo licenseInfo = transform(license);
      System.out.println("Server Tpe: " + licenseInfo.getVcsType());
      System.out.println("MAC: " + licenseInfo.getMacId());

      u.validateLicense("ZZZhEgAAAAAAABX2GCgaQIapOyAD01UauWDA");

      System.out.println("Store the license");
      u.store("hEgAAAAAAABX2GCgaQIapOyAD01UauWDA");
    }
    catch (IllegalArgumentException f)
    {
      System.out.println("The license is invalid");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Done");
  }
}