package com.example.appstore.Api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appstore.Interface.ApiCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CallApiUser {

    private OkHttpClient client = new OkHttpClient();

    public void getUser(String username, String password, ApiCallback callback) {
        String url = "http://10.0.2.2:3000/api/user";

        String json = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("ApiService", "Yêu cầu không thành công", e);
                callback.onError("Yêu cầu không thành công"+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("ApiService", "Mã lỗi không mong muốn: " + response.code());
                    callback.onError("Yêu cầu không thành công: " + response);
                    return;
                }
                String responseData = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(responseData);
                    if (jsonArray.length() == 0) {
                        Log.e("ApiService", "Mảng rỗng");
                        callback.onError("Mảng rỗng");
                    } else {
                        callback.onSuccess(responseData);

                    }
                } catch (JSONException e) {
                    Log.e("ApiService", "Lỗi khi đọc dữ liệu JSON", e);
                    callback.onError("Lỗi khi đọc dữ liệu JSON");
                }

            }

        });

    }

    public void createUser(String email, String password, ApiCallback callback) {
        String url = "http://10.0.2.2:3000/api/create-user";
        String json = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("ApiService", "Yêu cầu không thành công", e);
                callback.onError("Yêu cầu không thành công: " + e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("ApiService", "Yêu cầu không thành công với mã lỗi: " + response.code());
                    if (response.code() == 401) {
                        callback.onError("Đăng Kí Thất Bại. Tài Khoản Đã Tồn Tại!");
                    } else if (response.code() == 500) {
                        callback.onError("Lỗi khi thêm người dùng");
                    } else {
                        callback.onError("Lỗi không xác định: " + response.code());
                    }
                    return;
                }
                if (response.body() != null) {
                    String responseData = response.body().string();
                    Log.i("ApiService", "Dữ liệu phản hồi: " + responseData);
                    callback.onSuccess("Yêu cầu thành công: " + responseData);
                } else {
                    Log.e("ApiService", "Phản hồi không có body");
                    callback.onError("Phản hồi không có dữ liệu");
                }
            }
        });
    }

}
