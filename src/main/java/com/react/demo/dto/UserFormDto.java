package com.react.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserFormDto {

    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

}
