package top.vicat.halo.plugin.gameposter.platforms.steam.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SteamPersonalStateFlagEnum {

    NONE(0, "无"),
    VR_MODE(2048, "VR 模式")
    ;

    @JsonValue
    private final Integer code;
    private final String description;
}
