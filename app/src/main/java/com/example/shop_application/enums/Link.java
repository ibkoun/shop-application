package com.example.shop_application.enums;

/**
 * Contains the URL to the com.example.myapplication.php files.
 */
public enum Link {
    STORE("http://your local ip address/phpmyadmin/api/store.php"),
    UPDATE_STORE("http://your local ip address/phpmyadmin/api/update_store.php"),
    CART("http://your local ip address/phpmyadmin/api/cart.php"),
    UPDATE_CART("http://your local ip address/phpmyadmin/api/update_cart.php"),
    DELETE_CART("http://your local ip address/phpmyadmin/api/delete_cart.php"),
    HISTORY("http://your local ip address/phpmyadmin/api/history.php"),
    UPDATE_HISTORY("http://your local ip address/phpmyadmin/api/update_history.php");

    private String url;

    Link(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}
