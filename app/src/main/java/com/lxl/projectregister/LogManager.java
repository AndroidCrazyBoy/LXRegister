package com.lxl.projectregister;

import android.util.Log;
import com.lxl.log.ILog;
import java.util.ArrayList;

public class LogManager {

  private ArrayList<ILog> logs = new ArrayList<>();
  public ArrayList<String> logStr = new ArrayList<>();

  public LogManager() {
    init();
    Log.e("lxl", "size = " + logs.toString());
  }

  private void init() {
  }

  void register(ILog log) {
    if (logs != null || !logs.contains(log)) {
      logs.add(log);
    }
  }

  void register(String str) {
    if (logStr != null || !logStr.contains(str)) {
      logStr.add(str);
    }
  }

  public ArrayList<ILog> getLogs() {
    return logs;
  }
}
