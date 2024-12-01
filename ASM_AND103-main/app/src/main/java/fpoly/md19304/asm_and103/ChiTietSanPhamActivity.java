package fpoly.md19304.asm_and103;

import static fpoly.md19304.asm_and103.APIService.DOMAIN;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private TextView tvName, tvHang, tvNamSX, tvGia, tvmota;
    private ImageView imgAvatar;
    private Toolbar toolbarchitiet;
    private Spinner spinner;
    private Button btnAddToCart;
    APIService apiService;
    private String carName;
    private double carGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        // Ánh xạ các view
        toolbarchitiet = findViewById(R.id.toolbarchitietsp);
        spinner = findViewById(R.id.spinnerchitietsp);
        tvName = findViewById(R.id.tvName);
        tvHang = findViewById(R.id.tvHang);
        tvNamSX = findViewById(R.id.tvNamSX);
        tvGia = findViewById(R.id.tvGia);
        imgAvatar = findViewById(R.id.imgAvatatr);
        tvmota = findViewById(R.id.txtmotachitietsp);
        btnAddToCart = findViewById(R.id.btnthemgiohang);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        // Cấu hình toolbar
        setSupportActionBar(toolbarchitiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarchitiet.setNavigationOnClickListener(view -> finish());

        // Gán số lượng vào Spinner
        Integer[] quantityOptions = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, quantityOptions);
        spinner.setAdapter(quantityAdapter);

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

        // Xử lý sự kiện thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(view -> {
            // Lấy số lượng sản phẩm từ spinner
            int quantity = (int) spinner.getSelectedItem();

            // Lấy thông tin từ Intent (ví dụ: productId, imageUrl)
            String productName = carName;
            double price = carGia;
            String imageUrl = carAnh;

            // Tạo đối tượng CartItem
            CartItem cartItem = new CartItem(productName, quantity, price, imageUrl);

            // Gửi yêu cầu thêm sản phẩm vào giỏ hàng
            apiService.addToCart(cartItem).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Thông báo thêm thành công
                        Toast.makeText(ChiTietSanPhamActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Log error message for debugging
                        Log.e("AddToCart", "Error: " + response.message());

                        String errorMessage = "Thêm vào giỏ hàng thất bại!";
                        try {
                            if (response.errorBody() != null) {
                                errorMessage = response.errorBody().string(); // Get detailed error message from the server
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(ChiTietSanPhamActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Xử lý lỗi kết nối hoặc lỗi khi gọi API
                    String errorMessage = "Lỗi kết nối: " + t.getMessage();
                    Toast.makeText(ChiTietSanPhamActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
