package uz.qirol.eombor.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.qirol.eombor.model.Market;
import uz.qirol.eombor.model.User;
import uz.qirol.eombor.repository.MarketRepository;
import uz.qirol.eombor.repository.UserRepository;

@RestController
@Controller
@RequestMapping(value = "/user")
public class ApplicationUserManagement {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarketRepository marketRepository;

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
        market.setPassword(passwordEncoder.encode(market.getPassword()));
        Market market1 = marketRepository.save(market);
        market1.setPassword("");
        return ResponseEntity.ok(market1);
    }
}
