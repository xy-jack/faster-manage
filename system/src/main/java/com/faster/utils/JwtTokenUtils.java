
package com.faster.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * token验证工具
 *
 * @author YD
 */
@Slf4j
public class JwtTokenUtils {

	private static final String AUTHORITIES_KEY = "auth";

	/**
	 * secret key.
	 */
	private static SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	/**
	 * Token validity time(ms).
	 */
	public static long TOKEN_VALIDITY_IN_MILLISECONDS = 1000 * 60 * 30L;
	public static final String AUTHORIZATION_HEADER = "Authorization";

	/**
	 * Create token.
	 * @param subject auth info
	 * @return token
	 */
	public static String createToken(String subject) {
		long now = System.currentTimeMillis();
		Date validity = new Date(now + TOKEN_VALIDITY_IN_MILLISECONDS);
		return Jwts.builder().setSubject(subject).claim(AUTHORITIES_KEY, "").signWith(SECRET_KEY, SignatureAlgorithm.HS256)
				.setExpiration(validity).compact();
	}

	/**
	 * Get auth Info.
	 * @param token token
	 * @return auth info
	 */
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

		List<GrantedAuthority> authorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));

		User principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	/**
	 * validate token.
	 * @param token token
	 * @return whether valid
	 */
	public static boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		}
		catch (SignatureException e) {
			log.info("Invalid JWT signature.", e);
			log.trace("Invalid JWT signature trace: {}", e);
		}
		catch (MalformedJwtException e) {
			log.info("Invalid JWT token.");
			log.trace("Invalid JWT token trace: {}", e);
		}
		catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace: {}", e);
		}
		catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace: {}", e);
		}
		catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace: {}", e);
		}
		return false;
	}

}
