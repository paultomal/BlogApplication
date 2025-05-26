package com.example.nafiz.blog.common.enums;

public enum UserRoles {
    ROLE_ADMIN("Admin"),
    ROLE_USER("User");
    private final String label;

    UserRoles(String label) {
        this.label = label;
    }

    public static UserRoles getUserRolesByLabel(String label) {
        for (UserRoles roles : UserRoles.values()) {
            if (roles.label.equals(label)) return roles;
        }
        return null;
    }

    public static String getLabelByUserRoles(UserRoles userRoles) {
        return userRoles.label;
    }
}

