package com.radvision.vcs.license;

import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

public class LicenseResourceBundle
{
  private static Hashtable rbmap = new Hashtable();
  private static String LICENSE_RES = "rv-utilbiz";

  public static String getString(String key, Locale locale)
  {
    ResourceBundle rb = (ResourceBundle)rbmap.get(locale);
    if (rb == null)
    {
      rb = ResourceBundle.getBundle(LICENSE_RES, locale);
      rbmap.put(locale, rb);
    }
    try
    {
      return rb.getString(key);
    } catch (Throwable e) {
    }
    return key;
  }
}