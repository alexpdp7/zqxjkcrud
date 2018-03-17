package net.pdp7.zqxjkcrud.security;

import java.util.Collections;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.google.common.collect.ImmutableMap;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	protected final NamedParameterJdbcTemplate jdbcTemplate;

	public UserDetailsService(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Map<String, Object> user = jdbcTemplate.queryForMap("select * from _users where username = :username",
					ImmutableMap.<String, Object>builder().put("username", username).build());
			return new User(username, "{noop}" + (String) user.get("password"), Collections.emptyList());
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username + " not found", e);
		}
	}

}
