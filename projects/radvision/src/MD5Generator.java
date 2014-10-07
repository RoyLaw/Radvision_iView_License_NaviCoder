package com.radvision.vcs.license;

import com.radvision.nms.serverutil.Encrypt;

public class MD5Generator
  implements SignatureGenerator
{
  private final int len;

  public MD5Generator()
  {
    this(0); } 
  public MD5Generator(int len) { this.len = len; }


  public byte[] sign(byte[] data)
  {
    if (this.len <= 0) {
      return Encrypt.toMD5(data);
    }
    byte[] rv = new byte[this.len];
    byte[] md5 = Encrypt.toMD5(data);
    System.arraycopy(md5, 0, rv, 0, Math.min(md5.length, rv.length));
    return rv;
  }
}