package com.radvision.vcs.license.keys;

import com.radvision.nms.serverutil.Base64;
import java.io.PrintStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.DSAPrivateKeySpec;

public class KeyGenerator
{
  public static void main(String[] args)
    throws Exception
  {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
    keyGen.initialize(1024, SecureRandom.getInstance("SHA1PRNG", "SUN"));

    KeyPair pair = keyGen.generateKeyPair();

    KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
    DSAPrivateKeySpec keyspec = (DSAPrivateKeySpec)keyFactory.getKeySpec(pair.getPrivate(), DSAPrivateKeySpec.class);

    System.out.println("--- DSA private key parameters -- (x,p,q,g) ---");
    System.out.println(keyspec.getX());
    System.out.println(keyspec.getP());
    System.out.println(keyspec.getQ());
    System.out.println(keyspec.getG());
    System.out.println("--- DSA public key -- (Base64) ---");
    System.out.println(Base64.encode(pair.getPublic().getEncoded()));
  }
}