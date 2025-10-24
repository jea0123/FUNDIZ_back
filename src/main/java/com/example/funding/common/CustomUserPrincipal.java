package com.example.funding.common;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * <p>커스텀 유저 프린시펄</p>
 * <p>- Spring Security의 UserDetails를 구현하여 사용자 인증 정보를 담는 객체</p>
 *
 * @param creatorId   크리에이터 ID (없으면 null)
 * @param userId      사용자 ID
 * @param email       사용자 이메일 (username 역할)
 * @param authorities 사용자 권한 목록
 * @author 장민규
 * @since 2025-08-26
 */
public record CustomUserPrincipal(Long userId, Long creatorId, String email,
                                  Collection<? extends GrantedAuthority> authorities, String role) implements UserDetails {

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}