package com.plim.kati_app.jshCrossDomain.tech.retrofit;

import com.plim.kati_app.kati.crossDomain.tech.retrofit.SimpleRetrofitCallBack;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JSHRetrofitTool {

    public static <T>Callback<T> getCallback(JSHRetrofitCallback<T> callback){
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.isSuccessful()){ callback.onSuccessResponse(response); }
                else{ callback.onFailResponse(response); }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) { callback.onConnectionFail(t); }
        };
    }

    public static <T>Callback<T> getCallback(SimpleRetrofitCallBack<T> callback){
        return new Callback<T>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.isSuccessful()){ callback.onSuccessResponse(response); }
                else{ callback.onFailResponse(response); }
                callback.onResponse(response);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) { callback.onConnectionFail(t); }
        };
    }

}
