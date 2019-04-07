package com.vrm.djidemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;

import static com.vrm.djidemo.MainActivity.FLAG_CONNECTION_CHANGE;

public class DJIManager {

    public void Register(){

        DJISDKManager.getInstance().registerApp(getApplicationContext(), new DJISDKManager.SDKManagerCallback() {
            @Override
            public void onRegister(DJIError djiError) {

                if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                    showToast("Register Success");
                    DJISDKManager.getInstance().startConnectionToProduct();
                } else {
                    showToast("Register sdk fails, please check the bundle id and network connection!");
                }
                Log.v(TAG, djiError.getDescription());
            }

            @Override
            public void onProductDisconnect() {
                Log.d(TAG, "onProductDisconnect");
                showToast("Product Disconnected");
                notifyStatusChange();

            }
            @Override
            public void onProductConnect(BaseProduct baseProduct) {
                Log.d(TAG, String.format("onProductConnect newProduct:%s", baseProduct));
                showToast("Product Connected");
                notifyStatusChange();

            }
            @Override
            public void onComponentChange(BaseProduct.ComponentKey componentKey, BaseComponent
                    oldComponent,
                                          BaseComponent newComponent) {

                if (newComponent != null) {
                    newComponent.setComponentListener(new BaseComponent.ComponentListener() {

                        @Override
                        public void onConnectivityChange(boolean isConnected) {
                            Log.d(TAG, "onComponentConnectivityChanged: " + isConnected);
                            notifyStatusChange();
                        }
                    });
                }
                Log.d(TAG,
                        String.format("onComponentChange key:%s, oldComponent:%s, newComponent:%s",
                                componentKey,
                                oldComponent,
                                newComponent));

            }
        });
    }

    private void notifyStatusChange() {
        mHandler.removeCallbacks(updateRunnable);
        mHandler.postDelayed(updateRunnable, 500);
    }

    private Runnable updateRunnable = new Runnable() {

        @Override
        public void run() {
            Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
            sendBroadcast(intent);
        }
    };

    private void showToast(final String toastMsg) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        });

    }
}
