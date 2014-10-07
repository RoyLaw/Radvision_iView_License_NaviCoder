package com.radvision.vcs.license.history;

import java.util.Date;

public final class LicenseUsage
{
  private String license;
  private long timeInstalled;
  private long timeExpired;

  public LicenseUsage()
  {
  }

  public LicenseUsage(String license, long timeInstalled, long timeExpired)
  {
    this.license = license;
    this.timeInstalled = timeInstalled;
    this.timeExpired = timeExpired;
  }
  public String getLicense() {
    return this.license; } 
  public void setLicense(String license) { this.license = license; } 
  public long getTimeInstalled() { return this.timeInstalled; } 
  public void setTimeInstalled(long timeInstalled) { this.timeInstalled = timeInstalled; } 
  public long getTimeExpired() { return this.timeExpired; } 
  public void setTimeExpired(long timeExpired) { this.timeExpired = timeExpired; }

  public boolean equals(Object other)
  {
    if ((other == null) || (other.getClass() != LicenseUsage.class)) {
      return false;
    }
    LicenseUsage luother = (LicenseUsage)other;

    if (this.license == null)
    {
      if (luother.license != null) {
        return false;
      }
    }
    else if (!this.license.equals(luother.license)) {
      return false;
    }
    return (this.timeInstalled == luother.timeInstalled) || (this.timeExpired == luother.timeExpired);
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(this.license);
    sb.append(".  Installed: ");
    sb.append(new Date(this.timeInstalled));
    sb.append(".  Expired: ");
    sb.append(this.timeExpired);
    return sb.toString();
  }
}