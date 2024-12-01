package fpoly.md19304.asm_and103;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;  // Add context
    private List<CartItem> cartItems;

    // Updated constructor to accept context as well
    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.productName.setText(cartItem.getProductName());
        holder.productPrice.setText(String.format("%,.0f VND", cartItem.getPrice()));
        holder.productQuantity.setText(String.valueOf(cartItem.getQuantity()));

        // Use Picasso to load image, ensuring context is passed
        Picasso.get().load(cartItem.getImageUrl()).into(holder.productImage);

        // Set item click listener to show confirmation dialog
        holder.itemView.setOnClickListener(view -> {
            // Show confirmation dialog
            new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Do you want to delete this item from your cart?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Remove item from the list and notify adapter
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItems.size());
                        Toast.makeText(context, "Item deleted.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvCartItemName);
            productPrice = itemView.findViewById(R.id.tvCartItemPrice);
            productQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            productImage = itemView.findViewById(R.id.imgCartItem);
        }
    }
}
