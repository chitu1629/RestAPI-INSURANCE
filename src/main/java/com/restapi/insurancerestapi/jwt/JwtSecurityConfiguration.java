package com.restapi.insurancerestapi.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class JwtSecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                auth -> {
                    auth.anyRequest().authenticated();      //every request must be authenticated
                });

        http.sessionManagement(
                session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)    //disabling session creation
        );

        http.httpBasic();   //basic authentication popup

        http.csrf().disable();  //disabling csrf - don't need csrf token to be sent along with authorization header

        http.headers().frameOptions().sameOrigin();  //enabling frames for h2 console

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);     //enabling jwt

        return http.build();
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)   //h2
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)    // provides user-details ddl
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {        //password hashing
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailService(DataSource dataSource) {

        var admin = User.withUsername("admin")                          //user1
                .password("password")
                .passwordEncoder(str -> passwordEncoder().encode(str))  //hash version of password will be stored
                .roles("ADMIN", "USER")                                 //authority
                .build();

        var user = User.withUsername("user")                            //user2
                .password("password")
                .passwordEncoder(str -> passwordEncoder().encode(str))  //hash version of password will be stored
                .roles("USER")                                          //authority
                .build();

        var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);    //storing the user-details in h2 using datasource
        jdbcUserDetailsManager.createUser(user);
        jdbcUserDetailsManager.createUser(admin);

        return jdbcUserDetailsManager;
    }

    //enabling jwt - oauth resource server, we need a jwt decoder.
    //before creating a jwtdecoder, we need to create
    // 1.keypair - public and private key,
    // 2.RSA keyObject
    // 3.JWKSource ->Json web key source - create a JwkSet and return a JwkSource interface(get method)

    @Bean
    public KeyPair keyPair() {          //generates a RSA keyPair
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Bean
    public RSAKey rsaKey(KeyPair keyPair) {        //creating RSAKey Object

        return new RSAKey
                .Builder((RSAPublicKey) keyPair.getPublic()) //public key
                .privateKey(keyPair.getPrivate())   //private key
                .keyID(UUID.randomUUID().toString())  //keyID
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        var jwkSet = new JWKSet(rsaKey);    //create JWKSet using rsa keyObject

        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())     //decoder with public key
                .build();

    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);     //NimbusJwtEncoder uses private key for encoding
    }
}
