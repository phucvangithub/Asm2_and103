//package fpoly.md19304.asm_and103;
//
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private Uri selectedImageUri;
//
//    ListView lvMain;
//    List<CarModel> listCarModel;
//    CarAdapter carAdapter;
//    APIService apiService;
//    private ImageView selectedImageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        lvMain = findViewById(R.id.listviewMain);
//        FloatingActionButton btnAddCar = findViewById(R.id.btn_add);
//
//        // Tạo Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(APIService.DOMAIN)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        apiService = retrofit.create(APIService.class);
//
//        // Khởi tạo danh sách trống
//        listCarModel = new ArrayList<>();
//
//        // Lấy danh sách xe từ API
//        loadCarList();
//
//        // Mở hộp thoại thêm xe khi nhấn nút "Thêm xe"
//        btnAddCar.setOnClickListener(v -> showAddCarDialog());
//    }
//
//    private void loadCarList() {
//        Call<List<CarModel>> call = apiService.getCars();
//        call.enqueue(new Callback<List<CarModel>>() {
//            @Override
//            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    listCarModel = response.body();
//                    carAdapter = new CarAdapter(getApplicationContext(), listCarModel);
//                    lvMain.setAdapter(carAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<CarModel>> call, Throwable t) {
//                Log.e("Main", t.getMessage());
//            }
//        });
//    }
//
//    private void showAddCarDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_add_car, null);
//        builder.setView(dialogView);
//
//        EditText etCarName = dialogView.findViewById(R.id.etCarName);
//        EditText etCarYear = dialogView.findViewById(R.id.etCarYear);
//        EditText etCarBrand = dialogView.findViewById(R.id.etCarBrand);
//        EditText etCarPrice = dialogView.findViewById(R.id.etCarPrice);
//        ImageView ivSelectedImage = dialogView.findViewById(R.id.ivSelectedImage);
//        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
//
//        // Mở thư viện khi nhấn nút chọn ảnh
//        btnSelectImage.setOnClickListener(v -> openImagePicker());
//
//        // Lưu `ivSelectedImage` để cập nhật sau khi chọn ảnh
//        selectedImageView = ivSelectedImage;
//
//        // Xử lý sự kiện nút "Lưu"
//        Button btnSaveCar = dialogView.findViewById(R.id.btnSaveCar);
//        btnSaveCar.setOnClickListener(v -> {
//            String carName = etCarName.getText().toString();
//            String carYearStr = etCarYear.getText().toString();
//            String carBrand = etCarBrand.getText().toString();
//            String carPriceStr = etCarPrice.getText().toString();
//
//            if (!carName.isEmpty() && !carBrand.isEmpty()) {
//                int carYear = Integer.parseInt(carYearStr);
//                double carPrice = Double.parseDouble(carPriceStr);
//
//                String carImageUrl = selectedImageUri != null ? selectedImageUri.toString() : "";
//
//                CarModel newCar = new CarModel(carName, carYear, carBrand, carPrice, carImageUrl);
//
//                addCarToApi(newCar);
//
//                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(MainActivity.this, "Tên và hãng xe là bắt buộc", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
//        builder.create().show();
//    }
//
//    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            selectedImageUri = data.getData();
//
//            // Cập nhật `selectedImageView` thay vì gọi `findViewById` lại
//            if (selectedImageView != null) {
//                selectedImageView.setImageURI(selectedImageUri);
//            }
//        }
//    }
//
//
//    private void addCarToApi(CarModel newCar) {
//        // Chuyển đổi các trường dữ liệu của xe thành RequestBody
//        RequestBody ten = RequestBody.create(newCar.getTen(), okhttp3.MultipartBody.FORM);
//        RequestBody namSX = RequestBody.create(String.valueOf(newCar.getNamSX()), okhttp3.MultipartBody.FORM);
//        RequestBody hang = RequestBody.create(newCar.getHang(), okhttp3.MultipartBody.FORM);
//        RequestBody gia = RequestBody.create(String.valueOf(newCar.getGia()), okhttp3.MultipartBody.FORM);
//
//        MultipartBody.Part anhPart = null;
//
//        // Nếu ảnh được chọn, chuyển đổi URI ảnh thành File và RequestBody
//        if (selectedImageUri != null) {
//            try {
//                // Lấy InputStream từ ContentResolver
//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                // Tạo tệp tạm trong bộ nhớ cache
//                File tempFile = new File(getCacheDir(), "temp_image.jpg");
//                OutputStream outputStream = new FileOutputStream(tempFile);
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = inputStream.read(buffer)) > 0) {
//                    outputStream.write(buffer, 0, length);
//                }
//                outputStream.close();
//                inputStream.close();
//
//                // Tạo RequestBody từ tệp tạm
//                RequestBody requestFile = RequestBody.create(tempFile, MediaType.parse("image/*"));
//                anhPart = MultipartBody.Part.createFormData("anh", tempFile.getName(), requestFile);
//            } catch (Exception e) {
//                Log.e("AddCar", "Error converting image: " + e.getMessage());
//            }
//        }
//
//        // Gọi API để thêm xe với hình ảnh
//        Call<CarModel> call = apiService.addcar(ten, namSX, hang, gia, anhPart);
//        call.enqueue(new Callback<CarModel>() {
//            @Override
//            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    listCarModel.add(response.body());
//                    carAdapter.notifyDataSetChanged();
//                    loadCarList();
//                    Toast.makeText(MainActivity.this, "Thêm xe thành công", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Thêm xe không thành công: " + response.message(), Toast.LENGTH_SHORT).show();
//                    Log.e("AddCar", "Response error: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CarModel> call, Throwable t) {
//                Log.e("AddCar", "Error: " + t.getMessage());
//                Toast.makeText(MainActivity.this, "Lỗi khi thêm xe", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}
//
//
//
//

package fpoly.md19304.asm_and103;

import static java.security.AccessController.getContext;
import static fpoly.md19304.asm_and103.APIService.DOMAIN;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ViewPager bannerViewPager;
    private BannerAdapter bannerAdapter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    RecyclerView lvMain;
    List<CarModel> listCarModel;
    CarAdapter carAdapter;
    APIService apiService;
    private ImageView selectedImageView;
    private CircleIndicator circleIndicator;
    private int currentPage = 0;
    private Handler handler = new Handler();
    private EditText edSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerViewPager =findViewById(R.id.banner_viewpager);
        circleIndicator = findViewById(R.id.circleIndicator);
        ImageView giohang = findViewById(R.id.btn_cart);
        giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        List<Integer> listPhoto = new ArrayList<>();
        listPhoto.add(R.drawable.banner);
        listPhoto.add(R.drawable.tintuc1);
        listPhoto.add(R.drawable.tintuc3);
        listPhoto.add(R.drawable.tintuc4);

        bannerAdapter = new BannerAdapter(MainActivity.this, listPhoto);
        bannerViewPager.setAdapter(bannerAdapter);
        circleIndicator.setViewPager(bannerViewPager);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bannerAdapter.getCount() != 0) {
                    currentPage = (currentPage + 1) % bannerAdapter.getCount();
                    bannerViewPager.setCurrentItem(currentPage, true);
                }
                handler.postDelayed(this, 3000);
            }
        }, 3000);

        lvMain = findViewById(R.id.listviewMain);
        FloatingActionButton btnAddCar = findViewById(R.id.btn_add);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        // Khởi tạo danh sách trống
        listCarModel = new ArrayList<>();

        // Lấy danh sách xe từ API
        loadCarList();

        // Mở hộp thoại thêm xe khi nhấn nút "Thêm xe"
        btnAddCar.setOnClickListener(v -> showAddCarDialog());
    }

    private void loadCarList() {

        Call<List<CarModel>> call = apiService.getCars();
        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    listCarModel = response.body();
                    if (listCarModel.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No cars available.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    carAdapter = new CarAdapter(MainActivity.this, listCarModel);
                    lvMain.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    lvMain.setAdapter(carAdapter);
                } else {
                    Log.e("Main", "Response error: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to load cars.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Failed to load cars. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("Main", "Error: " + t.getMessage());
            }
        });
    }

    private void showAddCarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_car, null);
        builder.setView(dialogView);

        EditText etCarName = dialogView.findViewById(R.id.etCarName);
        EditText etCarYear = dialogView.findViewById(R.id.etCarYear);
        EditText etCarBrand = dialogView.findViewById(R.id.etCarBrand);
        EditText etCarPrice = dialogView.findViewById(R.id.etCarPrice);
        ImageView ivSelectedImage = dialogView.findViewById(R.id.ivSelectedImage);
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        EditText etCarMoTa = dialogView.findViewById(R.id.etmota);
        // Mở thư viện khi nhấn nút chọn ảnh
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        // Lưu `ivSelectedImage` để cập nhật sau khi chọn ảnh
        selectedImageView = ivSelectedImage;

        // Xử lý sự kiện nút "Lưu"
        Button btnSaveCar = dialogView.findViewById(R.id.btnSaveCar);
        btnSaveCar.setOnClickListener(v -> {
            String carName = etCarName.getText().toString();
            String carYearStr = etCarYear.getText().toString();
            String carBrand = etCarBrand.getText().toString();
            String carPriceStr = etCarPrice.getText().toString();
            String carMoTa = etCarMoTa.getText().toString();

            if (!carName.isEmpty() && !carBrand.isEmpty()) {
                int carYear = Integer.parseInt(carYearStr);
                double carPrice = Double.parseDouble(carPriceStr);

                // Nếu đã chọn ảnh, chuyển URI thành File tạm
                if (selectedImageUri != null) {
                    File imageFile = createFileFromUri(selectedImageUri, "temp_image");
                    if (imageFile != null) {
                        // Sử dụng URL đầy đủ cho ảnh
                        String carImageUrl = DOMAIN + imageFile.getName();
                        CarModel newCar = new CarModel(carName, carYear, carBrand, carPrice, carImageUrl,carMoTa);
                        addCarToApi(newCar, imageFile);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Tên và hãng xe là bắt buộc", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Cập nhật `selectedImageView` với ảnh đã chọn
            if (selectedImageView != null) {
                selectedImageView.setImageURI(selectedImageUri);
            }
        }
    }

    private File createFileFromUri(Uri uri, String name) {
        File tempFile = new File(getCacheDir(), name + ".jpg");
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            Log.e("AddCar", "Error converting image: " + e.getMessage());
            return null;
        }
    }

    private void addCarToApi(CarModel newCar, File imageFile) {
        RequestBody ten = RequestBody.create(newCar.getTen(), okhttp3.MultipartBody.FORM);
        RequestBody namSX = RequestBody.create(String.valueOf(newCar.getNamSX()), okhttp3.MultipartBody.FORM);
        RequestBody hang = RequestBody.create(newCar.getHang(), okhttp3.MultipartBody.FORM);
        RequestBody gia = RequestBody.create(String.valueOf(newCar.getGia()), okhttp3.MultipartBody.FORM);

        MultipartBody.Part anhPart = MultipartBody.Part.createFormData(
                "anh", imageFile.getName(), RequestBody.create(imageFile, MediaType.parse("image/*"))
        );
        RequestBody mota = RequestBody.create(newCar.getMota(), okhttp3.MultipartBody.FORM);
        Call<CarModel> call = apiService.addcar(ten, namSX, hang, gia, anhPart,mota);
        call.enqueue(new Callback<CarModel>() {
            @Override
            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listCarModel.add(response.body());
                    carAdapter.notifyDataSetChanged();
                    loadCarList();
                    Toast.makeText(MainActivity.this, "Thêm xe thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Thêm xe không thành công: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("AddCar", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CarModel> call, Throwable t) {
                Log.e("AddCar", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi khi thêm xe", Toast.LENGTH_SHORT).show();
            }
        });
        edSearch = findViewById(R.id.ed_search);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCars(s.toString()); // Gọi hàm lọc danh sách
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý sau khi văn bản thay đổi
            }
        });

        // Các đoạn code khác trong onCreate()
    }

    // Hàm lọc danh sách xe
    private void filterCars(String query) {
        List<CarModel> filteredList = new ArrayList<>();
        for (CarModel car : listCarModel) {
            if (car.getTen().toLowerCase().contains(query.toLowerCase()) ||
                    car.getHang().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(car);
            }
        }

        if (carAdapter != null) {
            carAdapter.updateData(filteredList); // Gọi hàm cập nhật trong adapter
        }
    }
}

