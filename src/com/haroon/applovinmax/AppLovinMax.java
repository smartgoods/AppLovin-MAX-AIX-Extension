package com.haroon.applovinmax;

import android.app.Activity;
import android.content.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;

@UsesLibraries(libraries = "applovin-sdk.jar")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE, com.google.android.gms.permission.AD_ID")
@DesignerComponent(
        version = 2,
        description = "AppLovin MAX extension for App Inventor. SDK initialization using reflection.",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = ""
)
@SimpleObject(external = true)
public class AppLovinMax extends AndroidNonvisibleComponent {

    private final Activity activity;
    private boolean sdkInitialized = false;

    public AppLovinMax(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
    }

    @SimpleFunction(description = "Test function to check if the extension is working.")
    public void TestExtension() {
        TestSuccess("AppLovin extension loaded successfully.");
    }

    @SimpleFunction(description = "Initialize AppLovin MAX SDK using your AppLovin SDK Key.")
    public void InitializeSdk(String sdkKey) {
        try {
            final Class<?> appLovinSdkClass = Class.forName("com.applovin.sdk.AppLovinSdk");
            final Class<?> initConfigClass = Class.forName("com.applovin.sdk.AppLovinSdkInitializationConfiguration");
            final Class<?> listenerClass = Class.forName("com.applovin.sdk.AppLovinSdk$SdkInitializationListener");

            Method builderMethod = initConfigClass.getMethod("builder", String.class, Context.class);
            Object builder = builderMethod.invoke(null, sdkKey, activity);

            Method setMediationProviderMethod = builder.getClass().getMethod("setMediationProvider", String.class);
            setMediationProviderMethod.invoke(builder, "max");

            Method buildMethod = builder.getClass().getMethod("build");
            Object initConfig = buildMethod.invoke(builder);

            Method getInstanceMethod = appLovinSdkClass.getMethod("getInstance", Context.class);
            Object appLovinSdk = getInstanceMethod.invoke(null, activity);

            Object listener = Proxy.newProxyInstance(
                    listenerClass.getClassLoader(),
                    new Class<?>[]{listenerClass},
                    (proxy, method, args) -> {
                        if ("onSdkInitialized".equals(method.getName())) {
                            sdkInitialized = true;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SdkInitialized();
                                }
                            });
                        }
                        return null;
                    }
            );

            Method initializeMethod = appLovinSdkClass.getMethod(
                    "initialize",
                    initConfigClass,
                    listenerClass
            );

            initializeMethod.invoke(appLovinSdk, initConfig, listener);

        } catch (Exception e) {
            SdkInitializationFailed(e.toString());
        }
    }

    @SimpleFunction(description = "Returns true if AppLovin SDK is initialized.")
    public boolean IsSdkInitialized() {
        return sdkInitialized;
    }

    @SimpleFunction(description = "Open AppLovin MAX Mediation Debugger.")
    public void OpenMediationDebugger() {
        try {
            Class<?> appLovinSdkClass = Class.forName("com.applovin.sdk.AppLovinSdk");
            Method getInstanceMethod = appLovinSdkClass.getMethod("getInstance", Context.class);
            Object appLovinSdk = getInstanceMethod.invoke(null, activity);

            Method showDebuggerMethod = appLovinSdkClass.getMethod("showMediationDebugger");
            showDebuggerMethod.invoke(appLovinSdk);

        } catch (Exception e) {
            SdkInitializationFailed(e.toString());
        }
    }

    @SimpleEvent(description = "Triggered when test function runs successfully.")
    public void TestSuccess(String message) {
        EventDispatcher.dispatchEvent(this, "TestSuccess", message);
    }

    @SimpleEvent(description = "Triggered when AppLovin SDK initializes successfully.")
    public void SdkInitialized() {
        EventDispatcher.dispatchEvent(this, "SdkInitialized");
    }

    @SimpleEvent(description = "Triggered when AppLovin SDK initialization fails.")
    public void SdkInitializationFailed(String error) {
        EventDispatcher.dispatchEvent(this, "SdkInitializationFailed", error);
    }
}
