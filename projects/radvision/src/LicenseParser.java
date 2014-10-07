package com.radvision.vcs.license.parser;

import com.radvision.vcs.license.License;
import com.radvision.vcs.license.LicenseDecoder;
import com.radvision.vcs.license.MD5Validator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

public class LicenseParser
{
  private static final String[] KEYS = { "ParseResult             =", "MacAddress              =", "SerialNum               =", "SoftVer                 =", "VCSType                 =", "MaxMembers              =", "MaxCalls                =", "Resource                =", "Scheduler               =", "Customization           =", "LicenseDays             =", "EvalCount               =", "Outlook                 =", "ImFirst                 =", "DemoLicence             =", "Custom(0-non,1-Cisco)   =", "MaxSeats                =", "MaxAudioCalls           =", "MaxMCUs                 =", "MaxMVPs                 =", "Notes                   =" };

  public static void main(String[] args)
  {
    LicenseParser parser = new LicenseParser();
    parser.parse(args);
  }

  private void parse(String[] args)
  {
    if (args.length == 0)
    {
      printUsage();
      return;
    }

    PrintWriter out = null;
    try
    {
      if (args.length == 2)
        out = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
      else {
        out = new PrintWriter(System.out);
      }
      LicenseDecoder decoder = new LicenseDecoder(new MD5Validator(6));
      License parsed = null;
      try
      {
        parsed = decoder.decode(args[0]);
      }
      catch (Exception e)
      {
      }

      if (parsed == null)
        printNull(out);
      else
        printLicense(parsed, out);
    }
    catch (Exception e)
    {
      if (out != null)
        printNull(out);
    }
    finally
    {
      if (out != null)
      {
        out.flush();
        out.close();
      }
    }
  }

  private void printNull(PrintWriter out)
  {
    out.println(KEYS[0] + "-1");
    out.println(KEYS[1] + "000000000000");
    out.println(KEYS[2] + "0000070000");
    out.println(KEYS[3] + "0.0.0.0");
    out.println(KEYS[4] + "0");
    out.println(KEYS[5] + "0");
    out.println(KEYS[6] + "0");
    out.println(KEYS[7] + "n");
    out.println(KEYS[8] + "n");
    out.println(KEYS[9] + "n");
    out.println(KEYS[10] + "0");
    out.println(KEYS[11] + "0");
    out.println(KEYS[12] + "n");
    out.println(KEYS[13] + "n");
    out.println(KEYS[14] + "n");
  }

  private void printLicense(License license, PrintWriter out)
  {
    out.println(KEYS[0] + (license.isVerified() ? "0" : "-1"));
    out.println(KEYS[1] + license.getMacId());
    out.println(KEYS[2] + license.getSerial());
    out.println(KEYS[3] + license.getVersion());
    out.println(KEYS[4] + license.getVcsType());
    out.println(KEYS[5] + license.getMaxMembers());
    out.println(KEYS[6] + license.getMaxCalls());
    out.println(KEYS[7] + yn(license.isResources()));
    out.println(KEYS[8] + yn(license.isScheduler()));
    out.println(KEYS[9] + yn(license.isNonRV()));
    out.println(KEYS[10] + license.getLicenseDays());
    out.println(KEYS[11] + license.getEvaluationCount());
    out.println(KEYS[12] + yn(license.isOutlook()));
    out.println(KEYS[13] + yn(license.isImFirst()));
    out.println(KEYS[14] + yn(license.isDemo()));
    out.println(KEYS[15] + license.getCustom());
    out.println(KEYS[16] + license.getMaxSeats());
    out.println(KEYS[17] + license.getMaxAudioCalls());
    out.println(KEYS[18] + license.getMaxMCUs());
    out.println(KEYS[19] + license.getMaxMVPs());
    out.println(KEYS[20] + yn(license.isNotes()));
  }
  private char yn(boolean b) {
    return b ? 'y' : 'n';
  }

  private void printUsage() {
    System.out.println("");
    System.out.println("iVIEW VCS License Parser");
    System.out.println("");
    System.out.println("Usage:");
    System.out.println("iViewVCSLicParse <licenseKey> [<OutputFile>]");
    System.out.println("<licenseKey> - is the license key to parse");
    System.out.println("<OutputFile> - the file to dump the parser output to.");
    System.out.println("");
  }
}