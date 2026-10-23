// Chuma's territory

class cartStore {
    constructor() {
        this.cartItems = [];
    }

    addToCart(item) {
        const existingItem = this.cartItems.find(cartItem => cartItem.id === item.id);
        if (existingItem) {
            existingItem.quantity += 1;
        } else {
            this.cartItems.push({ ...item, quantity: 1 });
        }
    }

    removeFromCart(itemId) {
        this.cartItems = this.cartItems.filter(cartItem => cartItem.id !== itemId);
    }

    clearCart() {
        this.cartItems = [];
    }

    get totalPrice() {
        return this.cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    }
}

export default new cartStore();
