package com.dome.viewer.ab;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
/**
 * 
 * @author ��� ������־
 *
 */
public abstract class IInfoCasActivity extends Activity {

    private static String Tag = "IdeaCodeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Tag, "����create");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public abstract void init();

    public abstract void refresh(Object... param);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(Tag, "����destroy");
      
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Tag, "����start");
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(Tag, "����ReStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Tag, "����resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(Tag, "����pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(Tag, "����stop");
    }
}
