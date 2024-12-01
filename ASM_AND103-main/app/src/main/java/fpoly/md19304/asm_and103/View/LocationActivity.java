package fpoly.md19304.asm_and103.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import fpoly.md19304.asm_and103.Adapter.Adapter_Item_District_Select_GHN;
import fpoly.md19304.asm_and103.Adapter.Adapter_Item_Province_Select_GHN;
import fpoly.md19304.asm_and103.Adapter.Adapter_Item_Ward_Select_GHN;
import fpoly.md19304.asm_and103.Models.District;
import fpoly.md19304.asm_and103.Models.DistrictRequest;
import fpoly.md19304.asm_and103.Models.Province;
import fpoly.md19304.asm_and103.Models.ResponseGHN;
import fpoly.md19304.asm_and103.Models.Ward;
import fpoly.md19304.asm_and103.R;
import fpoly.md19304.asm_and103.Serives.GHNRequest;
import fpoly.md19304.asm_and103.Serives.GHNServices;
import fpoly.md19304.asm_and103.databinding.ActivityLocationBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    private ActivityLocationBinding binding;
    private TextView tvName, tvHang, tvNamSX, tvGia, tvmota;
    private ImageView imgAvatar;
    private Toolbar toolbarchitiet;
    private GHNRequest request;
    private GHNServices ghnServices;
    private String _id, WardCode;
    private String ten;

    private int namSX;

    private String hang;

    private double gia;
    private String anh;
    private String carName;
    private double carGia;
    private int image, DistrictID, ProvinceID ;
    private Adapter_Item_Province_Select_GHN adapter_item_province_select_ghn;
    private Adapter_Item_District_Select_GHN adapter_item_district_select_ghn;
    private Adapter_Item_Ward_Select_GHN adapter_item_ward_select_ghn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        request = new GHNRequest();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _id = bundle.getString("Id");

            ten = bundle.getString("ten");
            namSX = bundle.getInt("namSX");
            hang = bundle.getString("hang");
            gia = bundle.getDouble("gia");
            image = bundle.getInt("anh");
        }

        request.callAPI().getListProvince().enqueue(responseProvince);
        binding.spProvince.setOnItemSelectedListener(onItemSelectedListener);
        binding.spDistrict.setOnItemSelectedListener(onItemSelectedListener);
        binding.spWard.setOnItemSelectedListener(onItemSelectedListener);

        binding.spProvince.setSelection(1);
        binding.spDistrict.setSelection(1);
        binding.spWard.setSelection(1);

        tvName = findViewById(R.id.tvName);
        tvHang = findViewById(R.id.tvHang);
        tvNamSX = findViewById(R.id.tvNamSX);
        tvGia = findViewById(R.id.tvGia);
        imgAvatar = findViewById(R.id.imgAvatatr);

        Button oder = findViewById(R.id.btn_order);
        oder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LocationActivity.this, "Đăt hàng thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Lấy thông tin từ Intent
        carName = getIntent().getStringExtra("ten");
        String carHang = getIntent().getStringExtra("hang");
        int carNamSX = getIntent().getIntExtra("namSX", 0);
        carGia = getIntent().getDoubleExtra("gia", 0.0);
        String carAnh = getIntent().getStringExtra("anh");
        String carMota = getIntent().getStringExtra("mota");

        // Cập nhật UI
        if (carName != null) {
            tvName.setText(carName);
        }

        if (carHang != null) {
            tvHang.setText(carHang);
        }

        tvNamSX.setText(String.valueOf(carNamSX));
        tvGia.setText(String.format("%,.0f VND", carGia));

        if (carAnh != null && !carAnh.isEmpty()) {
            Picasso.get().load(carAnh).into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.baseline_broken_image_24);
        }

        if (carMota != null) {
            tvmota.setText(carMota);
        }

        toolbarchitiet = findViewById(R.id.toolbarchitietsp);
        toolbarchitiet.setTitle("Thanh toán ");
        // Cấu hình toolbar
        setSupportActionBar(toolbarchitiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarchitiet.setNavigationOnClickListener(view -> finish());


    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.sp_province) {
                ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceID);
                request.callAPI().getListDistrict(districtRequest).enqueue(responseDistrict);
            } else if (parent.getId() == R.id.sp_district) {
                DistrictID = ((District) parent.getAdapter().getItem(position)).getDistrictID();
                request.callAPI().getListWard(DistrictID).enqueue(responseWard);
            } else if (parent.getId() == R.id.sp_ward) {
                WardCode = ((Ward) parent.getAdapter().getItem(position)).getWardCode();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Callback<ResponseGHN<ArrayList<Province>>> responseProvince = new Callback<ResponseGHN<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<Province> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinProvince(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lấy dữ liệu bị lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    Callback<ResponseGHN<ArrayList<District>>> responseDistrict = new Callback<ResponseGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<District> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinDistrict(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {

        }
    };

    Callback<ResponseGHN<ArrayList<Ward>>> responseWard = new Callback<ResponseGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){

                    if(response.body().getData() == null)
                        return;

                    ArrayList<Ward> ds = new ArrayList<>(response.body().getData());

                    ds.addAll(response.body().getData());
                    SetDataSpinWard(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    private void SetDataSpinProvince(ArrayList<Province> ds){
        adapter_item_province_select_ghn = new Adapter_Item_Province_Select_GHN(this, ds);
        binding.spProvince.setAdapter(adapter_item_province_select_ghn);
    }

    private void SetDataSpinDistrict(ArrayList<District> ds){
        adapter_item_district_select_ghn = new Adapter_Item_District_Select_GHN(this, ds);
        binding.spDistrict.setAdapter(adapter_item_district_select_ghn);
    }

    private void SetDataSpinWard(ArrayList<Ward> ds){
        adapter_item_ward_select_ghn = new Adapter_Item_Ward_Select_GHN(this, ds);
        binding.spWard.setAdapter(adapter_item_ward_select_ghn );
    }

}