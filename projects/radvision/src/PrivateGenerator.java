package com.radvision.vcs.license.keys;

import com.radvision.vcs.license.SignatureGenerator;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.DSAPrivateKeySpec;

public class PrivateGenerator
  implements SignatureGenerator
{
  private final String x;
  private final String p;
  private final String q;
  private final String g;
  private PrivateKey privateKey;

  public PrivateGenerator(String x, String p, String q, String g)
  {
    this.x = x;
    this.p = p;
    this.q = q;
    this.g = g;
  }

  private void init() throws Exception
  {
    if (this.privateKey != null) {
      return;
    }
    DSAPrivateKeySpec spec = new DSAPrivateKeySpec(new BigInteger(this.x), new BigInteger(this.p), new BigInteger(this.q), new BigInteger(this.g));
    KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
    this.privateKey = keyFactory.generatePrivate(spec);
  }

  public byte[] sign(byte[] data)
  {
    try
    {
      init();

      Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
      signature.initSign(this.privateKey);
      signature.update(data, 0, data.length);
      return signature.sign();
    }
    catch (Exception e)
    {
      throw new RuntimeException("unexpected failure in private key encyption", e);
    }
  }
}