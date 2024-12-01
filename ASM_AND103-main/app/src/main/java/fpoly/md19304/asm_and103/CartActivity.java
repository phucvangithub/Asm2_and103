package fpoly.md19304.asm_and103;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpoly.md19304.asm_and103.View.Location2Activity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;
    private TextView txtTotalPrice;
    private Button btnCheckout;
    private List<CartItem> billList;
    private double totalPrice;
    private Toolbar toolbarchitiet;

    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
//        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        txtTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        toolbarchitiet = findViewById(R.id.toolbarchitietsp);

        toolbarchitiet.setTitle("Giỏ hàng");
        setSupportActionBar(toolbarchitiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarchitiet.setNavigationOnClickListener(view -> finish());

        // Setup RecyclerView
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIService.class);

        // Load cart data
        loadCarList();
        fetchTotalPrice();

        // Handle Checkout button click
        btnCheckout.setOnClickListener(view -> {
            startActivity(new Intent(CartActivity.this, Location2Activity.class));
        });
    }

    private void loadCarList() {

        Call<List<CartItem>> call = apiService.getCart();
        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    billList = response.body();
                    if (billList.isEmpty()) {
                        Toast.makeText(CartActivity.this, "No cars available.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cartAdapter = new CartAdapter(CartActivity.this, billList); // Pass the context (CartActivity) to the adapter
                    recyclerViewCart.setAdapter(cartAdapter);

//                    // Optionally, calculate and display total price
//                    double totalPrice = 0;
//                    for (CartItem item : billList) {
//                        totalPrice += item.getPrice() * item.getQuantity();
//                    }
//                    tvTotalPrice.setText(String.format("%,.0f VND", totalPrice)); // Show total price
                } else {
                    Log.e("Main", "Response error: " + response.code());
                    Toast.makeText(CartActivity.this, "Failed to load cars.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Failed to load cars. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("Main", "Error: " + t.getMessage());
            }
        });
    }

    private void fetchTotalPrice() {
        Call<TotaPriceResponse> call = apiService.getTotalPrice();
        call.enqueue(new Callback<TotaPriceResponse>() {
            @Override
            public void onResponse(Call<TotaPriceResponse> call, Response<TotaPriceResponse> response) {
                if (response.isSuccessful()) {
                    totalPrice = response.body().getTotalPrice();
                    txtTotalPrice.setText("Tổng tiền" + "\n" + totalPrice);
                }else {
                    Toast.makeText(CartActivity.this, "Failed to fetch total price", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TotaPriceResponse> call, Throwable t) {

            }
        });
    }
}
