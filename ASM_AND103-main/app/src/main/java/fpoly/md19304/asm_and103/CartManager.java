package fpoly.md19304.asm_and103;

import java.util.ArrayList;

public class CartManager {
    private static CartManager instance;
    private ArrayList<Cart> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(Cart item) {
        for (Cart cartItem : cartItems) {
            if (cartItem.getName().equals(item.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(item);
    }

    public ArrayList<Cart> getCartItems() {
        return cartItems;
    }

    // Phương thức để xóa giỏ hàng
    public void clearCart() {
        cartItems.clear();
    }
}
