package com.lxl.mylibrary;

import android.util.Log;
import java.util.ArrayList;

public class RouterManager {

  private static ArrayList<IRouter> ROUTERS = new ArrayList<>();
  private static ArrayList<String> strings = new ArrayList<>();

  public RouterManager() {
    Log.e("lxl", "size = " + ROUTERS);
  }


  public static void init() {

  }

  static void registerRouter(IRouter router) {
    if (ROUTERS != null || !ROUTERS.contains(router)) {
      ROUTERS.add(router);
    }
  }

  static void registerRouter(String router) {
    if (strings != null || !strings.contains(router)) {
      strings.add(router);
    }
  }

  public static ArrayList<IRouter> getRouters() {
    return ROUTERS;
  }

  public static ArrayList<String> getStrings() {
    return strings;
  }
}
