package com.kitezeng.shopping.apiHelper;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kitezeng.shopping.ApiService;
import com.kitezeng.shopping.Callback3;
import com.kitezeng.shopping.Callback4;
import com.kitezeng.shopping.Model.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {
    private static Handler mainThreadHandler;
    private static final int TIME_OUT = 10 * 1000; // 超時時間
    private static final String CHARSET = "utf-8"; // 設定編碼
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

    public interface Callback5 {
        void success(String rawString);

        void fail(Exception exception);
    }


    public static void sendHTTPData(String urlpath, JSONObject json,int code1 , Callback5 callback5) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                HttpURLConnection connection = null;
                try {
                    URL url = new URL(urlpath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());
                    streamWriter.write(json.toString());
                    streamWriter.flush();
                    StringBuilder stringBuilder = new StringBuilder();

                    int code = conn.getResponseCode();
                    Log.e("code",code+"");
                    if (code1 == code) {
                        InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);
                        String response = null;
                        while ((response = bufferedReader.readLine()) != null) {
                            stringBuilder.append(response + "\n");
                        }
                        Log.e("json2",stringBuilder.toString());
                        if (callback5 != null) {
                            if (mainThreadHandler == null) {
                                mainThreadHandler = new Handler(Looper.getMainLooper());
                            }
                            mainThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback5.success(stringBuilder.toString());
//                                Log.e("response", response.toString());
                                }
                            });
                        }
                        // 根据定义好的数据结构解析出想要的东西
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                try {
//                    URL url = new URL(urlpath);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoOutput(true);
//                    connection.setDoInput(true);
//                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("Content-Type", "application/json");
//                    connection.setRequestProperty("Accept", "application/json");
//                    OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
//                    streamWriter.write(json.toString());
//                    streamWriter.flush();
//                    StringBuilder stringBuilder = new StringBuilder();
//                    if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
//                        InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//                        BufferedReader bufferedReader = new BufferedReader(streamReader);
//                        String response = null;
//                        while ((response = bufferedReader.readLine()) != null) {
//                            stringBuilder.append(response + "\n");
//                        }
//                        bufferedReader.close();
//                        if (callback2 != null) {
//                            if (mainThreadHandler == null) {
//                                mainThreadHandler = new Handler(Looper.getMainLooper());
//                            }
//                            mainThreadHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    callback2.success();
////                                Log.e("response", response.toString());
//                                }
//                            });
//                        }
//                        Log.d("test1", stringBuilder.toString());
////                return stringBuilder.toString();
//                    } else {
//                        Log.e("test", connection.getResponseMessage());
////                return null;
//                    }
//
//                } catch (Exception exception) {
//                    Log.e("test", exception.toString());
//
//                    if (callback2 != null) {
//                        if (mainThreadHandler == null) {
//                            mainThreadHandler = new Handler(Looper.getMainLooper());
//                        }
//                        mainThreadHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callback2.fail(exception);
//                            }
//                        });
//                    }
////            return null;
//                } finally {
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                }
            }
        }).start();


    }

    public static void sendHttp(String imagepath, Map<String,Object> map, Callback4 callback4){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(6000, TimeUnit.SECONDS)
                .writeTimeout(6000, TimeUnit.SECONDS)
                .readTimeout(6000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://springbootmall-env.eba-weyjyptf.us-east-1.elasticbeanstalk.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
//        map.put("productName","曾雅鈺");
//        map.put("category","FOOD");
//        map.put("price",12345);
//        map.put("stock",11);
        String data = new Gson().toJson(map);
        MediaType types = MediaType.parse("application/json; charset=UTF-8");
        RequestBody requestBody = RequestBody.create(types, data);
        File file = new File(imagepath);
        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/jpeg"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file",file.getName(),fileRQ);
        apiService.postFiles(part,requestBody).enqueue(new retrofit2.Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product = response.body();
                Log.e("productLast",product+"");
                callback4.success(product);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                callback4.fail(t);
            }
        });
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
                            "application/json");
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

    public void uploadImageFile(File file, String urlStr, Callback2 callback2) {
        Boolean result = false;
        String BOUNDARY = "letv"; // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        //String CONTENT_TYPE = "application/json"; // json
        String CONTENT_TYPE = "multipart/form-data";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(TIME_OUT);
                    conn.setConnectTimeout(TIME_OUT);
                    conn.setDoInput(true); // 允许输入流
                    conn.setDoOutput(true); // 允许输出流
                    conn.setUseCaches(false); // 不允许使用缓存
                    conn.setRequestMethod("POST"); // 请求方式
                    conn.setRequestProperty("Charset", CHARSET); // 设置编码
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                            + BOUNDARY);

                    if (file != null) {
                        /**
                         * 当文件不为空，把文件包装并且上传
                         */
                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                        StringBuffer sb = new StringBuffer();
                        sb.append(PREFIX);
                        sb.append(BOUNDARY);
                        sb.append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
                        sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                        sb.append(LINE_END);
                        dos.write(sb.toString().getBytes());


                        InputStream is = new FileInputStream(file);
                        byte[] bytes = new byte[1024 * 1024];
                        int len = 0;
                        while ((len = is.read(bytes)) != -1) {
                            dos.write(bytes, 0, len);
                        }
                        is.close();
                        dos.write(LINE_END.getBytes());
                        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                                .getBytes();
                        dos.write(end_data);
                        dos.flush();
                        /**
                         * 获取响应码 200=成功 当响应成功，获取响应的流
                         */
                        int res = conn.getResponseCode();

                        if (res == 200) {
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
                        }

                    }
                } catch (MalformedURLException e) {
                    callback2.fail(e);
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
