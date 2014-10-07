package com.radvision.vcs.license;

public class License
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
  private boolean imFirst;
  private int custom;
  private int maxSeats;
  private int maxAudioCalls;
  private int maxMCUs;
  private int maxMVPs;
  private boolean verified;
  private boolean notes;

  public String getMacId()
  {
    return this.macId; } 
  public void setMacId(String macId) { this.macId = macId; } 
  public String getSerial() { return this.serial; } 
  public void setSerial(String serial) { this.serial = serial; } 
  public String getVersion() { return this.version; } 
  public void setVersion(String version) { this.version = version; } 
  public int getVcsType() { return this.vcsType; } 
  public void setVcsType(int vcsType) { this.vcsType = vcsType; } 
  public int getMaxMembers() { return this.maxMembers; } 
  public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; } 
  public int getMaxCalls() { return this.maxCalls; } 
  public void setMaxCalls(int maxCalls) { this.maxCalls = maxCalls; } 
  public boolean isResources() { return this.resources; } 
  public void setResources(boolean resources) { this.resources = resources; } 
  public boolean isScheduler() { return this.scheduler; } 
  public void setScheduler(boolean scheduler) { this.scheduler = scheduler; } 
  public boolean isNonRV() { return this.nonRV; } 
  public void setNonRV(boolean nonRV) { this.nonRV = nonRV; } 
  public int getLicenseDays() { return this.licenseDays; } 
  public void setLicenseDays(int licenseDays) { this.licenseDays = licenseDays; } 
  public int getEvaluationCount() { return this.evaluationCount; } 
  public void setEvaluationCount(int evaluationCount) { this.evaluationCount = evaluationCount; } 
  public boolean isDemo() { return this.demo; } 
  public void setDemo(boolean demo) { this.demo = demo; } 
  public boolean isVerified() { return this.verified; } 
  public void setVerified(boolean verified) { this.verified = verified; } 
  public boolean isOutlook() { return this.outlook; } 
  public void setOutlook(boolean outlook) { this.outlook = outlook; } 
  public boolean isNotes() { return this.notes; } 
  public void setNotes(boolean notes) { this.notes = notes; } 
  public boolean isImFirst() { return this.imFirst; } 
  public void setImFirst(boolean imFirst) { this.imFirst = imFirst; } 
  public int getCustom() { return this.custom; } 
  public void setCustom(int custom) { this.custom = custom; }

  public int getElementLimit() {
    int result = getMaxCalls() / 5;
    if (result < 10) {
      return 5;
    }
    int i = 2;
    while (i * 5 <= result) {
      i++;
    }
    return (i - 1) * 5;
  }

  public int getMaxAudioCalls()
  {
    return this.maxAudioCalls;
  }

  public void setMaxAudioCalls(int maxAudioCalls)
  {
    this.maxAudioCalls = maxAudioCalls;
  }

  public int getMaxSeats()
  {
    return this.maxSeats;
  }

  public void setMaxSeats(int maxSeats)
  {
    this.maxSeats = maxSeats;
  }

  public int getMaxMCUs()
  {
    return this.maxMCUs;
  }

  public void setMaxMCUs(int maxMCUs)
  {
    this.maxMCUs = maxMCUs;
  }

  public int getMaxMVPs()
  {
    return this.maxMVPs;
  }

  public void setMaxMVPs(int maxMVPs)
  {
    this.maxMVPs = maxMVPs;
  }

  public boolean equals(Object other)
  {
    return (other instanceof License) ? equals((License)other, true) : false;
  }

  public boolean equals(License other, boolean withVerified)
  {
    return (this.vcsType == other.vcsType) && (this.maxMembers == other.maxMembers) && (this.maxCalls == other.maxCalls) && (this.maxSeats == other.maxSeats) && (this.maxAudioCalls == other.maxAudioCalls) && (this.maxMCUs == other.maxMCUs) && (this.maxMVPs == other.maxMVPs) && (this.resources == other.resources) && (this.scheduler == other.scheduler) && (this.nonRV == other.nonRV) && (this.outlook == other.outlook) && (this.notes == other.notes) && (this.licenseDays == other.licenseDays) && (this.evaluationCount == other.evaluationCount) && (this.demo == other.demo) && (this.imFirst == other.imFirst) && (equals(this.macId, other.macId)) && (equals(this.serial, other.serial)) && (equals(this.version, other.version)) && ((!withVerified) || (this.verified == other.verified));
  }

  private boolean equals(String s1, String s2)
  {
    if (s1 == null)
      return s2 == null;
    return s1.equalsIgnoreCase(s2);
  }

  public String getLicensType() {
    StringBuilder result = new StringBuilder();
    result.append(System.getProperty("vendor.license.name"));
    if ((this.custom == 1) || (this.custom == 0)) {
      result.append(" the other license type");
    }
    else if ((this.custom == 2) || (this.custom == 3) || (this.custom == 6) || (this.custom == 7) || (this.custom == 8))
      result.append(" " + String.valueOf(this.maxCalls) + " with Gatekeeper");
    else if ((this.custom == 4) || (this.custom == 9) || (this.custom == 10) || (this.custom == 11))
    {
      if (this.vcsType == 0) result.append(" " + String.valueOf(this.maxCalls) + " with multi-tenant support"); else
        result.append(" " + String.valueOf(this.maxCalls));
    }
    else if (this.custom == 5) {
      result.append(" additional 25");
    }
    return result.toString();
  }
}