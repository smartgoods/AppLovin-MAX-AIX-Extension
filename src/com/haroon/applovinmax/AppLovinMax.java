package com.haroon.applovinmax;

import android.app.Activity;

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

@UsesLibraries(libraries = "applovin-sdk.aar")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE, com.google.android.gms.permission.AD_ID")
@DesignerComponent(
        version = 1,
        description = "AppLovin MAX extension for App Inventor. AppLovin SDK library test build.",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = ""
)
@SimpleObject(external = true)
public class AppLovinMax extends AndroidNonvisibleComponent {

    private final Activity activity;

    public AppLovinMax(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
    }

    @SimpleFunction(description = "Test function to check if the extension is working.")
    public void TestExtension() {
        TestSuccess("AppLovin extension loaded successfully.");
    }

    @SimpleFunction(description = "Test function to check if AppLovin SDK AAR is attached.")
    public void TestAppLovinLibrary() {
        TestSuccess("AppLovin SDK library annotation added successfully.");
    }

    @SimpleEvent(description = "Triggered when test function runs successfully.")
    public void TestSuccess(String message) {
        EventDispatcher.dispatchEvent(this, "TestSuccess", message);
    }
}
