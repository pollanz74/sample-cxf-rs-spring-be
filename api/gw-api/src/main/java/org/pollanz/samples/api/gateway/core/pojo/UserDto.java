package org.pollanz.samples.api.gateway.core.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private String login;

    private String password;

    private List<String> authorities;

}
