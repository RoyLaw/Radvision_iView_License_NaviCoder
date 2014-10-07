package com.radvision.vcs.license;

import com.radvision.nms.serverutil.Encrypt;

public class MD5Validator
  implements SignatureValidator
{
  private final int len;

  public MD5Validator()
  {
    this(0); } 
  public MD5Validator(int len) { this.len = len; }

  public boolean validate(char magic, byte[] data, byte[] signature)
  {
    byte[] resigned = Encrypt.toMD5(data);
    if (this.len <= 0)
    {
      if (signature.length != resigned.length)
        return false;
      for (int i = 0; i < signature.length; i++)
        if (signature[i] != resigned[i])
          return false;
    }
    else
    {
      if (this.len != signature.length) {
        return false;
      }
      int i = 0;
      int llen = Math.min(resigned.length, this.len);
      for (; i < llen; i++)
        if (signature[i] != resigned[i])
          return false;
      for (; i < this.len; i++)
        if (signature[i] != 0)
          return false;
    }
    return true;
  }
}