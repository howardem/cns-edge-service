package com.polarbookshop.gateway.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.polarbookshop.gateway.config.SecurityConfiguration;
import com.polarbookshop.gateway.model.User;

@WebFluxTest(UserController.class)
@Import(SecurityConfiguration.class)
class UserControllerTests {

	@Autowired
	WebTestClient webClient; 

	@MockBean
	ReactiveClientRegistrationRepository clientRegistrationRepository;


	@Test
	void whenNotAuthenticatedThen401() {
		this.webClient.get()
			.uri("/user")
			.exchange()
			.expectStatus().isUnauthorized();
	}

	@Test
	void whenAuthenticatedThenReturnUser() {
		User expectedUser = new User("jon.snow", "Jon", "Snow", List.of("employee", "customer"));
		
		this.webClient.mutateWith(configureMockOidcLoginMutator(expectedUser))
			.get()
			.uri("/user")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(User.class)
			.value(user -> assertThat(user).isEqualTo(expectedUser));
	}

	private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLoginMutator(User user) {
		return SecurityMockServerConfigurers.mockOidcLogin()
				.idToken(builder -> {
					builder.claim(StandardClaimNames.PREFERRED_USERNAME, user.username());
					builder.claim(StandardClaimNames.GIVEN_NAME, user.firstName());
					builder.claim(StandardClaimNames.FAMILY_NAME, user.lastName());
				});
	}

}
