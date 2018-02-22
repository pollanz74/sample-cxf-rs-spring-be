package org.pollanz.samples.api.gateway.core.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginVM {

    @NotNull
    private String username;

    @NotNull
    @Size(min = 5, max = 50)
    private String password;

}
