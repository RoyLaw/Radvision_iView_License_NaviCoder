package com.radvision.vcs.license.history;

import com.radvision.nms.serverutil.Base64;
import com.radvision.nms.serverutil.Encrypt;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class LicenseUsageObfuscator
{
  public String obf1(LicenseUsage lu)
  {
    return obf1(new LicenseUsage[] { lu }); } 
  public String obf2(LicenseUsage lu) { return obf2(new LicenseUsage[] { lu }); }

  public String obf1(LicenseUsage[] lua)
  {
    byte[] b = lua2b(lua);
    spin(b, 5);
    xor(b);
    spin(b, 13);
    return Base64.encode(b);
  }

  public String obf2(LicenseUsage[] lua)
  {
    byte[] b = lua2b(lua);
    spin(b, 11);
    xor(b);
    spin(b, 7);
    return Base64.encode(b);
  }

  public LicenseUsage[] obf1(String s)
  {
    byte[] b = Base64.decode(s);
    spin(b, 13);
    rox(b);
    spin(b, 5);
    return b2lua(b);
  }

  public LicenseUsage[] obf2(String s)
  {
    byte[] b = Base64.decode(s);
    spin(b, 7);
    rox(b);
    spin(b, 11);
    return b2lua(b);
  }

  private byte[] lua2b(LicenseUsage[] lua)
  {
    try
    {
      ByteArrayOutputStream baout = new ByteArrayOutputStream();
      OutputStream out = new BufferedOutputStream(baout);
      out.write(l2b(lua.length));
      for (int i = 0; i < lua.length; i++)
      {
        byte[] lub = lu2b(lua[i]);
        out.write(l2b(lub.length));
        out.write(lub);
      }
      out.flush();
      out.close();
      return baout.toByteArray();
    }
    catch (Exception e)
    {
      throw new RuntimeException("unexpected exception", e);
    }
  }

  private LicenseUsage[] b2lua(byte[] b)
  {
    try
    {
      ByteArrayInputStream in = new ByteArrayInputStream(b);
      byte[] bl = new byte[8];

      in.read(bl);
      LicenseUsage[] lua = new LicenseUsage[(int)b2l(bl, 0)];
      for (int i = 0; i < lua.length; i++)
      {
        in.read(bl);
        byte[] lub = new byte[(int)b2l(bl, 0)];
        in.read(lub);
        lua[i] = b2lu(lub);
      }
      in.close();
      return lua;
    }
    catch (Exception e)
    {
      throw new RuntimeException("unexpected exception", e);
    }
  }

  private byte[] xor(byte[] b)
  {
    byte prev = b[(b.length - 1)];
    for (int i = b.length - 2; i >= 0; i--)
    {
      byte next = b[i];
      int tmp23_22 = i;
      byte[] tmp23_21 = b; tmp23_21[tmp23_22] = ((byte)(tmp23_21[tmp23_22] ^ prev));
      prev = next;
    }
    return b;
  }

  private byte[] rox(byte[] b)
  {
    for (int i = b.length - 2; i >= 0; i--)
    {
      int tmp11_10 = i;
      byte[] tmp11_9 = b; tmp11_9[tmp11_10] = ((byte)(tmp11_9[tmp11_10] ^ b[(i + 1)]));
    }return b;
  }

  public String xor(String s)
  {
    return Encrypt.toHexString(spin(xor(Encrypt.toBytes(s)), 5));
  }

  public String rox(String s)
  {
    return Encrypt.toString(rox(spin(Encrypt.fromHexString(s), 5)));
  }

  private byte[] spin(byte[] b, int len)
  {
    int bl = b.length - len;
    byte[] swap = new byte[len];

    for (int i = 0; i < bl; i += len)
    {
      for (int j = 0; j < len; j++)
        swap[(len - j - 1)] = b[(i + j)];
      for (int j = 0; j < len; j++)
        b[(i + j)] = swap[j];
    }
    return b;
  }

  private LicenseUsage b2lu(byte[] b)
  {
    LicenseUsage lu = new LicenseUsage();
    lu.setTimeInstalled(b2l(b, 0));
    lu.setTimeExpired(b2l(b, 8));
    char magic = (char)(int)b2l(b, 16);
    byte[] b64 = new byte[b.length - 24];
    System.arraycopy(b, 24, b64, 0, b64.length);
    String s = Base64.encode(b64);
    lu.setLicense(magic + s);
    return lu;
  }

  private byte[] lu2b(LicenseUsage lu)
  {
    byte[] b64 = Base64.decode(lu.getLicense().substring(1));
    byte[] magic = l2b(lu.getLicense().charAt(0));
    byte[] inst = l2b(lu.getTimeInstalled());
    byte[] expired = l2b(lu.getTimeExpired());

    byte[] full = new byte[b64.length + 24];
    System.arraycopy(inst, 0, full, 0, 8);
    System.arraycopy(expired, 0, full, 8, 8);
    System.arraycopy(magic, 0, full, 16, 8);
    System.arraycopy(b64, 0, full, 24, b64.length);
    return full;
  }

  private byte[] l2b(long l)
  {
    byte[] ba = new byte[8];
    long ltemp = l;
    for (int i = 0; i < 8; i++)
    {
      ba[i] = ((byte)(int)(ltemp & 0xFF));
      ltemp >>= 8;
    }
    return ba;
  }

  private long b2l(byte[] b, int off)
  {
    long ltemp = 0L;
    for (int i = 0; i < 8; i++)
    {
      ltemp <<= 8;
      ltemp += (b[(off + 7 - i)] & 0xFF);
    }
    return ltemp;
  }

  public static void main(String[] test)
  {
    LicenseUsageObfuscator obs = new LicenseUsageObfuscator();
    System.out.println(obs.obf1(new LicenseUsage[0]));
    System.out.println(obs.obf2(new LicenseUsage[0]));
    System.out.println(obs.obf1("AAAAAAAAAAA=").length);
  }
}