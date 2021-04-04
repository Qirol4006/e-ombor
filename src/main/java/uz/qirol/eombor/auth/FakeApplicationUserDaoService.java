package uz.qirol.eombor.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.qirol.eombor.model.User;
import uz.qirol.eombor.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static uz.qirol.eombor.security.ApplicationUserRole.*;

@Repository("fake")
@CrossOrigin
public class FakeApplicationUserDaoService implements ApplicationUserDao{


    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers(username)
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }


    private List<ApplicationUser> getApplicationUsers(String opa) {


        User user = userRepository.findByUsername(opa).orElse(null);
        if (user.getType().equals("SOTUVCHI")){
            List<ApplicationUser> applicationUsers = Lists.newArrayList(
                    new ApplicationUser(
                            user.getUsername(),
                            user.getPassword(),
                            ADMINTEE.getGrantedAuthorities(),
                            true,
                            true,
                            true,
                            true
                    )
            );
            return applicationUsers;
        }

        if (user.getType().equals("BOSHLIQ")){
            List<ApplicationUser> applicationUsers = Lists.newArrayList(
                    new ApplicationUser(
                            user.getUsername(),
                            user.getPassword(),
                            ADMIN.getGrantedAuthorities(),
                            true,
                            true,
                            true,
                            true
                    )
            );
            return applicationUsers;
        }

        if (user.getType().equals("MARKET")){
            List<ApplicationUser> applicationUsers = Lists.newArrayList(
                    new ApplicationUser(
                            user.getUsername(),
                            user.getPassword(),
                            ADMIN.getGrantedAuthorities(),
                            true,
                            true,
                            true,
                            true
                    )
            );
            return applicationUsers;
        }

        List<ApplicationUser> applicationUsers = Lists.newArrayList(
                new ApplicationUser(
                        "qirol",
                        passwordEncoder.encode("Qirol!234"),
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );
        return applicationUsers;
    }
}
