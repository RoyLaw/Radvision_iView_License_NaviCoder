package com.radvision.vcs.license;

public abstract interface SignatureGenerator
{
  public abstract byte[] sign(byte[] paramArrayOfByte);
}