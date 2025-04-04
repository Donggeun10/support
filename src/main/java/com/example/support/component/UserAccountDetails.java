package com.example.support.component;

import com.example.support.configuration.CacheConfig;
import com.example.support.entity.Member;
import com.example.support.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserAccountDetails implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = CacheConfig.APPS, key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> memberOpt = memberRepository.findByMemberId(username);
        if(memberOpt.isEmpty()) {
            log.warn("Could not found user {}", username);
            throw new UsernameNotFoundException("Could not found user" + username);
        }

        Member member = memberOpt.get();
        if(log.isDebugEnabled()) {
            log.debug("Success find member {}", member);
        }

        return User.builder()
            .username(member.getMemberId())
            .password(passwordEncoder.encode(member.getPassword()))
            .roles("USER")
            .build();

    }
}
