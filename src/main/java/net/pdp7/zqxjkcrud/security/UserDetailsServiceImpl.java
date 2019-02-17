package net.pdp7.zqxjkcrud.security;

import static org.jooq.impl.DSL.field;

import java.util.Collections;

import org.jooq.DSLContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class UserDetailsServiceImpl implements UserDetailsService {

	protected final DSLContext dslContext;

	public UserDetailsServiceImpl(DSLContext dslContext) {
		this.dslContext = dslContext;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		String password = dslContext.select()
				.from("_users")
				.where(field("username").eq(username))
				.fetchSingle("password", String.class);
		return new User(username, password, Collections.emptyList());
	}

}
