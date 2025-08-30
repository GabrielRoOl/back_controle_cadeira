package br.com.cadeira.controle.vitrium.authentication.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("admin"),
    USER("user");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

}
