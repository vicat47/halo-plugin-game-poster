package top.vicat.halo.plugin.gameposter.platforms.steam.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SteamPersonalStateEnum {
    OFFLINE(0, "离线"),
    ONLINE(1, "在线"),
    BUSY(2, "忙碌"),
    LEAVE(3, "离开"),
    NAPPING(4, "打盹"),
    WANT_TO_TRADE(5, "想要交易"),
    WANT_TO_PLAY(6, "想要玩"),
    ;
    @JsonEnumDefaultValue
    private final Integer code;
    private final String description;
}
