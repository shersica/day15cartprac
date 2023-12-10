package vttp.ssf.day15cartprac.repository;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf.day15cartprac.model.Item;

@Repository
public class CartRepository {
    
    @Autowired @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    public List<Item> getCart(String name) {
        ListOperations<String, String> listOps = template.opsForList();
        List<Item> cart = new LinkedList<>();
        if(!template.hasKey(name)){
            return cart;
        }

        long size = listOps.size(name);
        for(String i : listOps.range(name, 0, size)){
            Item item = new Item();
            item.setName(i.split(",")[0]);
            item.setQuantity(Integer.parseInt(i.split(",")[1]));
            cart.add(item);
        }

        return cart;
    }

    public void addCart(String name, List<Item> cart){

        ListOperations<String,String> listOps = template.opsForList();
        cart.stream()
            .forEach(item -> {
                String rec = "%s, %d".formatted(item.getName(), item.getQuantity());
                listOps.leftPush(name, rec);
            });

    }

    public void deleteCart(String name) {
        template.delete(name);
    }

    
}
