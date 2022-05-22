package com.kitezeng.shopping.apiHelper;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitezeng.shopping.Model.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

public class ApiHelper {
    private static Handler mainThreadHandler;
    private static final String TAG = ApiHelper.class.getSimpleName();


    public static void requestApi(String partialUrl, Map<String, String> params, Callback callback) {

        requestBaseApi(partialUrl, params, callback);
    }


    private static void requestBaseApi(final String apiUrl, final Map<String, String> params, final Callback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;

                try {
                    URL url = new URL(apiUrl);
                    if ("https".equalsIgnoreCase(url.getProtocol())) {
                    }
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    if (params != null) {
                        Set<String> keys = params.keySet();
                        for (String key : keys) {
                            connection.setRequestProperty(key, params.get(key));
                        }
                    }
                    connection.setDoInput(true);
                    Log.e("tttt", "" + connection.getResponseCode());
                    BufferedReader in;

                    // 判斷來源是否為gzip
                    if ("gzip".equals(connection.getContentEncoding())) {
                        InputStreamReader reader = new InputStreamReader(
                                new GZIPInputStream(connection.getInputStream()));
                        in = new BufferedReader(reader);
                    } else {
                        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                        in = new BufferedReader(reader);
                    }
                    // 返回的數據已經過解壓

                    // 讀取回傳資料
                    String line;
                    final StringBuilder response = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        response.append(line)
                                .append('\n');
                    }

                    Log.e("ttt", response.toString());
                    if (callback != null) {
                        if (mainThreadHandler == null) {
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(response.toString());
                                Log.e("response", response.toString());
                            }
                        });
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        if (mainThreadHandler == null) {
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.fail(e);
                            }
                        });
                    }
                }
            }
        }).start();

    }


    public interface Callback {
        void success(String rawString);

        void fail(Exception exception);
    }

    public interface Callback2 {
        void success();

        void fail(Exception exception);
    }


    public static void sendHTTPData(String urlpath, JSONObject json, Callback2 callback2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(urlpath);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                    streamWriter.write(json.toString());
                    streamWriter.flush();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);
                        String response = null;
                        while ((response = bufferedReader.readLine()) != null) {
                            stringBuilder.append(response + "\n");
                        }
                        bufferedReader.close();

                        Log.d("test", stringBuilder.toString());
//                return stringBuilder.toString();
                    } else {
                        Log.e("test", connection.getResponseMessage());
//                return null;
                    }
                    if (callback2 != null) {
                        if (mainThreadHandler == null) {
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback2.success();
//                                Log.e("response", response.toString());
                            }
                        });
                    }
                } catch (Exception exception) {
                    Log.e("test", exception.toString());

                    if (callback2 != null) {
                        if (mainThreadHandler == null) {
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback2.fail(exception);
                            }
                        });
                    }
//            return null;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();


    }


    public static void updateHTTPData(String urlpath, JSONObject json, Callback2 callback2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(urlpath);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                    streamWriter.write(json.toString());
                    streamWriter.flush();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);
                        String response = null;
                        while ((response = bufferedReader.readLine()) != null) {
                            stringBuilder.append(response + "\n");
                        }
                        bufferedReader.close();

                        Log.d("test", stringBuilder.toString());
//                return stringBuilder.toString();
                    } else {
                        Log.e("test", connection.getResponseMessage());
//                return null;
                    }
                    if (callback2 != null) {
                        if (mainThreadHandler == null) {
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback2.success();
//                                Log.e("response", response.toString());
                            }
                        });
                    }
                } catch (Exception exception) {
                    Log.e("test", exception.toString());

                    if (callback2 != null) {
                        if (mainThreadHandler == null) {
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback2.fail(exception);
                            }
                        });
                    }
//            return null;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();


    }

    public static void deleteHttpData(String urlpath , Callback2 callback2){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(urlpath);
                } catch (MalformedURLException exception) {
                    exception.printStackTrace();
                }
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    httpURLConnection.setRequestMethod("DELETE");
                    if (callback2 != null) {
                        if (mainThreadHandler == null) {
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback2.success();
//                                Log.e("response", response.toString());
                            }
                        });
                    }
                    System.out.println(httpURLConnection.getResponseCode());
                } catch (IOException exception) {
                    exception.printStackTrace();
                    if (callback2 != null) {
                        if (mainThreadHandler == null) {
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback2.fail(exception);
                            }
                        });
                    }
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

}
