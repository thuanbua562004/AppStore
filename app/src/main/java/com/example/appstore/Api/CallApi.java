package com.example.appstore.Api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appstore.Interface.ApiCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CallApi {

    private OkHttpClient client = new OkHttpClient();

    public void getUser(String email, ApiCallback callback) {
        String url = "http://10.0.2.2:3000/api/user";

        String json = "{\"email\":\"" + email + "\"}";
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
                String data = response.body().string();
                if(response.isSuccessful()){
                    if (response.code()==201){
                        callback.onSuccess(data);
                    }else if (data.isEmpty() && data.length()==0) {
                        callback.onError("Error");
                    }
                }
            }

        });

    }

    public void createUser(String email, String id, ApiCallback callback) {
        String url = "http://10.0.2.2:3000/api/create-user";
        String json = "{\"email\":\"" + email + "\", \"id\":\"" + id + "\"}";
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

    public  void updateUser(String id , String name ,String address,String date ,String phone , ApiCallback callback){
        String url = "http://10.0.2.2:3000/api/update-user";
        String json = "{\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"address\":\"" + address + "\", \"dateBirth\":\"" + date + "\", \"phone\":\"" + phone + "\"}";        RequestBody requestBody = RequestBody.create(json,MediaType.get("application/json; charset=utf-8"));
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new  Request.Builder()
                .url(url)
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("updateUser", "onFailure: " + e.getMessage());
                callback.onError("Error" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                if(response.isSuccessful()){
                 if(response.code()==500){
                     Log.i("updateUser", "onFailure: " + responseData);
                     callback.onError(response.toString());
                 }else if(response.code()==404){
                        Log.i("updateUser", "onFailure: " + response.body());
                     callback.onError(response.toString());
                 }else {
                     callback.onSuccess(responseData);
                 }
                }

            }
        });
    }
    public void getListProduct (ApiCallback callback){
        String url = "http://10.0.2.2:3000/api/product";
        Request request =new  Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Yeu cau list product"+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    if(response.code()==404){
                        callback.onError("Error");
                    } else if (response.code()==200) {
                        if(!responseData.isEmpty() && !responseData.equals("")){
                            callback.onSuccess(responseData);
                        }
                    }else {
                        callback.onError("Error");
                    }
                } else {
                    callback.onError("Error");
                }

            }
        });
    }
    public  void  getListNotifi (ApiCallback callback){
        String url = "http://10.0.2.2:3000/api/notifi";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Error"  + e.getMessage());
                Log.i("Notifi", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resphoneData = response.body().string();
                if (response.isSuccessful()){
                    if (!resphoneData.isEmpty()&& !resphoneData.equals("")){
                        callback.onSuccess(resphoneData);
                    }
                }else {
                    callback.onError("Error Data Null" + resphoneData);
                }
            }
        });

    }

    public void addToCart (String userId ,String id_color_size,String color ,String id_product,String imgProduct,String nameProduct,int number,int price,String size,ApiCallback apiCallback){
        String url = "http://10.0.2.2:3000/api/add-cart";
        String json = "{"
                + "\"userId\":\"" + userId + "\","
                + "\"id_color_size\":\"" + id_color_size + "\","
                + "\"color\":\"" + color + "\","
                + "\"productId\":\"" + id_product + "\","
                + "\"imgProduct\":\"" + imgProduct + "\","
                + "\"nameProduct\":\"" + nameProduct + "\","
                + "\"number\":\"" + number + "\","
                + "\"price\":\"" + price + "\","
                + "\"size\":\"" + size + "\""
                + "}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiCallback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 501) {
                    String errorBody = response.body().string();
                    Log.d("HTTP",   errorBody);
                    apiCallback.onSuccess( errorBody);
                } else {
                    apiCallback.onSuccess("Successful");
                }

            }
        });
    }

    public  void getCart(String userId,ApiCallback callback){
        String url ="http://10.0.2.2:3000/api/cart";
        String json = "{\"id\":\"" + userId + "\"}";
        RequestBody requestBody = RequestBody.create(json,MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String dataResphone = response.body().string();
                Log.i("cart", "api: "+ dataResphone);
                callback.onSuccess(dataResphone);
            }
        });
    }
    public  void updateCart(String userId ,String id_size_color ,Integer Number,ApiCallback callback ){
        String url ="http://10.0.2.2:3000/api/update-cart";
        String json = "{\"userid\":\"" + userId + "\", \"color_size\":\"" + id_size_color + "\", \"number\":\"" + Number + "\"}";
        RequestBody requestBody = RequestBody.create(json,MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String dataResphone = response.body().string();
                Log.i("cart", "api: "+ dataResphone);
                callback.onSuccess(dataResphone);
            }
        });
    }

    public  void deleteItemCart(String userId ,String id_size_color, ApiCallback callback ){
        String url ="http://10.0.2.2:3000/api/delItems-cart";
        String json = "{\"userid\":\"" + userId + "\", \"color_size\":\"" + id_size_color + "\"}";
        RequestBody requestBody = RequestBody.create(json,MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .delete(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String dataResphone = response.body().string();
                Log.i("cart", "api: "+ dataResphone);
                callback.onSuccess(dataResphone);
            }
        });
    }

    public  void deleteCart(String userId){
        String url ="http://10.0.2.2:3000/api/del-cart";
        String json = "{\"userid\":\"" + userId + "\"}";
        RequestBody requestBody = RequestBody.create(json,MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .delete(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("cart", "api: "+ e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String dataResphone = response.body().string();
                Log.i("cart", "api: "+ dataResphone);
            }
        });
    }
    public void saveHistoryBuy(String id , String address, String method, Integer totalPrice, String phone, String listProduct , ApiCallback callback){
        String url ="http://10.0.2.2:3000/api/history-buy";
        String json =
                "{ \"id\":\"" + id +
                        "\", \"address\":\"" + address +
                        "\", \"method\":\"" + method +
                        "\", \"totalPrice\":" + totalPrice +
                        ", \"phone\":\"" + phone +
                        "\", \"listProduct\":" + listProduct +
                        "}";

        RequestBody requestBody = RequestBody.create(json,MediaType.get("application/json; charset=utf-8"));
        Request  request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    callback.onSuccess(response.body().string());
                }

            }
        });
    }

    public void getHistoryBuy (ApiCallback callback){
        String url ="http://10.0.2.2:3000/api/history-buy";
        Request  request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    callback.onSuccess(response.body().string());
                }

            }
        });
    }
}
