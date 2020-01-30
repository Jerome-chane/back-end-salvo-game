package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}


	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {
			// save a couple of users
//			Player p1 = new Player("Jack", "Bauer", "TheRipper", "j.bauer@ctu.gov", passwordEncoder.encode("24") );
//			Player p2 = new Player("Chloe", "O'Brian", "JavaKiller", "c.obrian@ctu.gov", passwordEncoder.encode("42") );
//			Player p3 = new Player("Kim", "Bauer", "JavaSuicide","kim_bauer@gmail.com", passwordEncoder.encode("kb" ));
//			Player p4 = new Player("Tony", "Almeida", "JavaJumpWindow", "t.almeida@ctu.gov", passwordEncoder.encode("mole"));
//			playerRepository.save(p1);
//			playerRepository.save(p2);
//			playerRepository.save(p3);
//			playerRepository.save(p4);

//			Date date1 = new Date();
//			Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
//			Date date3 = Date.from(date1.toInstant().plusSeconds(100));
//			Date date4 = Date.from(date1.toInstant().plusSeconds(200));
//			Date date5 = Date.from(date1.toInstant().plusSeconds(203600));
//			Date date6 = Date.from(date1.toInstant().plusSeconds(15505000));
//
//			Game game1 = new Game(date1); Game game2 = new Game(date2);
//			Game game3 = new Game(date3); Game game4 = new Game(date4);
//			Game game5 = new Game(date5); Game game6 = new Game(date6);
//			Game game7 = new Game(date1); Game game8 = new Game(date2);
//
//			gameRepository.save(game1); gameRepository.save(game2);
//			gameRepository.save(game3); gameRepository.save(game4);
//			gameRepository.save(game5);	gameRepository.save(game6);
//			gameRepository.save(game7); gameRepository.save(game8);
//			gameRepository.save(game3);
//
//
//			String[] s1 = new String[]{"B5", "C5", "F1"};
//			String[] s2 = new String[]{"B4", "B5", "B6"};
//			String[] s3 = new String[]{"F2", "D5"};
//			String[] s4 = new String[]{"E1", "H3", "A2"};
//			String[] s5 = new String[]{"A2", "A4", "G6"};
//			String[] s6 = new String[]{"B5", "D5", "C7"};
//			String[] s7 = new String[]{"A3", "H6"};
//			String[] s8 = new String[]{"C5", "C6"};
//			String[] s9 = new String[]{"G6", "H6", "A4"};
//			String[] s10 = new String[]{"H1", "H2", "H3"};
//			String[] s11 = new String[]{"A2", "A3", "D8"};
//			String[] s12 = new String[]{"E1", "F2", "G3"};
//			String[] s13 = new String[]{"A3", "A4", "F7"};
//			String[] s14 = new String[]{"B5", "C6", "H1"};
//			String[] s15 = new String[]{"A2", "G6", "H6"};
//			String[] s16 = new String[]{"C5", "C7", "D5"};
//			String[] s17 = new String[]{"A1", "A2", "A3"};
//			String[] s18 = new String[]{"B5", "B6", "C7"};
//			String[] s19 = new String[]{"G6", "G7", "G8"};
//			String[] s20 = new String[]{"C6", "D6", "E6"};
//			String[] s21 = new String[]{"H1", "H8"};
//
//			GamePlayer gp1 = new GamePlayer(p1, game1, date1);
//			String[] l = new String[]{"H2", "H3", "H4"};
//			Ship DestroyerX = new Ship ("Destroyer", l, gp1);
//			String[] l1 = new String[]{"E1", "F1", "G1"};
//			Ship Submarine = new Ship("Submarine", l1, gp1);
//			String[] l2 = new String[]{"B4", "B5"};
//			Ship PatrolBoat = new Ship("Patrol Boat", l2, gp1);
//            Salvo salvo_a_t1 = new Salvo(1, s1, gp1);
//			Salvo salvo_a_t2 = new Salvo(2, s3, gp1);
//
//
//
//			GamePlayer gp2 = new GamePlayer(p2, game1, date2);
//			String[] l3 = new String[]{"B5", "C5", "D5"};
//			Ship Destroyer = new Ship("Destroyer", l3, gp2);
//			String[] l4 = new String[]{"F1", "F2"};
//			Ship PatrolBoat2 = new Ship("Patrol Boat", l4, gp2);
//			Salvo salvo_b_t1 = new Salvo(1, s2, gp2);
//			Salvo salvo_b_t2 = new Salvo(2, s4, gp2);
//
//
//
//			GamePlayer gp3 = new GamePlayer( p1, game2, date3);
//			String[] l5 = new String[]{"B5", "C5", "D5"};
//			Ship Destroyer2 = new Ship("Destroyer", l5, gp3);
//			String[] l6 = new String[]{"C6", "C7"};
//			Ship PatrolBoat3 = new Ship("Patrol Boat", l6, gp3);
//			Salvo salvo_c_t1 = new Salvo(1, s5, gp3);
//			Salvo salvo_c_t2 = new Salvo(2, s7, gp3);
//
//
//			GamePlayer gp4 = new GamePlayer( p2, game2, date4);
//			String[] l7 = new String[]{"A2", "A3", "A4"};
//			Ship Submarine2 = new Ship("Submarine", l7, gp4);
//			String[] l8 = new String[]{"G6", "H6"};
//			Ship PatrolBoat4 = new Ship("Patrol Boat", l8, gp4);
//			Salvo salvo_d_t1 = new Salvo(1, s6, gp4);
//			Salvo salvo_d_t2 = new Salvo(2, s8, gp4);
//
//			GamePlayer gp5 = new GamePlayer( p2, game3, date5);
//			String[] l9 = new String[]{"B5", "C5", "D5"};
//			Ship Destroyer4 = new Ship("Destroyer", l9, gp5);
//			String[] l10 = new String[]{"C6", "C7"};
//			Ship PatrolBoat5 = new Ship("Patrol Boat", l10, gp5);
//			Salvo salvo_e_t1 = new Salvo(1, s9, gp5);
//			Salvo salvo_e_t2 = new Salvo(2, s11, gp5);
//
//			GamePlayer gp6 = new GamePlayer( p4, game3, date6);
//			String[] l11 = new String[]{"A2", "A3", "A4"};
//			Ship Submarine3 = new Ship("Submarine", l11, gp6);
//			String[] l12 = new String[]{"G6", "H6"};
//			Ship PatrolBoat6 = new Ship("Patrol Boat", l12, gp6);
//			Salvo salvo_f_t1 = new Salvo(1, s10, gp6);
//			Salvo salvo_f_t2 = new Salvo(2, s12, gp6);
//
//
//
//			GamePlayer gp7 = new GamePlayer( p2, game4, date1);
//			String[] l13 = new String[]{"B5", "C5", "D5"};
//			Ship Destroyer5 = new Ship("Destroyer", l13, gp7);
//			String[] l14 = new String[]{"C6", "C7"};
//			Ship PatrolBoat7 = new Ship("Patrol Boat", l14, gp7);
//			Salvo salvo_g_t1 = new Salvo(1, s13, gp7);
//			Salvo salvo_g_t2 = new Salvo(2, s15, gp7);
//
//
//
//			GamePlayer gp8 = new GamePlayer( p1, game4, date2);
//			String[] l15 = new String[]{"A2", "A3", "A4"};
//			Ship Submarine4 = new Ship("Submarine", l15, gp8);
//			String[] l16 = new String[]{"G6", "H6"};
//			Ship PatrolBoat8 = new Ship("Patrol Boat", l16, gp8);
//			Salvo salvo_h_t1 = new Salvo(1, s14, gp8);
//			Salvo salvo_h_t2 = new Salvo(2, s16, gp8);
//
//
//			GamePlayer gp9 = new GamePlayer( p4, game5, date2);
//			String[] l17 = new String[]{"B5", "C5", "D5"};
//			Ship Destroyer6 = new Ship("Destroyer", l17, gp9);
//			String[] l18 = new String[]{"C6", "C7"};
//			Ship PatrolBoat9 = new Ship("Patrol Boat", l18, gp9);
//			Salvo salvo_i_t1 = new Salvo(1, s17, gp9);
//			Salvo salvo_i_t2 = new Salvo(2, s19, gp9);
//
//
//			GamePlayer gp10 = new GamePlayer( p1, game5, date2);
//			String[] l19 = new String[]{"A2", "A3", "A4"};
//			Ship Submarine5 = new Ship("Submarine", l19, gp10);
//			String[] l20 = new String[]{"G6", "H6"};
//			Ship PatrolBoat10 = new Ship("Patrol Boat", l20, gp10);
//			Salvo salvo_j_t1 = new Salvo(1, s18, gp10);
//			Salvo salvo_j_t2 = new Salvo(2, s20, gp10);
//			Salvo salvo_j_t3 = new Salvo(3, s21, gp10);
//
//			GamePlayer gp11 = new GamePlayer( p3, game6, date2);
//			String[] l21 = new String[]{"B5", "C5", "D5"};
//			Ship Destroyer7 = new Ship("Destroyer", l21, gp11);
//			String[] l22 = new String[]{"C6", "C7"};
//			Ship PatrolBoat11 = new Ship("Patrol Boat", l22, gp11);
//
//
//			GamePlayer gp12 = new GamePlayer( p3, game8, date2);
//			String[] l40 = new String[]{"B5", "C5", "D5"};
//			Ship Destroyer8 = new Ship("Destroyer", l40, gp12);
//			String[] l41 = new String[]{"C6", "C7"};
//			Ship PatrolBoat12 = new Ship("Patrol Boat", l41, gp12);
//
//
//			GamePlayer gp13 = new GamePlayer( p4, game8, date2);
//			String[] l42 = new String[]{"A2", "A3", "A4"};
//			Ship Submarine6 = new Ship("Submarine", l42, gp13);
//			String[] l43 = new String[]{"G6", "H6"};
//			Ship PatrolBoat13 = new Ship("Patrol Boat", l43, gp13);
//
//
//			gamePlayerRepository.save(gp1);
//			gamePlayerRepository.save(gp2);
//			gamePlayerRepository.save(gp3);
//			gamePlayerRepository.save(gp4);
//			gamePlayerRepository.save(gp5);
//	 		gamePlayerRepository.save(gp6);
//			gamePlayerRepository.save(gp7);
//			gamePlayerRepository.save(gp8);
//			gamePlayerRepository.save(gp9);
//			gamePlayerRepository.save(gp10);
//			gamePlayerRepository.save(gp11);
//			gamePlayerRepository.save(gp12);
//			gamePlayerRepository.save(gp13);
//

//			shipRepository.save(Submarine);shipRepository.save(PatrolBoat);
//			shipRepository.save(Submarine2);shipRepository.save(PatrolBoat2);
//			shipRepository.save(Submarine3);shipRepository.save(PatrolBoat3);
//			shipRepository.save(Submarine4);shipRepository.save(PatrolBoat4);
//			shipRepository.save(Submarine5);shipRepository.save(PatrolBoat5);
//			shipRepository.save(Submarine6);shipRepository.save(PatrolBoat5);
//
//			shipRepository.save(PatrolBoat6);shipRepository.save(Destroyer);
//			shipRepository.save(PatrolBoat7);shipRepository.save(Destroyer2);
//			shipRepository.save(PatrolBoat8);shipRepository.save(Destroyer4);
//			shipRepository.save(PatrolBoat9);shipRepository.save(Destroyer5);
//			shipRepository.save(PatrolBoat10);shipRepository.save(Destroyer6);
//			shipRepository.save(PatrolBoat11);shipRepository.save(Destroyer7);
//			shipRepository.save(PatrolBoat12);shipRepository.save(Destroyer8);
//			shipRepository.save(PatrolBoat13);shipRepository.save(DestroyerX);
//
//			salvoRepository.save(salvo_a_t1);salvoRepository.save(salvo_a_t2);
//			salvoRepository.save(salvo_b_t1);salvoRepository.save(salvo_b_t2);
//			salvoRepository.save(salvo_c_t1);salvoRepository.save(salvo_c_t2);
//			salvoRepository.save(salvo_d_t1);salvoRepository.save(salvo_d_t2);
//			salvoRepository.save(salvo_e_t1);salvoRepository.save(salvo_e_t2);
//			salvoRepository.save(salvo_f_t1);salvoRepository.save(salvo_f_t2);
//			salvoRepository.save(salvo_g_t1);salvoRepository.save(salvo_g_t2);
//			salvoRepository.save(salvo_h_t1);salvoRepository.save(salvo_h_t2);
//			salvoRepository.save(salvo_i_t1);salvoRepository.save(salvo_i_t2);
//			salvoRepository.save(salvo_j_t1);salvoRepository.save(salvo_j_t2);salvoRepository.save(salvo_j_t3);
//
//
//			Score score = new Score(p1, game1, 1);Score score5 = new Score(p4, game5, null);
//			Score scoreb = new Score(p2, game1, 0);Score score5b = new Score(p1, game5, null);
//			Score score2 = new Score(p1, game2, 0.5);Score score6 = new Score(p3, game6, null);
//			Score score2b = new Score(p2, game2, 0.5);Score score7 = new Score(p4, game7, null);
//			Score score3 = new Score(p2, game3, 1);Score score8 = new Score(p3, game8, null);
//			Score score3b = new Score(p4, game3, 0); Score score8b = new Score(p4, game8, null);
//			Score score4 = new Score(p2, game4, 0.5);Score score4b = new Score(p1, game4, 0.5);
//
//
//			scoreRepository.save(score);scoreRepository.save(scoreb);
//			scoreRepository.save(score2);scoreRepository.save(score2b);
//			scoreRepository.save(score3);scoreRepository.save(score3b);
//			scoreRepository.save(score4);scoreRepository.save(score4b);
//			scoreRepository.save(score5);scoreRepository.save(score5b);
//			scoreRepository.save(score6);scoreRepository.save(score7);
//			;scoreRepository.save(score8);;scoreRepository.save(score8b);

		};
}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository playerRepository;
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByEmail(inputName);
			if (player != null) {
				return new User(player.getEmail(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.authorizeRequests()
				.antMatchers("/api/games").permitAll()
				.antMatchers("/api/signup/**").permitAll()
				.antMatchers("/api/login").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/login.html").permitAll()
				.antMatchers("/login.js").permitAll()
                .antMatchers("/game-view").hasAnyAuthority("USER")
				.anyRequest()
				.fullyAuthenticated();
		System.out.println("login requested");
		http.formLogin()
				.usernameParameter("email")
				.passwordParameter("pwd")
		.loginPage("/api/login");
		http.logout().logoutUrl("/api/logout");
		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

		http.headers().frameOptions().disable();
	}


	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		// The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD",
				"GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// will fail with 403 Invalid CORS request
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


}