package fpoly.md19304.asm_and103;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class EditCarActivity extends AppCompatActivity {

    private EditText etName, etNamSX, etHang, etGia, etAnh, etmota;
    private Button btnSave;
    private APIService apiService;
    private String carId;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        etName = findViewById(R.id.etName);
        etNamSX = findViewById(R.id.etNamSX);
        etHang = findViewById(R.id.etHang);
        etGia = findViewById(R.id.etGia);
        etAnh = findViewById(R.id.etAnh); // Thêm trường cho link ảnh
        etmota = findViewById(R.id.etmota);
        btnSave = findViewById(R.id.btnSave);

        apiService = RetrofitClient.getClient().create(APIService.class);
        carId = getIntent().getStringExtra("car_id");

        loadCarDetails();

        btnSave.setOnClickListener(v -> updateCarDetails());




        Intent intent = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadCarDetails() {
        Call<CarModel> call = apiService.getCarById(carId);
        call.enqueue(new Callback<CarModel>() {
            @Override
            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                if (response.isSuccessful()) {
                    CarModel car = response.body();
                    etName.setText(car.getTen());
                    etNamSX.setText(String.valueOf(car.getNamSX()));
                    etHang.setText(car.getHang());
                    etGia.setText(String.valueOf(car.getGia()));
                    etAnh.setText(car.getAnh()); // Hiển thị đường link ảnh hiện tại
                    etmota.setText(car.getMota());
                } else {
                    Toast.makeText(EditCarActivity.this, "Lỗi tải thông tin xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarModel> call, Throwable t) {
                Toast.makeText(EditCarActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCarDetails() {
        String name = etName.getText().toString();
        int namSX = Integer.parseInt(etNamSX.getText().toString());
        String hang = etHang.getText().toString();
        double gia = Double.parseDouble(etGia.getText().toString());
        String anh = etAnh.getText().toString();
        String mota = etmota.getText().toString();
        CarModel updatedCar = new CarModel(carId, name, namSX, hang, gia, anh,mota);

        // Kiểm tra JSON gửi lên server
        Log.d("EditCarActivity", "JSON to send: " + new Gson().toJson(updatedCar));

        Call<Void> call = apiService.updateCar(carId, updatedCar);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(EditCarActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorMessage = "Lỗi cập nhật";
                    try {
                        errorMessage += ": " + response.errorBody().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("EditCarActivity", errorMessage);
                    Toast.makeText(EditCarActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EditCarActivity", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(EditCarActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
