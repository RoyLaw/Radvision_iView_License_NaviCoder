package com.radvision.vcs.license;

import java.util.BitSet;

public class BitEncoder
{
  private int length;
  private BitSet bits;

  public BitEncoder(int blen)
  {
    this.bits = new BitSet(blen);
  }

  public void add(int d, int l)
  {
    int dd = d;
    for (int i = 0; i < l; i++)
    {
      bits_set(this.length++, (dd & 0x1) == 1);
      dd >>>= 1;
    }
  }

  private void bits_set(int bitIndex, boolean value)
  {
    if (value)
      this.bits.set(bitIndex);
    else
      this.bits.clear(bitIndex);
  }

  public void add(boolean b)
  {
    add(b ? 1 : 0, 1);
  }

  public byte[] getBytes()
  {
    byte[] data = this.length % 8 == 0 ? new byte[this.length / 8] : new byte[this.length / 8 + 1];
    byte mask = 0;
    for (int i = 0; i < this.length; i++)
    {
      if (i % 8 == 0)
        mask = 1;
      else {
        mask = (byte)(mask << 1);
      }
      if (this.bits.get(i))
      {
        int array = i / 8;
        int tmp86_84 = array;
        byte[] tmp86_83 = data; tmp86_83[tmp86_84] = ((byte)(tmp86_83[tmp86_84] | mask));
      }
    }
    return data;
  }
}