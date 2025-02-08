package cc.gilmore.audioplayer;

import android.os.Bundle;

public class MainApplication extends android.app.Application {
    static {
        System.out.println("MyApplication: Static initializer block");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MyApplication: onCreate");
    }
}
