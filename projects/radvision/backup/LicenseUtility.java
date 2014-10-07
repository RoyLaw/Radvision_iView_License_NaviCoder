package com.radvision.vcs.license;

import com.radvision.vcs.license.history.LicenseUsage;
import com.radvision.vcs.license.history.LicenseUsageUtility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
//import org.apache.log4j.Logger;

public class LicenseUtility
{
  public static final String LICENSE_FILE = "vcs-license.txt";
  public static final String PUBKEY_FILE = "vcs-pubkey.txt";
  public static final String USAGE_FILE_1 = "vcs_trf.exe";
  public static final String USAGE_FILE_2 = "vcs_2we.dll";
  private static LicenseUtility licenseUtility = null;

  public static LicenseUtility getInstance()
  {
    if (licenseUtility == null)
      licenseUtility = new LicenseUtility();
    return licenseUtility;
  }

  public License validateLicense(String strLicense)
    throws Exception
  {
    License license = parseLicense(strLicense.trim());
    if (!license.isVerified()) {
      throw new Exception("Verifying the license failed");
    }
    String licenseDir = ".";
    File fk = new File(licenseDir, "vcs-pubkey.txt");
    String programDir = readString(fk);
    if ((programDir == null) || (programDir.trim().length() == 0))
    {
      programDir = ".";
    }

    File history1 = new File(licenseDir, "vcs_trf.exe");
    File history2 = new File(programDir, "vcs_2we.dll");
    LicenseUsage[] lua1 = LicenseUsageUtility.readUsage1(history1);
    LicenseUsage[] lua2 = LicenseUsageUtility.readUsage2(history2);

    LicenseUsage[] lua = LicenseUsageUtility.merge(lua1, lua2);
    LicenseUsage newlu = ajustUsage(strLicense, lua);

    return license;
  }

  public License retrieve()
    throws Exception
  {
    File f = new File(".", "vcs-license.txt");
    String slicense = readString(f).trim();
    License license = new LicenseDecoder(new MD5Validator(6)).decode(slicense);
    return license;
  }

  public License parseLicense(String strLicense)
  {
    LicenseDecoder decoder = new LicenseDecoder(new MD5Validator(6));
    License license = decoder.decode(strLicense.trim());
    return license;
  }

  public void store(String aLicense)
    throws Exception
  {
    String strLicense = aLicense.trim();
    strLicense = strLicense.substring(0, 1) + LicenseDecoder.pad(strLicense.substring(1));

    String licenseDir = ".";
    File fk = new File(licenseDir, "vcs-pubkey.txt");
    String programDir = readString(fk);
    if ((programDir == null) || (programDir.trim().length() == 0))
    {
      programDir = ".";
    }

    File history1 = new File(licenseDir, "vcs_trf.exe");
    File history2 = new File(programDir, "vcs_2we.dll");
    LicenseUsage[] lua1 = LicenseUsageUtility.readUsage1(history1);
    LicenseUsage[] lua2 = LicenseUsageUtility.readUsage2(history2);

    LicenseUsage[] lua = LicenseUsageUtility.merge(lua1, lua2);

    LicenseUsage[] luaCurr = ensureCurrentUsage(strLicense, lua);

    File flicense = new File(licenseDir, "vcs-license.txt");
    writeString(strLicense, flicense);

    LicenseUsageUtility.writeUsage1(luaCurr, history1);
    LicenseUsageUtility.writeUsage2(luaCurr, history2);
  }

  public void store(String aLicense, String licenseDir, String programDir)
    throws Exception
  {
    String strLicense = aLicense.trim();
    strLicense = strLicense.substring(0, 1) + LicenseDecoder.pad(strLicense.substring(1));

    updateLicense(strLicense, new File(licenseDir, "vcs-license.txt"));
    ensurePubkey(programDir, new File(licenseDir, "vcs-pubkey.txt"));

    LicenseDecoder ld = new LicenseDecoder(new MD5Validator(6));
    License license = ld.decode(strLicense);

    LicenseUsage newUsage = new LicenseUsage();
    newUsage.setLicense(strLicense);
    newUsage.setTimeExpired(0L);
    newUsage.setTimeInstalled(System.currentTimeMillis());
    LicenseUsage[] lua = new LicenseUsage[1];
    lua[0] = newUsage;

    File history1 = new File(licenseDir, "vcs_trf.exe");
    File history2 = new File(programDir, "vcs_2we.dll");
    LicenseUsageUtility.writeUsage1(lua, history1);
    LicenseUsageUtility.writeUsage2(lua, history2);
  }

  private void updateLicense(String strLicense, File file)
    throws Exception
  {
    FileWriter out = new FileWriter(file);
    out.write(strLicense);
    out.flush();
    out.close();
  }

  private String readString(File file)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(file));
    return br.readLine();
  }

  private void writeString(String str, File file)
    throws IOException
  {
    FileWriter out = new FileWriter(file);
    out.write(str);
    out.flush();
    out.close();
  }

  private LicenseUsage[] ensureCurrentUsage(String strLicense, LicenseUsage[] lua)
    throws Exception
  {
    for (int i = 0; i < lua.length; i++)
    {
      if (strLicense.equals(lua[i].getLicense())) {
        return lua;
      }

    }

    LicenseUsage newlu = ajustUsage(strLicense, lua);
    LicenseUsage[] newlua = new LicenseUsage[lua.length + 1];
    System.arraycopy(lua, 0, newlua, 1, lua.length);
    newlua[0] = newlu;
    return newlua;
  }

  private LicenseUsage ajustUsage(String strLicense, LicenseUsage[] lua) throws Exception
  {
    License license = parseLicense(strLicense);
    int evalCount = license.getEvaluationCount();

    LicenseUsage newlu = null;
    if (license.isDemo())
    {
      int maxEvalCount = 0;
      int indexToMaxEvalCount = 0;
      int indexToMatchEvalCount = -1;
      for (int i = 0; i < lua.length; i++)
      {
        License hist = parseLicense(lua[i].getLicense());
        if (hist.isDemo())
        {
          int histEvalCount = hist.getEvaluationCount();
          if (histEvalCount > maxEvalCount)
          {
            maxEvalCount = histEvalCount;
            indexToMaxEvalCount = i;
          }
          if (histEvalCount == evalCount) {
            indexToMatchEvalCount = i;
          }
        }
      }
      if (indexToMatchEvalCount != -1)
      {
        newlu = new LicenseUsage();
        newlu.setLicense(strLicense);
        newlu.setTimeExpired(lua[indexToMatchEvalCount].getTimeExpired());
        newlu.setTimeInstalled(lua[indexToMatchEvalCount].getTimeInstalled());
      } else if (evalCount > maxEvalCount)
      {
        newlu = new LicenseUsage();
        newlu.setLicense(strLicense);
        newlu.setTimeInstalled(System.currentTimeMillis());
      }
      else
      {
        String errMsg = LicenseResourceBundle.getString("EVAL_CNT_ERROR", Locale.getDefault());
        Logger.getLogger(LicenseUtility.class).error(errMsg);
        throw new Exception(errMsg);
      }
    }
    else
    {
      newlu = new LicenseUsage();
      newlu.setLicense(strLicense);
      newlu.setTimeInstalled(System.currentTimeMillis());
    }
    return newlu;
  }

  private void ensurePubkey(String pubKey, File file)
    throws Exception
  {
    FileWriter out = new FileWriter(file);
    out.write(pubKey);
    out.flush();
    out.close();
  }

  public static void main(String[] argvs)
  {
    LicenseUtility u = getInstance();
    if (argvs.length == 0)
    {
      System.out.println("Usage: com.radvision.vcs.license.LicenseUtility thelicense");
      System.exit(0);
    }

    try
    {
      System.out.println("Store the license");
      u.store(argvs[0], ".", ".");
      System.out.println("Validate the license");
      u.validateLicense(argvs[0]);
      System.out.println("Update Store the license");
      u.store(argvs[0]);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Done");
  }
}