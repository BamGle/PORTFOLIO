package com.example.demo.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService{
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	
	public void create(UserCreateForm userCreateForm) {
		SiteUser user = new SiteUser();
		user.setUsername(userCreateForm.getUsername());
		user.setEmail(userCreateForm.getEmail());
		user.setPassword( passwordEncoder.encode(userCreateForm.getPassword1()) );
		userRepository.save(user);		
	}
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser =  userRepository.findByusername(username);
		if(siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("siteuser not found");
		}
	}

}
