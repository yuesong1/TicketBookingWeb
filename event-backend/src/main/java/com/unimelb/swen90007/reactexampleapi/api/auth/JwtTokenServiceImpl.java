package com.unimelb.swen90007.reactexampleapi.api.auth;

import com.unimelb.swen90007.reactexampleapi.api.objects.Token.RefreshToken;
import com.unimelb.swen90007.reactexampleapi.api.objects.Token.RefreshTokenRepository;
import com.unimelb.swen90007.reactexampleapi.api.objects.Token.Token;
import com.unimelb.swen90007.reactexampleapi.api.objects.Token.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.crypto.SecretKey;

public class JwtTokenServiceImpl implements TokenService {

    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_AUTHORITIES = "authorities";

    private SecretKey key;
    private final RefreshTokenRepository repository;
    private final String issuer;
    private final int timeToLiveSeconds;
    private final String secret;

    public JwtTokenServiceImpl(RefreshTokenRepository repository, String issuer, int timeToLiveSeconds, String secret) {
        this.repository = repository;
        this.issuer = issuer;
        this.timeToLiveSeconds = timeToLiveSeconds;
        this.secret = secret;
    }

    @Override
    public UsernamePasswordAuthenticationToken readToken(String accessToken) {
        try {
            var jws = parse(accessToken);
            return new UsernamePasswordAuthenticationToken(getUsername(jws.getBody()), null, getAuthorities(jws.getBody()));
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Token expired");
        } catch (JwtException e) {
            throw new BadCredentialsException("Bad token");
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Claims body) {
        return (Collection<? extends GrantedAuthority>) body.get(CLAIM_AUTHORITIES);
    }

    private String getUsername(Claims body) {
        return body.getSubject();
    }

    @Override
    public Token createToken(UserDetails user) {
        return generateToken(user.getUsername(), user.getAuthorities());
    }

    @Override
    public Token refresh(String accessToken, String refreshTokenId) {
        var claims = parse(accessToken).getBody(); // Get the Claims from Jws
        var refresh = repository.get(refreshTokenId)
                .orElseThrow(() -> new BadCredentialsException("Bad refresh token"));
        if (!refresh.getTokenId().equals(claims.getId())) {
            throw new BadCredentialsException("Bad refresh token");
        }
        repository.delete(refreshTokenId);
        return generateToken(getUsername(claims), getAuthorities(claims));
    }

    @Override
    public void logout(String username) {
        repository.deleteAllForUsername(username);
    }

    private Token generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        var now = new Date();
        var expires = Date.from(now.toInstant().plusSeconds(timeToLiveSeconds));
        var id = UUID.randomUUID().toString();
        var tokenStr = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(username)
                .setAudience(issuer)
                .setExpiration(expires)
                .setNotBefore(now)
                .setIssuedAt(now)
                .setId(id)
                .claim(CLAIM_USERNAME, username)
                .claim(CLAIM_AUTHORITIES, authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .signWith(getKey())
                .compact();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(UUID.randomUUID().toString());
        refreshToken.setTokenId(id);
        refreshToken.setUsername(username);
        repository.save(refreshToken);

        var token = new Token();
        token.setAccessToken(tokenStr);
        token.setRefreshToken(refreshToken.getId());
        return token;
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .requireAudience(issuer)
                .build()
                .parseClaimsJws(token);
    }

    private SecretKey getKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }
}
