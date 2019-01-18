package by.hurynovich.mus_overview.enumeration;

import java.util.Arrays;

public enum UserRole {

    USER(),
    ADMIN();

    public static String[] getAllRolesStStringArray() {
        return Arrays.stream(UserRole.values()).map(String::valueOf).toArray(String[]::new);
    }

}