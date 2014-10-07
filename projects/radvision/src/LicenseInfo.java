package com.visionnex.utility.server;

import java.io.Serializable;

public class LicenseInfo
  implements Serializable
{
  public static final int TYPE_SERVICE_PROVIDER = 0;
  public static final int TYPE_ENTERPRISE = 1;
  public static final int TYPE_LIGHT = 2;
  private String macId;
  private String serial;
  private String version;
  private int vcsType;
  private int maxMembers;
  private int maxCalls;
  private boolean resources;
  private boolean scheduler;
  private boolean nonRV;
  private boolean outlook;
  private int licenseDays;
  private int evaluationCount;
  private boolean demo;
  private boolean verified;
  private boolean imFirst;
  private int daysLeft;
  private int p2pCalls;
  private boolean specialp2p;
  private int nmsTerminals;
  private boolean specialnms;
  private int mobileLicense;
  private boolean specialMobile;
  private boolean badMac;

  public String getMacId()
  {
    return this.macId;
  }

  public void setMacId(String macId)
  {
    this.macId = macId;
  }

  public String getSerial()
  {
    return this.serial;
  }

  public void setSerial(String serial)
  {
    this.serial = serial;
  }

  public String getVersion()
  {
    return this.version;
  }

  public void setVersion(String version)
  {
    this.version = version;
  }

  public int getVcsType()
  {
    return this.vcsType;
  }

  public void setVcsType(int vcsType)
  {
    this.vcsType = vcsType;
  }

  public int getMaxMembers()
  {
    return this.maxMembers;
  }

  public void setMaxMembers(int maxMembers)
  {
    this.maxMembers = maxMembers;
  }

  public int getMaxCalls()
  {
    return this.maxCalls;
  }

  public void setMaxCalls(int maxCalls)
  {
    this.maxCalls = maxCalls;
  }

  public boolean isResources()
  {
    return this.resources;
  }

  public void setResources(boolean resources)
  {
    this.resources = resources;
  }

  public boolean isScheduler()
  {
    return this.scheduler;
  }

  public void setScheduler(boolean scheduler)
  {
    this.scheduler = scheduler;
  }

  public boolean isNonRV()
  {
    return this.nonRV;
  }

  public void setNonRV(boolean nonRV)
  {
    this.nonRV = nonRV;
  }

  public int getLicenseDays()
  {
    return this.licenseDays;
  }

  public void setLicenseDays(int licenseDays)
  {
    this.licenseDays = licenseDays;
  }

  public int getEvaluationCount()
  {
    return this.evaluationCount;
  }

  public void setEvaluationCount(int evaluationCount)
  {
    this.evaluationCount = evaluationCount;
  }

  public boolean isDemo()
  {
    return this.demo;
  }

  public void setDemo(boolean demo)
  {
    this.demo = demo;
  }

  public boolean isVerified()
  {
    return this.verified;
  }

  public void setVerified(boolean verified)
  {
    this.verified = verified;
  }

  public boolean isOutlook()
  {
    return this.outlook;
  }

  public void setOutlook(boolean outlook)
  {
    this.outlook = outlook;
  }

  public int getP2pCalls() {
    return this.p2pCalls;
  }

  public void setP2pCalls(int p2pCalls) {
    this.p2pCalls = p2pCalls;
  }

  public boolean isSpecialp2p() {
    return this.specialp2p;
  }

  public void setSpecialp2p(boolean specialp2p) {
    this.specialp2p = specialp2p;
  }

  public int getNmsTerminals() {
    return this.nmsTerminals;
  }

  public void setNmsTerminals(int nmsTerminals) {
    this.nmsTerminals = nmsTerminals;
  }

  public boolean isSpecialnms() {
    return this.specialnms;
  }

  public void setSpecialnms(boolean specialnms) {
    this.specialnms = specialnms;
  }

  public int getMobileLicense() {
    return this.mobileLicense;
  }

  public void setMobileLicense(int mobileLicense) {
    this.mobileLicense = mobileLicense;
  }

  public boolean isSpecialMobile() {
    return this.specialMobile;
  }

  public void setSpecialMobile(boolean specialMobile) {
    this.specialMobile = specialMobile;
  }

  public boolean equals(Object other)
  {
    return (other instanceof LicenseInfo) ? equals((LicenseInfo)other, true) : false;
  }

  public boolean equals(LicenseInfo other, boolean withVerified)
  {
    return (this.vcsType == other.vcsType) && (this.maxMembers == other.maxMembers) && (this.maxCalls == other.maxCalls) && (this.resources == other.resources) && (this.scheduler == other.scheduler) && (this.nonRV == other.nonRV) && (this.outlook == other.outlook) && (this.licenseDays == other.licenseDays) && (this.evaluationCount == other.evaluationCount) && (this.demo == other.demo) && (equals(this.macId, other.macId)) && (equals(this.serial, other.serial)) && (equals(this.version, other.version)) && ((!withVerified) || (this.verified == other.verified));
  }

  private boolean equals(String s1, String s2)
  {
    if (s1 == null)
      return s2 == null;
    return s1.equalsIgnoreCase(s2);
  }

  public int getDaysLeft()
  {
    return this.daysLeft;
  }

  public void setDaysLeft(int daysLeft)
  {
    this.daysLeft = daysLeft;
  }

  public boolean isImFirst()
  {
    return this.imFirst;
  }

  public void setImFirst(boolean imFirst)
  {
    this.imFirst = imFirst;
  }

  public boolean isBadMac()
  {
    return this.badMac;
  }

  public void setBadMac(boolean badMac)
  {
    this.badMac = badMac;
  }
}