package com.radvision.vcms.oss.biz.util;

import com.radvision.vcs.license.License;
import com.radvision.vcs.license.LicenseDecoder;
import com.radvision.vcs.license.MD5Validator;
import com.radvision.vcs.license.history.LicenseUsage;
import com.radvision.vcs.license.history.LicenseUsageUtility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
//import org.apache.log4j.Logger;

public class LicenseManager
{
  private static long MILLISEC_ONE_DAY = 86400000L;

  //static Logger log = Logger.getLogger(LicenseManager.class);
  private String[] hostMacIds;
  private String strLicense;
  private License license;
  private LicenseUsage[] history;
  private LicenseUsage current;
  private boolean valid;
  private boolean badmac;
  private boolean expired;
  private long lastExpiredTime;

  public LicenseManager()
  {
  }

  public LicenseManager(String[] hostMacIds)
  {
    this.hostMacIds = hostMacIds;
  }

  public void init() throws Exception
  {
    readLicense();
   /* if (log.isInfoEnabled())
    {*/
      System.out.println("DEMO: " + this.license.isDemo());
      System.out.println("MAC: " + this.license.getMacId());
    //}

    if (!this.license.isVerified()) {
      throw new RuntimeException("Internal Error: license is invalid.");
    }

    readUsage();
    checkValid();

    Timer timer = new Timer("^clock^", true);
    timer.schedule(new ExpiryTimer(null), 900000L, 900000L);
  }

  public boolean hasValidLicense()
  {
    return this.valid;
  }

  public boolean hasExpired()
  {
    return this.expired;
  }

  public boolean hasBadMac()
  {
    return this.badmac;
  }

  public License getLicense()
  {
    return this.license;
  }

  public LicenseUsage getLicenseUsage()
  {
    return this.current;
  }

  public String[] getHostMacIds()
  {
    return this.hostMacIds;
  }

  public void setHostMacIds(String[] macids)
  {
    this.hostMacIds = macids;
  }

  private void readLicense() throws IOException
  {
    File f = new File(".", "vcs-license.txt");
    this.strLicense = readString(f);
    this.license = new LicenseDecoder(new MD5Validator(6)).decode(this.strLicense);
  }

  private void readUsage() throws Exception
  {
    String pubkey = readString(new File(".", "vcs-pubkey.txt"));
    File history1 = new File(".", "vcs_trf.exe");
    File history2 = new File(pubkey, "vcs_2we.dll");
    LicenseUsage[] lua1 = LicenseUsageUtility.readUsage1(history1);
    LicenseUsage[] lua2 = LicenseUsageUtility.readUsage2(history2);

    if (lua1.length != lua2.length) {
      throw new RuntimeException("Usage 1/1 mismatch.");
    }
    for (int i = 0; i < lua1.length; i++) {
      if (!lua1[i].equals(lua2[i]))
        throw new RuntimeException("Usage 1/1 changed.");
    }
    boolean notfound = true;
    for (int i = 0; (i < lua1.length) && (notfound); i++)
    {
      if (this.strLicense.equals(lua1[i].getLicense()))
      {
        this.history = lua1;
        this.current = lua1[i];
        notfound = false;

        //if (log.isInfoEnabled())
        //{
          System.out.println("Liense Usage: " + this.current);
        //}
      }
    }

    if (notfound)
      throw new Exception("Internal Error: Item missed in the history");
  }

  private void checkValid()
  {
    this.valid = false;
    this.badmac = true;
    this.expired = false;

    int daysLeft = getDaysLeft();
    if (daysLeft == 0)
    {
      this.expired = true;
      System.out.println("This license is expired.");
      return;
    }

    this.badmac = isMACBad();
    if (this.badmac)
    {
      return;
    }

    String vendorName = System.getProperty("vendor.name");
    if ((vendorName != null) && (vendorName.equalsIgnoreCase("cisco")) && (this.license.getCustom() != 1)) {
      return;
    }

    this.valid = true;
  }

  protected boolean isMACBad()
  {
    boolean bad = true;

    if ("000000000000".equals(this.license.getMacId()))
    {
      if ((!this.license.isDemo()) && ("true".equals(System.getProperty("vnex.vcms.oss.mechanism.force.real.mac"))))
      {
        bad = true;
      }
      else {
        bad = false;
      }
    }
    else {
      for (int i = 0; (i < this.hostMacIds.length) && (this.badmac); i++)
      {
        if (this.hostMacIds[i].equalsIgnoreCase(this.license.getMacId()))
          bad = false;
      }
    }
    if (bad) {
      System.out.println("Bad MAC. licensed MAC: " + this.license.getMacId());
    }
    return bad;
  }

  public int getDaysLeft()
  {
    int licensedDays = this.license.getLicenseDays();

    if ((this.license.isDemo()) && (licensedDays == 0)) {
      return -1;
    }
    if (!this.license.isDemo()) {
      return -1;
    }

    long timeInstalled = this.current.getTimeInstalled();
    long timeCurrent = new Date().getTime();
    long timeLicensed = (licensedDays + 1) * MILLISEC_ONE_DAY + timeInstalled;
    long timeLeft = timeLicensed - timeCurrent;
    if (timeLeft <= 0L)
      return 0;
    int daysLeft = (int)(timeLeft / MILLISEC_ONE_DAY);
    //if (log.isInfoEnabled()) {
      System.out.println("Licensedays: " + licensedDays + " timeinstalled:" + new Date(timeInstalled) + " left: " + daysLeft);
    //}

    long licensetimems = licensedDays * MILLISEC_ONE_DAY;
    long expiredTime = this.current.getTimeExpired();
    int expiredDays = (int)(expiredTime / MILLISEC_ONE_DAY);
    int daysLeft2 = licensedDays - expiredDays;
    if (daysLeft2 <= 0)
      return 0;
    //if (log.isInfoEnabled()) {
      System.out.println("Licensedays: " + licensedDays + " expiredDays: " + expiredDays + " daysleft: " + daysLeft2);
    //}
    return daysLeft2 < daysLeft ? daysLeft2 : daysLeft;
  }

  public void incrementExpiredTime()
  {
    //if (log.isInfoEnabled()) {
      System.out.println("Tick");
    //}
    if ((!this.valid) || (getDaysLeft() < 0)) {
      return;
    }
    if (this.lastExpiredTime == 0L)
    {
      this.lastExpiredTime = System.currentTimeMillis();
      return;
    }

    long now = System.currentTimeMillis();
    long elapsed = now - this.lastExpiredTime;
    this.lastExpiredTime = now;
    this.current.setTimeExpired(this.current.getTimeExpired() + elapsed);
    try
    {
      writeHistory();
    }
    catch (Throwable e) {
      System.err.println(e.getMessage());
    }

    if (getDaysLeft() == 0)
    {
      this.expired = true;
      this.valid = false;
    }
  }

  private String readString(File file) throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(file));
    return br.readLine();
  }

  private void writeHistory() throws IOException
  {
    File historyFile1 = new File(".", "vcs_trf.exe");
    File historyFile2 = new File(".", "vcs_2we.dll");
    LicenseUsageUtility.writeUsage1(this.history, historyFile1);
    LicenseUsageUtility.writeUsage2(this.history, historyFile2);
  }

  private class ExpiryTimer extends TimerTask
  {
    private ExpiryTimer()
    {
    }

    public void run()
    {
      LicenseManager.this.incrementExpiredTime();
    }

    ExpiryTimer(LicenseManager x1)
    {
      this();
    }
  }
}