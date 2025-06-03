package com.t8.backend.t8.backend.security.service;

import com.t8.backend.t8.backend.security.config.UserInfoUserDetails;
import com.t8.backend.t8.backend.security.entity.UserInfo;
import com.t8.backend.t8.backend.security.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserInfoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) 
         throws UsernameNotFoundException {
        Optional<UserInfo> optionalUserInfo = repository.findByEmail(username);
        return optionalUserInfo.map(userInfo -> new UserInfoUserDetails(userInfo))
                //optionalUserInfo.map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }
   
}