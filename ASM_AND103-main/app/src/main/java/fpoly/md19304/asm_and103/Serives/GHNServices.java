package fpoly.md19304.asm_and103.Serives;



import java.util.ArrayList;

import fpoly.md19304.asm_and103.Models.District;
import fpoly.md19304.asm_and103.Models.DistrictRequest;
import fpoly.md19304.asm_and103.Models.Province;
import fpoly.md19304.asm_and103.Models.ResponseGHN;
import fpoly.md19304.asm_and103.Models.Ward;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GHNServices {
    public static String GHN_URL = "https://dev-online-gateway.ghn.vn/";

    @GET("/shiip/public-api/master-data/province")
    Call<ResponseGHN<ArrayList<Province>>> getListProvince();

    @POST("/shiip/public-api/master-data/district")
    Call<ResponseGHN<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);

    @GET("/shiip/public-api/master-data/ward")
    Call<ResponseGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);
}
