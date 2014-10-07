package com.radvision.vcs.license;

public abstract interface SignatureValidator
{
  public abstract boolean validate(char paramChar, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
}