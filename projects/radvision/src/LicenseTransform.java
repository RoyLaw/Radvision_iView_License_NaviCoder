package com.visionnex.utility.server;

import com.radvision.vcs.license.License;
import com.radvision.vcs.license.LicenseUtility;
import com.radvision.vcs.license.LicenseEncoder;
import com.radvision.vcs.license.MD5Generator;
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
      License license = u.validateLicense("hEgAAAAAAABX2GCgaQIapOyAD01UauWDA");
      //LicenseInfo licenseInfo = transform(license);
	  license.setVcsType(1);
      System.out.println("Server Tpe: " + license.getVcsType());
	  license.setMacId("001560542eb4");
      System.out.println("MAC: " + license.getMacId());
      System.out.println("Day: " + license.getLicenseDays());
	  license.setSerial("8625987123");
	  System.out.println("SN: " + license.getSerial());
	  System.out.println("MaxCalls: " + license.getMaxCalls());
	  //license.setMaxMembers(5000);
	  System.out.println("MaxMembers: " + license.getMaxMembers());
	  System.out.println("Verified: " + license.isVerified());
	  license.setVersion("7.7.0.0");
	  System.out.println("Ver: " + license.getVersion());
	  System.out.println("Resources: " + license.isResources());
	  System.out.println("Scheduler: " + license.isScheduler());
	  System.out.println("NonRV: " + license.isNonRV());
	  System.out.println("Outlook: " + license.isOutlook());
	  System.out.println("EvaluationCount: " + license.getEvaluationCount());
	  license.setDemo(false);
	  System.out.println("Demo: " + license.isDemo());
	  System.out.println("ImFirst: " + license.isImFirst());
	  //license.setCustom(3);
	  System.out.println("Customer: " + license.getCustom());
	  System.out.println("MaxSeats: " + license.getMaxSeats());
	  System.out.println("MaxAudioCalls: " + license.getMaxAudioCalls());
	  //license.setMaxMCUs(100);
	  System.out.println("MaxMCU: " + license.getMaxMCUs());
	  //license.setMaxMVPs(100);
	  System.out.println("MaxMVPs: " + license.getMaxMVPs());
	  System.out.println("Notes: " + license.isNotes());
      LicenseEncoder encoder = new LicenseEncoder(new MD5Generator(6));
      String strle = encoder.encode(license);
	  System.out.println(strle);
      //System.out.println();
      //u.validateLicense("ZZZhEgAAAAAAABX2GCgaQIapOyAD01UauWDA");

      System.out.println("Store the license");
      //u.store("hEgAAAAAAABX2GCgaQIapOyAD01UauWDA");
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