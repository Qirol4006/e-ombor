package uz.qirol.eombor.contents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.qirol.eombor.model.*;
import uz.qirol.eombor.repository.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
    @Autowired
    private SellProds sellProds;

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
    public ResponseEntity<?> sellProduct(@RequestBody List<SellProduct> sellProduct, Principal principal){

        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }

        ReqAccepted accepted = acceptedRepository.findByUserId(user.getId()).orElse(null);

        Transaction transaction = new Transaction();

        transaction.setSoni((long) sellProduct.size());
        transaction.setSellDate(LocalDateTime.now());
        transaction.setUserId(user.getId());
        transaction.setMarketId(accepted.getMarketId());
        transaction.setUser(user.getName());

        Transaction transaction1 = transactionRepository.save(transaction);

        sellProds.deleteAll();
        sellProduct.forEach( u -> {
            u.setMarketId(accepted.getMarketId());
            u.setTransactionId(transaction1.getId());
        });
        sellProds.saveAll(sellProduct);

        return ResponseEntity.ok("Saved!");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getProductInfo(@PathVariable("id") Long id, Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }
        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        Product product = productRepository.findById(id).orElse(null);
        if (!product.getMarketId().equals(reqAccepted.getMarketId())){
            return ResponseEntity.ok("market");
        }
        return ResponseEntity.ok(product);
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

    @GetMapping(value = "/del/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") Long id, Principal principal){
        if (!productRepository.findById(id).isPresent()){
            return ResponseEntity.ok("product");
        }
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }
        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        Product product = productRepository.findById(id).orElse(null);
        if (!product.getMarketId().equals(reqAccepted.getMarketId())){
            return ResponseEntity.ok("market");
        }
        productRepository.deleteById(product.getId());
        return ResponseEntity.ok("Deleted");
    }
}
