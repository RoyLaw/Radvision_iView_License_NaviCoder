package com.radvision.vcs.license;

import java.util.BitSet;

public class BitDecoder
{
  private byte[] data;
  private BitSet bits;
  private int done;

  public BitDecoder(byte[] data)
  {
    this.data = data;
  }

  public int read(int l)
  {
    ensureBS();

    int val = 0;
    int mask = 1;
    for (int i = 0; i < l; i++)
    {
      if (this.bits.get(this.done++))
        val |= mask;
      mask <<= 1;
    }
    return val;
  }
  public boolean readBoolean() {
    return read(1) != 0;
  }

  private void ensureBS() {
    if (this.bits != null) {
      return;
    }
    this.bits = new BitSet(this.data.length * 8 + 1);
    int r = 0;
    for (int i = 0; i < this.data.length; i++)
    {
      int d = this.data[i] & 0xFF;
      for (int j = 0; j < 8; j++)
      {
        bits_set(r++, (d & 0x1) == 1);
        d >>>= 1;
      }
    }
  }

  private void bits_set(int bitIndex, boolean value)
  {
    if (value)
      this.bits.set(bitIndex);
    else
      this.bits.clear(bitIndex);
  }
}