//package fpoly.md19304.asm_and103;
//
//import java.util.List;
//
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.DELETE;
//import retrofit2.http.GET;
//import retrofit2.http.Multipart;
//import retrofit2.http.POST;
//import retrofit2.http.PUT;
//import retrofit2.http.Part;
//import retrofit2.http.Path;
//
//public interface APIService {
//
//    String DOMAIN = "http://172.16.0.2:3000";
//
//    // Lấy danh sách xe
//    @GET("/api/list")
//    Call<List<CarModel>> getCars();
//    // Lấy thông tin xe theo ID
//    @GET("/api/car/{id}")  // Adjust this URL based on your API
//    Call<CarModel> getCarById(@Path("id") String carId);
//
////    // Thêm xe mới
////    @POST("/api/add_xe")
////    Call<List<CarModel>> addCar(@Body CarModel car);
//    @Multipart
//    @POST("/api/add_xe")
//    Call<List<CarModel>> addCarWithImage(
//            @Part("ten") RequestBody name,
//            @Part("namSX") RequestBody year,
//            @Part("hang") RequestBody brand,
//            @Part("gia") RequestBody price,
//            @Part MultipartBody.Part image);
//
//
//    // Xóa xe theo ID
//    @DELETE("/api/xoa/{id}")
//    Call<Void> deleteCar(@Path("id") String carId);
//
////    // Cập nhật thông tin xe theo tên
////    @PUT("/api/update/{ten}")
////    Call<List<CarModel>> updateCar(@Path("ten") String carName);
//@PUT("/api/update/{ten}")
//Call<Void> updateCar(@Path("ten") String carName, @Body CarModel updatedCar);
//
//}

package fpoly.md19304.asm_and103;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {

    String DOMAIN = "http://192.168.45.103:3000";

    // Lấy danh sách xe
    @GET("/api/cars/list")
    Call<List<CarModel>> getCars();

    // Lấy thông tin xe theo ID
    @GET("/api/cars/car/{id}")
    Call<CarModel> getCarById(@Path("id") String carId);

    // Thêm xe mới với hình ảnh
    @Multipart
    @POST("/api/cars/add_xe")
    Call<CarModel> addcar(
            @Part("ten") RequestBody ten,
            @Part("namSX") RequestBody namSX,
            @Part("hang") RequestBody hang,
            @Part("gia") RequestBody gia,
            @Part MultipartBody.Part anh,
            @Part("mota") RequestBody mota
    );
    @Multipart
    @POST("upload_image")
    Call<UploadResponse> uploadImage(
            @Part MultipartBody.Part image
    );
    // Thêm xe mới
    @POST("/api/cars/add_xe")
    Call<List<CarModel>> addCar(@Body CarModel car , @Part MultipartBody.Part anh);

    // Xóa xe theo ID
    @DELETE("/api/cars/xoa/{id}")
    Call<Void> deleteCar(@Path("id") String carId);

    // Cập nhật thông tin xe theo tên
    @PUT("/api/cars/update/{id}")
    Call<Void> updateCar(@Path("id") String carId, @Body CarModel updatedCar);

    @POST("/api/cart/add")
    Call<Void> addToCart(@Body CartItem cartItem);
    @GET("/api/cart/xe")
    Call<List<CartItem>> getCart();

    @GET("/api/cart/total")
    Call<TotaPriceResponse> getTotalPrice();

    // Xóa xe theo ID
    @DELETE("/api/cart/xoa/{id}")
    Call<Void> deleteCart(@Path("id") String cartId);
}
