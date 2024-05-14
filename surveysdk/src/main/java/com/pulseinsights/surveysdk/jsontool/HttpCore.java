package com.pulseinsights.surveysdk.jsontool;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.pulseinsights.surveysdk.util.DebugTool;
import com.pulseinsights.surveysdk.util.PreferencesManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by LeoChao on 2016/10/6.
 */

public class HttpCore {
    Context context = null;
    JsonGetResult jsonGetResult = null;
    String strExtend = "";
    boolean isInterrupted = false;
    Thread httpRequest = null;


    public HttpCore(Context context) {
        this.context = context;
        this.isInterrupted = false;
    }

    public void setCallBack(JsonGetResult jsonGetResult) {
        this.jsonGetResult = jsonGetResult;
    }

    public void doTerminate() {
        isInterrupted = true;
        if (httpRequest != null) {
            httpRequest.interrupt();
        }
    }

    public String composeRequestUrl(String path) {
        return composeRequestUrl(path, new HashMap<String, String>());
    }

    public String composeRequestUrl(String path, Map<String, String> requestParams) {
        String hostDomain = PreferencesManager.getInstance(context).getServerHost();
        String paramStr = "?";
        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            paramStr += String.format("%s=%s&", key, value);
        }
        paramStr = paramStr.substring(0, paramStr.length() - 1);

        return String.format("%s/%s%s", hostDomain, path, paramStr);
    }

    public static String stringifyMap(Map<String, String> map) {
        String contents = "";
        for (String key : map.keySet()) {
            contents += String.format("\"%s\":\"%s\",", key, map.get(key));
        }
        contents = contents.isEmpty() ? "" : contents.substring(0, contents.length() - 1);
        return String.format("{%s}", contents);
    }

    public void startRequest(String strReq, String strExt) {
        this.strExtend = strExt;
        httpRequest = new Thread(new HttpReqThread(strReq));
        httpRequest.start();

    }

    class RespondHandler extends Handler {
        public RespondHandler() {
        }

        public RespondHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String jsonString = bundle.getString("jsonstring");
            if (jsonString.equalsIgnoreCase("error")) {
                strExtend = "-1";
            }
            if (!isInterrupted) {
                jsonGetResult.getResult(jsonString, strExtend);
            }
        }
    }

    class HttpReqThread implements Runnable {

        String strHttpReq = "";

        public HttpReqThread(String strRequest) {

            this.strHttpReq = strRequest;
            // TODO Auto-generated constructor stub
            //We already have the city_name, city_id and color from the list page, no need re-catch
            //Log.i("cat", city+backcolor+cityId);

        }

        @Override
        public void run() {
            DebugTool.debugPrintln(context,
                    "PI/httpcore", "Thread excuted.................");
            String jsonString = "";
            jsonString = connServerForString(strHttpReq);


            if (!isInterrupted) {
                Bundle bundle = new Bundle();
                RespondHandler txtHandler;
                txtHandler = new RespondHandler(context.getMainLooper());
                Message msg = txtHandler.obtainMessage();
                bundle.putString("jsonstring", jsonString);
                msg.setData(bundle);
                txtHandler.sendMessage(msg);
                DebugTool.debugPrintln(context, "PI/httpcore/result", jsonString);
            }
        }


    }


    String connServerForString(String strUrl) {
        String strResult = "error";
        String strErrMsg = "";
        HttpResponse httpResp = null;

        try {
            DebugTool.debugPrintln(context, "PI/httpcore/request", strUrl);
            HttpGet httpRequest = new HttpGet(strUrl);
            String deviceOs = Build.VERSION.RELEASE;
            String deviceBrand = Build.MANUFACTURER;
            String deviceModel = Build.MODEL;
            String userAgent = String.format("Android-HttpClient/UNAVAILABLE (%s %s, Android %s)",
                    deviceBrand, deviceModel, deviceOs);

            httpRequest.addHeader("User-Agent", userAgent);
            HttpClient httpClient = new DefaultHttpClient();
            httpResp = httpClient.execute(httpRequest);
            if (httpResp.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
            } else {
                strErrMsg = EntityUtils.toString(httpResp.getEntity());
                DebugTool.debugPrintln(context,
                        "PI/httpcore/err", ">>>>>>" + strErrMsg);
            }

        } catch (ClientProtocolException exception) {
            // TODO Auto-generated catch block
            DebugTool.debugPrintln(context, "PI/httpcore/err",
                    ">>>>>>" + "protocol error");
            exception.printStackTrace();
        } catch (IOException exception) {
            // TODO Auto-generated catch block
            DebugTool.debugPrintln(context,
                    "PI/httpcore/err", ">>>>>>" + "IO error");
            exception.printStackTrace();
        } finally {

            try {
                HttpEntity entity = httpResp.getEntity();
                if (entity != null) {
                    entity.consumeContent();
                }
                DebugTool.debugPrintln(context, "PI/httpcore", "closed");
                httpResp.getEntity().getContent().close();
            } catch (Exception exception) {
                //TODO:Handle the exception
            }
        }


        return strResult;
    }


}
