package bakery.caker.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Authority {
    CLIENT("ROLE_CLIENT"),
    TRAINEE("ROLE_TRAINEE"),
    BAKER("ROLE_BAKER");

    private String value;
}
