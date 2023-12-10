package vttp.ssf.day15cartprac.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.ssf.day15cartprac.model.Item;
import vttp.ssf.day15cartprac.repository.CartRepository;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepo;

    public List<Item> getCart(String name) {
        List<Item> cart = cartRepo.getCart(name);
        return cart;
    }

    public void save(String name, List<Item> cart){
        cartRepo.deleteCart(name);
        cartRepo.addCart(name, cart);
    }
}
