package com.radvision.vcs.license.keys;

import com.radvision.nms.serverutil.Base64;
import com.radvision.vcs.license.SignatureValidator;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class PublicValidator
  implements SignatureValidator
{
  private final String publicKey64;
  private PublicKey publicKey;

  public PublicValidator(String publicKey64)
  {
    this.publicKey64 = publicKey64;
  }

  private void init() throws Exception
  {
    if (this.publicKey != null) {
      return;
    }
    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decode(this.publicKey64));
    KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
    this.publicKey = keyFactory.generatePublic(pubKeySpec);
  }

  public boolean validate(char magic, byte[] data, byte[] signBytes)
  {
    try
    {
      init();

      Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
      signature.initVerify(this.publicKey);
      signature.update(data, 0, data.length);
      return signature.verify(signBytes);
    }
    catch (Exception e)
    {
      throw new RuntimeException("unexpected exception in key validation", e);
    }
  }
}