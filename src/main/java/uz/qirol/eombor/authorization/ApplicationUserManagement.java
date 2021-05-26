package uz.qirol.eombor.authorization;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.qirol.eombor.jwt.JwtConfig;
import uz.qirol.eombor.model.*;
import uz.qirol.eombor.repository.*;

import javax.crypto.SecretKey;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;

import static uz.qirol.eombor.security.ApplicationUserRole.ADMIN;

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

    @Autowired
    private AcceptedRepository acceptedRepository;

    private final PasswordEncoder passwordEncoder;


    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public ApplicationUserManagement(PasswordEncoder passwordEncoder, JwtConfig jwtConfig, SecretKey secretKey) {
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){

        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.ok("username");
        }

        if (marketRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.ok("market");
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

        if (userRepository.findByUsername(market.getUsername()).isPresent()){
            return ResponseEntity.ok("user");
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

    @PostMapping(value = "/market")
    public ResponseEntity<?> marketLogin(@RequestBody MarketLogin marketLogin){

        if (!marketRepository.findByUsername(marketLogin.getUsername()).isPresent()){
            return ResponseEntity.ok("xatok");
        }
        Market market = marketRepository.findByUsername(marketLogin.getUsername()).orElse(null);

        if (!passwordEncoder.matches(marketLogin.getPassword() ,market.getPassword())){
            return ResponseEntity.ok("hatok");
        }
        String token = Jwts.builder()
                .setSubject(market.getUsername())
                .claim("authorities", ADMIN.getGrantedAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return ResponseEntity.ok(jwtConfig.getTokenPrefix() + token);
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

    @GetMapping(value = "/permissionrequests")
    public ResponseEntity<?> perReqs(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!permissionRequest.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("bad");
        }

        Permission permission = permissionRequest.findByUserId(user.getId()).orElse(null);

        Market market = marketRepository.findById(permission.getMagazinId()).orElse(null);

        assert market != null;
        market.setPassword("");

        return ResponseEntity.ok(market);
    }

    @GetMapping(value = "/market/permissionrequests")
    public ResponseEntity<?> getPerReqsForMarket(Principal principal){
        Market market = marketRepository.findByUsername(principal.getName()).orElse(null);
        return ResponseEntity.ok(permissionRequest.findAllByMagazinId(market.getId()));
    }

    @GetMapping(value = "/acceptrequest/{id}")
    public ResponseEntity<?> acceptRequest(@PathVariable("id") Long id, Principal principal){

        if (!permissionRequest.findById(id).isPresent()){
            return ResponseEntity.ok("bad");
        }

        if (!permissionRequest.findById(id).isPresent()){
            return ResponseEntity.ok("bad2");
        }

        Permission permission = permissionRequest.findById(id).orElse(null);

        Market market = marketRepository.findByUsername(principal.getName()).orElse(null);

        if (!permission.getMagazinId().equals(market.getId())){
            return ResponseEntity.ok("bad3");
        }

        User user = userRepository.findById(permission.getUserId()).orElse(null);
        user.setType(permission.getPermission());
        userRepository.save(user);
        ReqAccepted reqAccepted = new ReqAccepted();

        reqAccepted.setMarketId(permission.getMagazinId());
        reqAccepted.setUserId(permission.getUserId());

        permissionRequest.deleteById(id);

        return ResponseEntity.ok(acceptedRepository.save(reqAccepted));
    }

    @GetMapping(value = "/delete/request/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable("id") Long id, Principal principal){

        if (!permissionRequest.findById(id).isPresent()){
            return ResponseEntity.ok("bad");
        }

        if (!permissionRequest.findById(id).isPresent()){
            return ResponseEntity.ok("bad2");
        }

        Permission permission = permissionRequest.findById(id).orElse(null);

        Market market = marketRepository.findByUsername(principal.getName()).orElse(null);

        if (!permission.getMagazinId().equals(market.getId())){
            return ResponseEntity.ok("bad3");
        }

        if (permissionRequest.findById(id).isPresent()){
            permissionRequest.deleteById(id);
        }else {
            return ResponseEntity.ok("NotFoundRequest");
        }


        return ResponseEntity.ok("Deleted!");
    }


    @GetMapping(value = "/getmarketid")
    public ResponseEntity<?> getMarketId(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        
        return ResponseEntity.ok(acceptedRepository.findByUserId(user.getId()));
    }

}
