package com.radvision.vcs.license;

import com.radvision.nms.serverutil.Base64;
import com.radvision.nms.serverutil.Encrypt;
import java.util.StringTokenizer;

public class LicenseEncoder
{
  public static final char MAGIC_DEMO = 'h';
  public static final char MAGIC_FULL = 'i';
  private SignatureGenerator signer;

  public LicenseEncoder(SignatureGenerator signer)
  {
    this.signer = signer;
  }

  public String encode(License license)
  {
    byte[] licensedata = encodeDetails(license);
    byte[] checkdata = this.signer.sign(licensedata);
    byte[] full = new byte[licensedata.length + checkdata.length + 1];

    full[0] = ((byte)(licensedata.length + 1));
    System.arraycopy(licensedata, 0, full, 1, licensedata.length);
    System.arraycopy(checkdata, 0, full, licensedata.length + 1, checkdata.length);

    return (license.isDemo() ? 'h' : 'i') + Base64.encode(full);
  }

  private byte[] encodeDetails(License license)
  {
    BitEncoder bits = new BitEncoder(240);

    byte[] bmac = Encrypt.fromHexString(license.getMacId());
    for (int i = 0; i < 6; i++) {
      bits.add(bmac[i], 8);
    }

    int year = 100 + (license.getSerial().charAt(0) - '0') * 10 + (license.getSerial().charAt(1) - '0');

    bits.add(license.getSerial().charAt(1) - '3', 2);
    bits.add(Integer.parseInt(license.getSerial().substring(2, 4)), 7);
    bits.add(Integer.parseInt(license.getSerial().substring(7, 10)), 10);

    int productFamily = (license.getSerial().charAt(4) - '0') * 100 + (license.getSerial().charAt(5) - '0') * 10 + (license.getSerial().charAt(6) - '0');

    StringTokenizer st = new StringTokenizer(license.getVersion(), "./_- ");
    int nfield = st.countTokens();
    int major = Integer.parseInt(st.nextToken());
    int minor = Integer.parseInt(st.nextToken());
    int patch = Integer.parseInt(st.nextToken());
    int build = 0;
    String customer = null;
    if (major <= 4) {
      build = Integer.parseInt(st.nextToken());
    }
    else {
      if (nfield == 5)
        customer = st.nextToken();
      else
        customer = "0";
      build = Integer.parseInt(st.nextToken());
    }

    bits.add(major, 3);
    bits.add(minor, 5);
    bits.add(patch, 5);
    bits.add(build, 10);

    bits.add(license.getVcsType(), 1);
    bits.add(license.getMaxMembers(), 8);
    bits.add(license.getMaxCalls(), 16);
    bits.add(license.isResources());
    bits.add(license.isScheduler());
    bits.add(license.isNonRV());
    bits.add(license.getLicenseDays(), 7);
    bits.add(license.getEvaluationCount(), 3);
    bits.add(license.isDemo());
    bits.add(license.isOutlook());
    bits.add(license.isImFirst());

    bits.add(license.getVcsType() == 2);
    bits.add(license.getCustom(), 4);
    bits.add(year, 8);

    if (major >= 5)
    {
      int length = customer.length();
      if (length > 5) {
        length = 5;
      }
      bits.add(length, 3);
      int loop = 0;
      for (loop = 0; loop < length; loop++)
      {
        bits.add(customer.charAt(loop), 8);
      }

      for (int j = loop; j < 5; j++)
      {
        bits.add(0, 8);
      }

      bits.add(license.getMaxSeats(), 32);
      bits.add(license.getMaxAudioCalls(), 32);
      bits.add(license.getMaxMCUs(), 8);
      bits.add(license.getMaxMVPs(), 8);
      bits.add(productFamily, 10);
    }
    bits.add(license.isNotes());

    return bits.getBytes();
  }
}