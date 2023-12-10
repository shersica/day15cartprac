package vttp.ssf.day15cartprac.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.ssf.day15cartprac.model.Item;
import vttp.ssf.day15cartprac.model.Utils;
import vttp.ssf.day15cartprac.service.CartService;

@Controller
@RequestMapping(path="/cart")
public class CartController {
    
    @Autowired
    private CartService cartSvc;

    @GetMapping
    public String getCart(@RequestParam String name, Model model, HttpSession sess){
        List<Item> cart = cartSvc.getCart(name);
        //storing the cart in the session
        sess.setAttribute("cart", cart);

        model.addAttribute("username", name);
        model.addAttribute("item", new Item());
        model.addAttribute("cart", cart);

        return "cart";
    } 

    @PostMapping
    public ModelAndView postCart(@Valid @ModelAttribute("item") Item item, BindingResult result, HttpSession sess, @RequestParam String username){
        ModelAndView mav = new ModelAndView();

        System.out.printf("item: %s\n", item);
        System.out.printf("item: %s\n", item.getName());

        System.out.printf("error: %b\n", result.hasErrors());

        System.out.printf(">>> @RequestParam: %s\n", username);
        // System.out.printf(">>> @RequestBody: %s\n", body);

        if(result.hasErrors()){
            mav.setStatus(HttpStatusCode.valueOf(400));
            mav.setViewName("cart");

            return mav;
        }

        //Storing updated cart in the session
        List<Item> cart = Utils.getCart(sess);
        // String name = (String) sess.getAttribute("username");
        cart.add(item);
        for(Item i : cart ){
            System.out.println(i);
        }
        sess.setAttribute("cart", cart);

        mav.addObject("item", new Item());  //why new item
        mav.addObject("username", username);
        mav.setStatus(HttpStatusCode.valueOf(200));
        mav.setViewName("cart");

        return mav;
    }

    @PostMapping("/checkout")
    public String postCartCheckout(HttpSession sess, @RequestParam String username){
        List<Item> cart = Utils.getCart(sess);

        cartSvc.save(username, cart);
        sess.invalidate();

        return "redirect:/";
    }
}
