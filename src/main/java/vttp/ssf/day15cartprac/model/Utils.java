package vttp.ssf.day15cartprac.model;

import java.util.LinkedList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

public class Utils {

    // public static final String ATTR_CART = "cart";

    public static List<Item> getCart(HttpSession sess) {
        //Retrieving the cart from the session
        List<Item> cart = (List<Item>)sess.getAttribute("cart");
        if (null == cart) {
            cart = new LinkedList<>();
            sess.setAttribute("cart", cart);
        }
        return cart;
    }
   
}