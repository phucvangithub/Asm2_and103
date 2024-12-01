package fpoly.md19304.asm_and103;

import static fpoly.md19304.asm_and103.APIService.DOMAIN;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private final APIService requestInterface;

    // Constructor mặc định không cần token
    public HttpRequest() {
        requestInterface = createRetrofit(null);
    }

    // Constructor với token
    public HttpRequest(String token) {
        requestInterface = createRetrofit(token);
    }

    // Phương thức khởi tạo Retrofit chung
    private APIService createRetrofit(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (token != null && !token.isEmpty()) {
            httpClient.addInterceptor(createAuthInterceptor(token));
        }

        return new Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(APIService.class);
    }

    // Interceptor thêm Authorization header khi có token
    private Interceptor createAuthInterceptor(String token) {
        return new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(request);
            }
        };
    }

    // Phương thức gọi API
    public APIService callAPI() {
        return requestInterface;
    }
}
