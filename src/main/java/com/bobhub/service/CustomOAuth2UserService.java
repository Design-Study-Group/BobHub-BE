package com.bobhub.service;

import com.bobhub.domain.User;
import com.bobhub.mapper.UserMapper;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserMapper userMapper;

  public CustomOAuth2UserService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    System.out.println("CustomOAuth2UserService: loadUser 호출됨");

    OAuth2User oAuth2User = super.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    Map<String, Object> attributes = oAuth2User.getAttributes();

    System.out.println("RegistrationId: " + registrationId);
    System.out.println("User attributes: " + attributes);

    String email = (String) attributes.get("email");
    String name = (String) attributes.get("name");
    String picture = (String) attributes.get("picture");

    System.out.println("Email: " + email);
    System.out.println("Name: " + name);
    System.out.println("Picture: " + picture);

    User user = userMapper.findByEmail(email);
    if (user == null) {
      System.out.println("User not found. Creating new user.");
      user = new User();
      user.setEmail(email);
      user.setName(name);
      user.setPicture(picture);
      userMapper.insertUser(user);
      System.out.println("User created and saved to DB.");
    } else {
      System.out.println("User found in DB: " + user.getEmail());
    }

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), attributes, "email");
  }
}
