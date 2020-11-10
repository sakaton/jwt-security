package org.sakaton.security.jwt.util;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * iss (issuer)：签发人
 * exp (expiration time)：过期时间
 * sub (subject)：主题
 * aud (audience)：受众
 * nbf (Not Before)：生效时间
 * iat (Issued At)：签发时间
 * jti (JWT ID)：编号
 * <p>
 * HMACSHA256(
 * base64UrlEncode(header) + "." +
 * base64UrlEncode(payload),
 * secret)
 *
 * @author sakaton
 * @version created on 2020/11/10.
 */
public final class TokenHelper {

	private TokenHelper() {
		throw new IllegalStateException();
	}


	public static String createToken(String subject, String audience, String secret, Long timeout) {
		return createToken(subject, audience, secret, timeout, Maps.newHashMap());
	}

	public static String createToken(String subject, String audience, String secret, Long timeout, Map<String, Object> claims) {

		long currentTime = System.currentTimeMillis();
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setClaims(claims)
				// 编号
				.setId("" + System.nanoTime())
				// 签发人
				.setIssuer(StringUtils.EMPTY)
				// 主题
				.setSubject(subject)
				// 受众
				.setAudience(audience)
				// 生效时间
				.setNotBefore(new Date(currentTime + 50))
				// 签发时间
				.setIssuedAt(new Date(currentTime))
				// 过期时间
				.setExpiration(new Date(currentTime + timeout))

				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}


	public static Claims parseToken(String token, String secret) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

}
