package fpoly.md19304.asm_and103;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Địa chỉ của API
    private static final String BASE_URL = "http://192.168.45.103:3000";

    private static Retrofit retrofit = null;

    // Phương thức trả về Retrofit instance
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)  // URL của API
                    .addConverterFactory(GsonConverterFactory.create())  // Chuyển đổi JSON sang đối tượng Java
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getClient1(String baseUrl) {
        if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl)) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
