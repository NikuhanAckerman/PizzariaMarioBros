package pwaula.trabalho.pizzariamario.s;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pwaula.trabalho.pizzariamario.s.service.ClientDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/images/**", "/css/**", "/js/**").permitAll() // Permitir acesso à página de login e arquivos estáticos
                        .anyRequest().authenticated() // Exigir autenticação para qualquer outra página
                )
                .formLogin(form -> form
                        .loginPage("/login") // Definir a URL da página de login personalizada
                        .defaultSuccessUrl("/index", true) // Redirecionar para /home após login bem-sucedido
                        .permitAll() // Permitir que todos acessem a página de login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirecionar para o login após logout
                        .permitAll()
                );

        return http.build();
    }
}

