package org.sakaton.security.jwt.configure.security.handler;

import org.sakaton.security.jwt.util.TokenHelper;

import java.util.concurrent.TimeUnit;

/**
 * TOKEN 管理器
 *
 * @author zhengshijun
 * @version created on 2020/11/10.
 */
public interface TokenManageHandler {

	/**
	 * 生成token
	 *
	 * @return 返回TOKEN
	 */
	String createToken();

	/**
	 * 验证 token 是否有效
	 *
	 * @param token token
	 * @return 返回结果
	 */
	boolean verifyToken(String token);


	/**
	 * 删除 token
	 *
	 * @param token token
	 * @return 返回结果
	 */
	boolean removeToken(String token);


	class Default implements TokenManageHandler {

		final static String SECRET = "123456";

		@Override
		public String createToken() {
			return TokenHelper.createToken("", "", SECRET, TimeUnit.MINUTES.toMillis(30));
		}

		@Override
		public boolean verifyToken(String token) {
			TokenHelper.parseToken(token, SECRET);
			return Boolean.TRUE;
		}

		@Override
		public boolean removeToken(String token) {
			return Boolean.TRUE;
		}
	}

}
