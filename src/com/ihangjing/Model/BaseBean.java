package com.ihangjing.Model;

public abstract class BaseBean
  implements BeanStringBridge
{
  public String getCacheKey()
  {
    return "-1";
  }
}
