# Pulse Insights Android SDK

## Installation

To install PulseInsights on your application, follow those steps:

1. Add the `maven repositories target` and the `google repositories target` in the **build.gradle** script of the **project level** as the following example shows, make sure you have **google()** in the top of **repositories** section in the both of **buildscript** and **allprojects**
```gradle
buildscript {
    repositories {
        google()
        ...
    }
}
allprojects {
    repositories {
        google()
        ...
        maven {
            url  "https://pi-sdk.s3.us-east-1.amazonaws.com/android"
        }
        ...

    }
}
```

2. Add the dependencies description in the **build.gradle** script of the **app level** as the following example shows
```gradle
dependencies {
    ...
    implementation 'com.pulseinsights:android-sdk:2.4.4'
}
```
> You only need to modify this implementation description with the available version name when you want to update the SDK in the future

3. Sync the gradle script

4. If your app is targeting API level 28 (Android 9.0) or above, please make sure the following `<uses-library>` element description been added inside the `<application>` element of `AndroidManifest.xml` , you can check [the developer documentation](https://developers.google.com/maps/documentation/android-sdk/config#specify_requirement_for_apache_http_legacy_library) for more detail
```Xml
<uses-library
    android:name="org.apache.http.legacy"
    android:required="false" />
```

## Usage

### 1. Initialization

When you application start, you should initialize the library using the snippet below:

```Java
PulseInsights pi = new PulseInsights([context], YOUR_ACCOUNT_ID);
```
Replace YOUR_ACCOUNT_ID with your own PulseInsights ID, like PI-12345678.

You should subclass `Application` and provide a helper method that returns your application's PulseInsights object:

```Java
public class PulseInsightsApplication extends Application {

  private PulseInsights pi;

  synchronized public getPulseInsights() {

      if(pi == null)
          pi = new PulseInsights(this, YOUR_ACCOUNT_ID);

      return pi;
  }
}
```

You also need to provide the context of the Activity object for display the survey view or the invite widget
```Java
@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    pi.setContext(Context context)
    ...
}
```

After this initial step, you can fetch the PulseInsights object as below:
```Java
new PulseInsights([context]);
```

### 2. View tracking

PulseInsights allow to target surveys given a screen name. In order for the SDK to know about the current screen name, you can use the following method to notify the SDK of the current screen name change:

```Java
pi.setViewName(String viewName);
```

For example, you can override the onCreate function or the onActivityResult function on the Activity class:

```Java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    pi.setViewName("cus_MainActivity");
}
```

```Java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == MainActivity_REQUEST) {
        String result = data.getExtras().getString("result");
        if (result.equalsIgnoreCase("BACK_ACTIVITY")) {
            pi.setViewName("cus_MainActivity");
        }
    }
}
```

### 3. Survey polling

The PulseInsights SDK will automatically regularly fetch surveys that would match various target conditions, based on a frequency that you can override as show below:

```Java
pi.setScanFrequency(int frequencyInSecond);
```

If you want to manual fetch new surveys, you can also directly use this method:

```Java
pi.serve();
```

### 4. Render a specific survey

It's also possible to manual trigger the rendering of a survey by its id:

```Java
pi.present(String surveyID);
```

### 5. Inline surveys

Inline surveys are rendered within the content of the application, instead of being rendered as pop-ups, overlaying the application content.

In order to integrate the inline surveys, you can programmatically create the `InlineSurveyView` object with giving an identifier and insert it into a view:

```Java
InLineSurveyView inlineView = new InLineSurveyView(Context context, String trackId);
```

You still can integrate the inline surveys by adding the inline survey on the XML layout with the survey view class `com.pulseinsights.pisurveylibrary.util.InlineSurveyView`

```xml
<com.pulseinsights.pisurveylibrary.util.InlineSurveyView
    android:id="@+id/inline_survey"
    android:layout_width="match_parent"
/>
```

In this case, you can assign the identifier on the inline view by using the method `setIdentifier`

```Java
InlineSurveyView inlineSurvey;
inlineSurvey = (InlineSurveyView) itemView.findViewById(R.id.inline_survey);
inlineSurvey.setIdentifier(String trackId);
```

### 6. Survey rendering

You can pause and resume the survey rendering feature with the following method:

```Java
pi.switchSurveyScan(boolean enable);
```

And check the current configueration with the flollowing method:
- true: survey rendering feature is working
- false: survey rendering feature has been paused

```Java
boolean renderingConfig = pi.isSurveyRenderingActive();
```

It's also possible to pause the survey rendering feature from the initialization of the Pulse Insights library:

```Java
ExtraConfig piConfig = new ExtraConfig();
piConfig.automaticStart = false;
PulseInsights pi = new PulseInsights(Context context, String accountId, ExtraConfig piConfig);
```

> You can check [this article](#1-extraconfig) for learn more about the `ExtraConfig` class.

### 7. Client Key

Client key can be setup using this method:
```Java
pi.setClientKey(String clientKey);
```

The configured client key can be fetched with this method:
```Java
String getKey = pi.getClientKey();
```


### 8. Preview mode

The preview mode can be enable/disable by shake motion according the following rule:
```
Shake the device more than 10-time in 3-second
```

The preview mode can be programally enable/disable by this method:
```Java
pi.setPreviewMode(boolean enable)
```

It's also possible to set the preview mode from the initialization of the Pulse Insights library:
```Java
ExtraConfig piConfig = new ExtraConfig();
piConfig.previewMode = true;
PulseInsights pi = new PulseInsights(Context context, String accountId, ExtraConfig piConfig);
```

> You can check [this article](#1-extraconfig) for learn more about the `ExtraConfig` class.

In order to check the status of preview mode, use this method:
```Java
boolean isPreviewModeOn = pi.isPreviewModeOn()
```

### 9. Callbacks

If you want to know if a survey has been answered by the current device, this method can be used:
```Java
boolean answered = pi.checkSurveyAnswered(String surveyId);
```

It's also possible to configure a callback to be executed when a survey has been answered:

```Java
pi.setAnswerListener(new SurveyAnsweredListener() {
    @Override
    public void onAnswered(String answerId) {

    }
});
```
### 10. Context data

You can save context data along with the survey results, or for a refined survey targeting, using the `customData` config attribute, for example:

```Java
ExtraConfig piConfig = new ExtraConfig();
piConfig.customData = new HashMap<String, String>();
piConfig.customData.put("gender", "male");
piConfig.customData.put("age", "32");
piConfig.customData.put("locale", "en-US");
PulseInsights pi = new PulseInsights(Context context, String accountId, ExtraConfig piConfig);

```

You can also use `pi.clearContextData()` to clear all data you added before.

> You can check [this article](#1-extraconfig) for learn more about the `ExtraConfig` class.

### 11. Device data

If you want to set device data, which will be saved along the survey results, the method `setDeviceData` can be used as follows:

```Java
pi.setDeviceData(Map<String, String> map);
```

`setDeviceData` can be called at any time, it will trigger a separate network request to save the data.

### 12. Advanced

The default host is "survey.pulseinsights.com". If you want to target the staging environment, or any other environment, it's possible to override the default host:

```Java
pi.setHost(String surveyID);
```

The debug mode can be turned on and off:

```Java
pi.setDebugMode(boolean enable);
```

PulseInsights creates a unique UDID to track a given device. If you wish to reset this UDID, you can call the following method:

```Java
pi.resetUdid();
```

If you want manually config the context, you can call the following method:

```Java
pi.setContext(Context context)
```

And get the context object which been configured

```Java
Context context = pi.getContext()
```

### 13. Others

#### 1. ExtraConfig

The ExtraConfig is the class which you can apply the additioanl configuration from the initialization of the Pulse Insights library:

```Java
ExtraConfig piConfig = new ExtraConfig();
piConfig.automaticStart = false;
PulseInsights pi = new PulseInsights(Context context, String accountId, ExtraConfig piConfig);
```

You can apply the additional config with the following sub variables:

| Name | function | Default | Usage |
| --- | --- | --- | --- |
| automaticStart | [Survey rendering](#6-survey-rendering) | true | true - survey rendering will automatically started from the initialization  <br> false - survey rendering will not started from the initialization |
| previewMode | [Preview mode](#8-preview-mode) | false | ture - turn the peview mode on <br> false - turn the preview mode off |
| customData | [Context data](#10-context-data) | new HashMap<>() | Save data along with survey results |



## Uninstall

1. Remove whatever you added when you go through the install flow
2. Sync or rebuild your project

