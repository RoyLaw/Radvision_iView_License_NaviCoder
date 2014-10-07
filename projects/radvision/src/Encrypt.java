package com.radvision.nms.serverutil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

public class Encrypt
{
  public static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  public static byte[] toBytes(String s)
  {
    char[] ca = s.toCharArray();
    byte[] ba = new byte[ca.length * 2];
    for (int i = 0; i < ca.length; i++)
    {
      ba[(2 * i)] = ((byte)(ca[i] >> '\b' & 0xFF));
      ba[(2 * i + 1)] = ((byte)(ca[i] & 0xFF));
    }
    return ba;
  }

  public static String toString(byte[] ba)
  {
    char[] ca = new char[ba.length / 2];
    for (int i = 0; i < ca.length; i++)
      ca[i] = ((char)(ba[(2 * i)] << 8 & 0xFF00 | ba[(2 * i + 1)] & 0xFF));
    return new String(ca);
  }

  public static String toHexString(byte[] ba)
  {
    char[] ca = new char[ba.length * 2];
    for (int i = 0; i < ba.length; i++)
    {
      ca[(i * 2)] = HEX_CHARS[(ba[i] >> 4 & 0xF)];
      ca[(i * 2 + 1)] = HEX_CHARS[(ba[i] & 0xF)];
    }
    return new String(ca);
  }

  public static byte[] fromHexString(String s)
  {
    byte[] ba = new byte[s.length() / 2];
    for (int i = 0; i < ba.length; i++)
      ba[i] = fromHex(s.charAt(2 * i), s.charAt(2 * i + 1));
    return ba;
  }

  private static byte fromHex(char cup, char clo)
  {
    return (byte)(fromHex(cup) << 4 | fromHex(clo));
  }

  private static int fromHex(char c)
  {
    if (c >= 'a')
      return c - 'a' + 10;
    if (c >= 'A')
      return c - 'A' + 10;
    return c - '0';
  }

  public static byte[] toGzip(byte[] ba)
  {
    try
    {
      ByteArrayOutputStream baout = new ByteArrayOutputStream();
      GZIPOutputStream gout = new GZIPOutputStream(baout);
      gout.write(ba);
      gout.flush();
      gout.close();
      return baout.toByteArray();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }return ba;
  }

  public static byte[] fromGzip(byte[] ba)
  {
    try
    {
      ByteArrayOutputStream baout = new ByteArrayOutputStream();
      ByteArrayInputStream bain = new ByteArrayInputStream(ba);
      GZIPInputStream gin = new GZIPInputStream(bain);
      int r;
      while ((r = gin.read()) != -1)
        baout.write(r);
      gin.close();
      baout.flush();
      baout.close();
      return baout.toByteArray();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }return ba;
  }

  public static byte[] toHuffman(byte[] ba)
  {
    try
    {
      ByteArrayOutputStream baout = new ByteArrayOutputStream();
      DeflaterOutputStream hout = new DeflaterOutputStream(baout, new Deflater(2));
      hout.write(ba);
      hout.flush();
      hout.close();
      return baout.toByteArray();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }return ba;
  }

  public static byte[] fromHuffman(byte[] ba)
  {
    try
    {
      ByteArrayOutputStream baout = new ByteArrayOutputStream();
      ByteArrayInputStream bain = new ByteArrayInputStream(ba);
      InflaterInputStream iin = new InflaterInputStream(bain);
      int r;
      while ((r = iin.read()) != -1)
        baout.write(r);
      iin.close();
      baout.flush();
      baout.close();
      return baout.toByteArray();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }return ba;
  }

  public static String toMD5(String s)
  {
    try
    {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(toBytes(s));
      return toHexString(md5.digest());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }return null;
  }

  public static byte[] toMD5(byte[] ba)
  {
    try
    {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(ba);
      return md5.digest();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }return null;
  }

  public static String toXOR(String s, byte[] key)
  {
    if ((s == null) || (s.length() == 0)) {
      return s;
    }
    byte[] sb = toBytes(s);
    int c = 0;
    for (int i = 0; i < sb.length; i++)
      sb[i] = ((byte)(sb[i] ^ key[(++c % key.length)]));
    return toHexString(sb);
  }

  public static byte[] xor(byte[] ba, byte[] key)
  {
    byte[] rba = new byte[ba.length];

    int c = 0;
    if (key.length == 0)
      System.arraycopy(ba, 0, rba, 0, ba.length);
    else {
      for (int i = 0; i < ba.length; i++)
        rba[i] = ((byte)(ba[i] ^ key[(++c % key.length)]));
    }
    return rba;
  }

  public static String fromXOR(String s, byte[] key)
  {
    if ((s == null) || (s.length() == 0)) {
      return s;
    }
    try
    {
      byte[] sb = fromHexString(s);
      int c = 0;
      for (int i = 0; i < sb.length; i++)
        sb[i] = ((byte)(sb[i] ^ key[(++c % key.length)]));
      return toString(sb);
    }
    catch (Exception e) {
    }
    return s;
  }

  public static Object toObject(byte[] ba)
  {
    try
    {
      return new ObjectInputStream(new ByteArrayInputStream(ba)).readObject();
    }
    catch (Exception e) {
    }
    return null;
  }

  public static byte[] fromObject(Object object)
  {
    try
    {
      ByteArrayOutputStream baout = new ByteArrayOutputStream();
      ObjectOutputStream oout = new ObjectOutputStream(baout);
      oout.writeObject(object);
      oout.flush();
      oout.close();
      return baout.toByteArray();
    }
    catch (Exception e) {
    }
    return new byte[0];
  }

  public static byte[] sign(byte[] ba, PrivateKey key)
  {
    try
    {
      Signature signer = Signature.getInstance("DSA");
      signer.initSign(key);
      signer.update(ba);
      return signer.sign();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }return new byte[0];
  }

  public static boolean verify(byte[] ba, byte[] signature, PublicKey key)
  {
    try
    {
      Signature signer = Signature.getInstance("DSA");
      signer.initVerify(key);
      signer.update(ba);
      return signer.verify(signature);
    }
    catch (Exception e) {
    }
    return false;
  }
}