package uz.qirol.eombor.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.qirol.eombor.model.Market;
import uz.qirol.eombor.model.Permission;
import uz.qirol.eombor.model.User;
import uz.qirol.eombor.repository.MarketNameRepository;
import uz.qirol.eombor.repository.MarketRepository;
import uz.qirol.eombor.repository.PermissionRequest;
import uz.qirol.eombor.repository.UserRepository;

import java.security.Principal;

@RestController
@Controller
@RequestMapping(value = "/user")
public class ApplicationUserManagement {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private PermissionRequest permissionRequest;

    @Autowired
    private MarketNameRepository marketNameRepository;

    private final PasswordEncoder passwordEncoder;

    public ApplicationUserManagement(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){

        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.ok("username");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.ok("email");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User user1 = userRepository.save(user);

        user1.setPassword("");

        return ResponseEntity.ok(user1);
    }

    @PostMapping(value = "/register/market")
    public ResponseEntity<?> registerMarket (@RequestBody Market market){

        if (marketRepository.findByName(market.getName()).isPresent()){
            return ResponseEntity.ok("name");
        }

        if (marketRepository.findByUsername(market.getUsername()).isPresent()){
            return ResponseEntity.ok("username");
        }

        if (marketRepository.findByEmail(market.getEmail()).isPresent()){
            return ResponseEntity.ok("email");
        }
        market.setPassword(passwordEncoder.encode(market.getPassword()));
        Market market1 = marketRepository.save(market);
        market1.setPassword("");
        return ResponseEntity.ok(market1);
    }

    @GetMapping(value = "/about/market")
    public ResponseEntity<?> getDetailMarket(Principal principal){

        Market market = marketRepository.findByUsername(principal.getName()).orElse(null);
        assert market != null;
        return ResponseEntity.ok(market);
    }

    @GetMapping(value = "/about/user")
    public ResponseEntity<?> getDetailUser(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        assert user != null;
        user.setPassword("");
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/all/magazine")
    public ResponseEntity<?> allMazines(){
        return ResponseEntity.ok(
                marketNameRepository.findAll()
        );
    }

    @PostMapping(value = "/getpermission")
    public ResponseEntity<?> getPermission(@RequestBody Permission permission,
                                           Principal principal){

        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        assert user != null;

        if (!user.getId().equals(permission.getUserId())){
            return ResponseEntity.ok("user");
        }

        if (!marketRepository.findById(permission.getMagazinId()).isPresent()){
            return ResponseEntity.ok("market");
        }

        if (permissionRequest.findByUserIdAndAndMagazinId(
                user.getId(),
                permission.getMagazinId())
                .isPresent()
        ){
            return ResponseEntity.ok("bad");
        }

        return ResponseEntity.ok(permissionRequest.save(permission));
    }

    @GetMapping(value = "/permissionrequsts")
    public ResponseEntity<?> perReqs(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        Permission permission = permissionRequest.findByUserId(user.getId()).orElse(null);

        Market market = marketRepository.findById(permission.getMagazinId()).orElse(null);

        assert market != null;
        market.setPassword("");

        return ResponseEntity.ok(market);
    }


}
