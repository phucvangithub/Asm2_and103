package fpoly.md19304.asm_and103;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fpoly.md19304.asm_and103.View.LocationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private Context context;
    private List<CarModel> carModelList;
    private APIService apiService;

    public CarAdapter(Context context, List<CarModel> carModelList) {
        this.context = context;
        this.carModelList = carModelList;
        this.apiService = RetrofitClient.getClient().create(APIService.class);
    }
    public void updateData(List<CarModel> newCarList) {
        this.carModelList = new ArrayList<>(newCarList);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Locale vietnamLocale = new Locale("vi", "VN");
        NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
        CarModel car = carModelList.get(position);
        holder.tvName.setText(car.getTen());
        holder.tvNamSX.setText(String.valueOf(car.getNamSX()));
        holder.tvHang.setText(car.getHang());
        holder.tvGia.setText(String.valueOf(car.getGia()));

        String imageUrl = car.getAnh();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_broken_image_24)
                    .error(R.drawable.baseline_broken_image_24)
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.baseline_broken_image_24);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
            intent.putExtra("car_id", car.getId());
            intent.putExtra("ten", car.getTen());
            intent.putExtra("hang", car.getHang());
            intent.putExtra("namSX", car.getNamSX());
            intent.putExtra("gia", car.getGia());
            intent.putExtra("anh", car.getAnh());
            intent.putExtra("mota",car.getMota());
            context.startActivity(intent);
        });
        // Handle edit button
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditCarActivity.class);
            intent.putExtra("car_id", car.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        // Handle delete button
        holder.btnDelete.setOnClickListener(v -> deleteCar(car.getId(), position));

        holder.addToCart.setOnClickListener(v -> {
            Intent intent = new Intent(context, LocationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Id", car.getId()); // Sử dụng `car`
            // Nếu `CarModel` không có `getProductTypeId`, hãy thêm phương thức đó hoặc loại bỏ dòng này.
            bundle.putString("ten", car.getTen());
            bundle.putInt("namSX", car.getNamSX()); // Tương tự, xác minh thuộc tính này.
            bundle.putString("hang", car.getHang()); // Tương tự, xác minh thuộc tính này.
            bundle.putDouble("gia", car.getGia());
            bundle.putString("anh", car.getAnh());
            intent.putExtra("car_id", car.getId());
            intent.putExtra("ten", car.getTen());
            intent.putExtra("hang", car.getHang());
            intent.putExtra("namSX", car.getNamSX());
            intent.putExtra("gia", car.getGia());
            intent.putExtra("anh", car.getAnh());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return carModelList.size();
    }

    private void deleteCar(String carId, int position) {
        if (carId == null || carId.isEmpty()) {
            Log.e("CarAdapter", "Car ID is null or empty. Cannot delete.");
            Toast.makeText(context, "Error: Invalid car ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = apiService.deleteCar(carId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    carModelList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Car deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("CarAdapter", "Error response: " + response.code() + " - " + response.message());
                    Toast.makeText(context, "Error deleting car", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CarAdapter", "API call failed: " + t.getMessage());
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvNamSX, tvHang, tvGia;
        private ImageView imgAvatar;
        CardView addToCart;
        private ImageButton btnEdit, btnDelete;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvNamSX = itemView.findViewById(R.id.tvNamSX);
            tvHang = itemView.findViewById(R.id.tvHang);
            tvGia = itemView.findViewById(R.id.tvGia);
            imgAvatar = itemView.findViewById(R.id.imgAvatatr);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            addToCart = itemView.findViewById(R.id.addToCart);
        }
    }
}
