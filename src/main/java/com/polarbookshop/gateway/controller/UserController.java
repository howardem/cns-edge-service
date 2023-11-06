package com.polarbookshop.gateway.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarbookshop.gateway.model.User;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping
	public Mono<User> getAuthenticatedUser(@AuthenticationPrincipal OidcUser oidcUser) {
		return Mono.just(new User(oidcUser.getPreferredUsername(), oidcUser.getGivenName(), oidcUser.getFamilyName(), List.of("employee", "customer")));
	}

}
