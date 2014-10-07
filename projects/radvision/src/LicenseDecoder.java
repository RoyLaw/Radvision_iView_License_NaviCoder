package com.radvision.vcs.license;

import com.radvision.nms.serverutil.Base64;
import com.radvision.nms.serverutil.Encrypt;
import java.io.PrintStream;

public class LicenseDecoder
{
  public static final int CUSTOMER_FLD_LEN = 5;
  public static final int LICENSE_BIT_LENGTH = 240;
  public static final int DEMO_CALLS_LIMIT = 30;
  public static final char MAGIC_DEMO = 'h';
  public static final char MAGIC_FULL = 'i';
  private SignatureValidator validator;

  public LicenseDecoder(SignatureValidator validator)
  {
    this.validator = validator;
  }

  public License decode(String slicense)
  {
    if ((slicense == null) || (slicense.length() == 0))
      throw new IllegalArgumentException("license parse failure: 1");
    if ((slicense.charAt(0) != 'h') && (slicense.charAt(0) != 'i')) {
      throw new IllegalArgumentException("license parse failure: 2");
    }
    byte[] data = Base64.decode(pad(slicense.substring(1)));

    byte[] licensedata = new byte[data[0] - 1];
    byte[] checkdata = new byte[data.length - data[0]];

    System.arraycopy(data, 1, licensedata, 0, licensedata.length);
    System.arraycopy(data, data[0], checkdata, 0, checkdata.length);

    License license = decode(licensedata);
    license.setVerified(((slicense.charAt(0) == 'h') == license.isDemo()) && (this.validator.validate(slicense.charAt(0), licensedata, checkdata)));
    return license;
  }

  public static String pad(String s)
  {
    if (s.length() % 4 == 0)
      return s;
    StringBuffer sb = new StringBuffer(s.length() + 4);
    sb.append(s);
    for (int i = s.length() % 4; i < 4; i++)
      sb.append("=");
    return sb.toString();
  }

  private License decode(byte[] licensedata)
  {
    License license = new License();
    BitDecoder bits = new BitDecoder(licensedata);

    byte[] bmac = new byte[6];
    for (int i = 0; i < 6; i++)
      bmac[i] = ((byte)bits.read(8));
    license.setMacId(Encrypt.toHexString(bmac));

    StringBuffer sb = new StringBuffer();
    sb.append("0");
    sb.append(3 + bits.read(2));
    int week = bits.read(7);
    if (week < 10)
      sb.append("0");
    sb.append(week);
    sb.append("070");
    int build = bits.read(10);
    if (build < 10) sb.append("0");
    if (build < 100) sb.append("0");
    sb.append(build);
    license.setSerial(sb.toString());

    int vmajor = bits.read(3);
    int vminor = bits.read(5);
    int vpatch = bits.read(5);
    int vbuild = bits.read(10);

    license.setVcsType(bits.read(1));
    license.setMaxMembers(bits.read(8));
    license.setMaxCalls(bits.read(16));
    license.setResources(bits.readBoolean());
    license.setScheduler(bits.readBoolean());
    license.setNonRV(bits.readBoolean());
    license.setLicenseDays(bits.read(7));
    license.setEvaluationCount(bits.read(3));
    license.setDemo(bits.readBoolean());
    license.setOutlook(bits.readBoolean());
    license.setImFirst(bits.readBoolean());
    if (bits.readBoolean()) {
      license.setVcsType(2);
    }
    license.setCustom(bits.read(4));

    int year = bits.read(8);
    if (year != 0)
    {
      year -= 100;
      String strY = new Integer(year).toString();
      if (strY.length() < 2) strY = "0" + strY;
      String serial = license.getSerial();
      serial = strY + serial.substring(2);
      license.setSerial(serial);
    }

    sb = new StringBuffer();
    sb.append(vmajor);
    sb.append(".");
    sb.append(vminor);
    sb.append(".");
    sb.append(vpatch);
    sb.append(".");

    int maxSeats = 0;
    int maxAudioCalls = 0;
    int maxMCUs = 0;
    int maxMVPs = 0;

    if (vmajor >= 5)
    {
      int len = bits.read(3);
      if (len == 0) {
        sb.append('0');
      }
      else {
        int loop = 0;
        for (loop = 0; loop < len; loop++)
        {
          char c = (char)bits.read(8);
          sb.append(c);
        }

        for (int j = loop; j < 5; j++)
          bits.read(8);
      }
      sb.append(".");

      maxSeats = bits.read(32);
      maxAudioCalls = bits.read(32);
      maxMCUs = bits.read(8);
      maxMVPs = bits.read(8);

      int productFamily = bits.read(10);
      if (productFamily != 0)
      {
        String strPF = new Integer(productFamily).toString();
        if (productFamily < 100)
          strPF = "0" + strPF;
        String ser = license.getSerial();
        String serial = ser.substring(0, 4) + strPF + ser.substring(7, 10);
        license.setSerial(serial);
      }

    }

    if (maxSeats == 0)
      license.setMaxSeats(license.getMaxCalls());
    else {
      license.setMaxSeats(maxSeats);
    }
    if (maxAudioCalls == 0)
      license.setMaxAudioCalls(license.getMaxCalls());
    else {
      license.setMaxAudioCalls(maxAudioCalls);
    }
    if (maxMCUs != 0)
      license.setMaxMCUs(maxMCUs);
    if (maxMVPs != 0) {
      license.setMaxMVPs(maxMVPs);
    }
    sb.append(vbuild);
    license.setVersion(sb.toString());
    license.setNotes(bits.readBoolean());

    return license;
  }

  public static void main(String[] test) throws Exception
  {
    LicenseDecoder decoder = new LicenseDecoder(new MD5Validator(6));

    System.out.println(decoder.decode("hIQAAAAAAAAECaAEBDABxOiADZIEBAAAAADUMAAA1DAAAVMzVRtYU").getMaxMCUs());
  }
}