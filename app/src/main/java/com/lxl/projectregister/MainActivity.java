package com.lxl.projectregister;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewStub;
import com.lxl.log.ILog;
import com.lxl.mylibrary.IRouter;
import com.lxl.mylibrary.RouterManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewStub viewStub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        ArrayList<IRouter> al = new RouterManager().getRouters();
//        for (IRouter router : al) {
//            Log.e("lxl", "al = " + router.name());
//        }
        RouterManager.init();
        ArrayList<String> al = RouterManager.getStrings();
        for (String router : al) {
            Log.e("lxl", "RouterManager = " + router);
        }

        ArrayList<IRouter> routers = RouterManager.getRouters();
        for (IRouter router : routers) {
            Log.e("lxl", "RouterManager11 = " + router);
        }

        ArrayList<ILog> al1 = new LogManager().getLogs();
        for (ILog log : al1) {
            Log.e("lxl", "al = " + log.getName());
        }

        ArrayList<String> al2 = new LogManager().logStr;
        for (String log : al2) {
            Log.e("lxl", "alstr = " + log);
        }

    }
}
