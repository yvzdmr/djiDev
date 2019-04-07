package com.vrm.djiplugin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;

public class DJIPluginManager {

    interface RegisterCallback {
        public void OnRegisterSuccess();
        public  void OnRegisterFail(String message);
    }

    private static final DJIPluginManager ourInstance = new DJIPluginManager();

    public static DJIPluginManager getInstance() {
        return ourInstance;
    }

    Context appContext;
    RegisterCallback registerCallback;

    private DJIPluginManager() {

    }

    public void Register(Context context, RegisterCallback callback){
        appContext = context;
        registerCallback = callback;
        DJISDKManager.getInstance().registerApp(context, new DJISDKManager.SDKManagerCallback() {
            @Override
            public void onRegister(DJIError djiError) {
                if(djiError == DJISDKError.REGISTRATION_SUCCESS){
                    registerCallback.OnRegisterSuccess();
                    Toast.makeText(appContext, "Register Success", Toast.LENGTH_SHORT);
                }
                else{
                    registerCallback.OnRegisterFail(djiError.getDescription());
                    Toast.makeText(appContext, "Register Failed", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onProductDisconnect() {

            }

            @Override
            public void onProductConnect(BaseProduct baseProduct) {

            }

            @Override
            public void onComponentChange(BaseProduct.ComponentKey componentKey, BaseComponent baseComponent, BaseComponent baseComponent1) {

            }
        });
    }


}
