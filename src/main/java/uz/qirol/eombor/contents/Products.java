package uz.qirol.eombor.contents;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.qirol.eombor.model.*;
import uz.qirol.eombor.repository.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@Controller
@RequestMapping(value = "/products")
public class Products {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AcceptedRepository acceptedRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @GetMapping(value = "/getall")
    public ResponseEntity<?> getAllProducts(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("bad");
        }

        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);

        return ResponseEntity.ok(productRepository.findAllByMarketId(reqAccepted.getMarketId()));
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product, Principal principal){

        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserIdAndMarketId(user.getId(), product.getMarketId()).isPresent()){
            return ResponseEntity.ok("market");
        }

        if (productRepository.findByName(product.getName()).isPresent()){
            return ResponseEntity.ok("bor");
        }

        product.setLastUpdate(LocalDateTime.now());

        return ResponseEntity.ok(productRepository.save(product));
    }

    @GetMapping(value = "/sells")
    public ResponseEntity<?> getSells(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        return ResponseEntity.ok(transactionRepository.findAllByMarketId(reqAccepted.getMarketId()));
    }

    @PostMapping(value = "/sell")
    public ResponseEntity<?> sellProduct(@RequestBody SellProduct sellProduct, Principal principal){

        if (!productRepository.findById(sellProduct.getId()).isPresent()){
            return ResponseEntity.ok("product");
        }

        Product product = productRepository.findById(sellProduct.getId()).orElse(null);
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (!acceptedRepository.findByUserIdAndMarketId(user.getId(), product.getMarketId()).isPresent()){
            return ResponseEntity.ok("user");
        }
        if (product.getSoni() < sellProduct.getCount()){
            return ResponseEntity.ok("count");
        }
        Transaction transaction = new Transaction();
        transaction.setMarketId(product.getMarketId());
        transaction.setProductId(product.getId());
        transaction.setUser(user.getName());
        transaction.setUserId(user.getId());
        transaction.setProductName(product.getName());
        transaction.setSellDate(LocalDateTime.now());
        transaction.setSoni(sellProduct.getCount());
        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateProduct(@RequestBody Product product, Principal principal){

        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserIdAndMarketId(user.getId(), product.getMarketId()).isPresent()){
            return ResponseEntity.ok("bad");
        }

        product.setLastUpdate(LocalDateTime.now());

        productRepository.save(product);
        return ResponseEntity.ok(productRepository.findAllByMarketId(product.getMarketId()));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getProds(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }

        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        return ResponseEntity.ok(sellerRepository.findAllByMarketId(reqAccepted.getMarketId()));
    }

    @GetMapping(value = "/all/simplefilter/{id}")
    public ResponseEntity<?> getFiltered(@PathVariable("id") Long id, Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }

        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        return ResponseEntity.ok(
                sellerRepository.findAllByMarketIdAndType1(
                        reqAccepted.getMarketId(),
                        id
                )
        );
    }

    @GetMapping(value = "/all/filter/{id1}/{id2}")
    public ResponseEntity<?> getFil(
            @PathVariable("id1") Long id1,@PathVariable("id2") Long id2, Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }

        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        return ResponseEntity.ok(
                sellerRepository.findAllByMarketIdAndType1AndAndType2(
                        reqAccepted.getMarketId(), id1, id2));
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getFilteredProducts(@PathVariable("id") Long id, Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }

        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);

        return ResponseEntity.ok(
                productRepository.findAllByMarketIdAndType1(
                        reqAccepted.getMarketId(), id));
    }

    @GetMapping(value = "/get/{id1}/{id2}")
    public ResponseEntity<?> lla(
            @PathVariable("id1") Long id1, @PathVariable("id2") Long id2, Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }

        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        return ResponseEntity.ok(productRepository.findAllByMarketIdAndType1AndType2(
                reqAccepted.getMarketId(), id1, id2));
    }
}
