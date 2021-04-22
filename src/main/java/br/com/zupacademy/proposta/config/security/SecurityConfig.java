package br.com.zupacademy.proposta.config.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@Import({ KeycloakSpringBootConfigResolver.class })
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

	/*
	 * Método utilizado para registrar o Keycloak como o provedor de autenticação
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
		auth.authenticationProvider(keycloakAuthenticationProvider);
	}

	/*
	 * Definindo uma estratégia para autenticação de sessão **Registra uma sessão de
	 * usuário após a autenticação bem-sucedida**
	 */
	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	/*
	 * O bean HttpSessionManager já está definido na biblioteca do adaptador
	 * Keycloak. A anotação @ConditionalOnMissingBean indica que o Bean abaixo será
	 * carregado condicionalmente apenas se nenhum outro bean desse tipo tiver sido
	 * definido.
	 */
	
	@Bean
	@Override
	@ConditionalOnMissingBean(HttpSessionManager.class)
	protected HttpSessionManager httpSessionManager() {
		return new HttpSessionManager();
	}

	/*
	 * Definindo políticas de segurança para acesso aos endpoints da aplicação
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		http.authorizeRequests()
//			.antMatchers("/propostas/**").hasAnyRole("admin", "user")
//		   	.antMatchers("/biometrias/**").hasAnyRole("admin", "user")
//		   	.antMatchers("/bloqueio-cartoes/**").hasAnyRole("admin", "user")
//			.antMatchers("/cartao/**").hasAnyRole("admin", "user")
			.anyRequest().permitAll()
			.and().cors()
			.and().csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		//	Configuração realizada para acessar o banco de dados em memória H2
			.and().headers().frameOptions().sameOrigin();
	}
	
}