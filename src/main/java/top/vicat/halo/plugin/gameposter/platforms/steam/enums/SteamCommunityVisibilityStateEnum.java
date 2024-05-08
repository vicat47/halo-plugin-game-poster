package top.vicat.halo.plugin.gameposter.platforms.steam.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SteamCommunityVisibilityStateEnum {
    INVISIBLE(1, "不可见"),
    PUBLIC(3, "公开"),
    ;
    @JsonEnumDefaultValue
    private final Integer code;
    private final String description;
}
