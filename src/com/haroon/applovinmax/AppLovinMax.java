package com.haroon.applovinmax;

import android.app.Activity;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkInitializationConfiguration;

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
        description = "AppLovin MAX extension for App Inventor. SDK initialization added.",
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
            AppLovinSdkInitializationConfiguration initConfig =
                    AppLovinSdkInitializationConfiguration.builder(sdkKey, activity)
                            .setMediationProvider(AppLovinMediationProvider.MAX)
                            .build();

            AppLovinSdk.getInstance(activity).initialize(initConfig, new AppLovinSdk.SdkInitializationListener() {
                @Override
                public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                    sdkInitialized = true;
                    SdkInitialized();
                }
            });

        } catch (Exception e) {
            SdkInitializationFailed(e.getMessage());
        }
    }

    @SimpleFunction(description = "Returns true if AppLovin SDK is initialized.")
    public boolean IsSdkInitialized() {
        return sdkInitialized;
    }

    @SimpleFunction(description = "Open AppLovin MAX Mediation Debugger.")
    public void OpenMediationDebugger() {
        try {
            AppLovinSdk.getInstance(activity).showMediationDebugger();
        } catch (Exception e) {
            SdkInitializationFailed(e.getMessage());
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
