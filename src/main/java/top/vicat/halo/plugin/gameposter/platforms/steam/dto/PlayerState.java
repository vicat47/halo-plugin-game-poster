package top.vicat.halo.plugin.gameposter.platforms.steam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import top.vicat.halo.plugin.gameposter.platforms.steam.enums.SteamCommunityVisibilityStateEnum;
import top.vicat.halo.plugin.gameposter.platforms.steam.enums.SteamPersonalStateEnum;
import top.vicat.halo.plugin.gameposter.platforms.steam.serializer.LocalDateTimeTimestampDeserializer;
import java.time.LocalDateTime;

public record PlayerState(
    @JsonProperty("steamid")
    String steamId,
    @JsonProperty("communityvisibilitystate")
    SteamCommunityVisibilityStateEnum communityVisibilityState,
    @JsonProperty("profilestate")
    Long profileState,
    @JsonProperty("personaname")
    String personaName,
    @JsonProperty("profileurl")
    String profileUrl,
    @JsonProperty("avatar")
    String avatar,
    @JsonProperty("avatarmedium")
    String avatarMedium,
    @JsonProperty("avatarfull")
    String avatarFull,
    @JsonProperty("avatarhash")
    String avatarHash,
    @JsonProperty("lastlogoff")
    @JsonDeserialize(using = LocalDateTimeTimestampDeserializer.class)
    LocalDateTime lastLogoff,
    @JsonProperty("personastate")
    SteamPersonalStateEnum personaState,
    @JsonProperty("realname")
    String realName,
    @JsonProperty("primaryclanid")
    String primaryClanId,
    @JsonProperty("timecreated")
    @JsonDeserialize(using = LocalDateTimeTimestampDeserializer.class)
    LocalDateTime timeCreated,
    @JsonProperty("personastateflags")
    Long personaStateFlags,
    @JsonProperty("gameextrainfo")
    String gameExtraInfo,
    @JsonProperty("gameid")
    String gameId,
    @JsonProperty("lobbysteamid")
    String lobbySteamId,
    @JsonProperty("loccountrycode")
    String locCountryCode,
    @JsonProperty("locstatecode")
    String locStateCode
) {}
