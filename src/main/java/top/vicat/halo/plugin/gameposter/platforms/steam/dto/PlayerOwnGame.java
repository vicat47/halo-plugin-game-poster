package top.vicat.halo.plugin.gameposter.platforms.steam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import top.vicat.halo.plugin.gameposter.platforms.steam.serializer.DurationDeserializer;
import top.vicat.halo.plugin.gameposter.platforms.steam.serializer.LocalDateTimeTimestampDeserializer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public record PlayerOwnGame(
    @JsonProperty("appid")
    Long id,
    String name,
    @JsonProperty("img_icon_url")
    String imgIconUrl,
    @JsonProperty("playtime_windows_forever")
    @JsonDeserialize(using = DurationDeserializer.class)
    Duration playtimeWindowsForever,
    @JsonProperty("playtime_mac_forever")
    @JsonDeserialize(using = DurationDeserializer.class)
    Duration playtimeMacForever,
    @JsonProperty("playtime_linux_forever")
    @JsonDeserialize(using = DurationDeserializer.class)
    Duration playtimeLinuxForever,
    @JsonProperty("playtime_deck_forever")
    @JsonDeserialize(using = DurationDeserializer.class)
    Duration playtimeDeckForever,
    @JsonProperty("playtime_forever")
    @JsonDeserialize(using = DurationDeserializer.class)
    Duration playtimeForever,
    @JsonProperty("rtime_last_played")
    @JsonDeserialize(using = DurationDeserializer.class)
    LocalDateTime rTimeLastPlayed,
    @JsonProperty("content_descriptorids")
    List<Long> contentDescriptorIds,
    @JsonProperty("playtime_disconnected")
    @JsonDeserialize(using = DurationDeserializer.class)
    Duration playtimeDisconnected,
    @JsonProperty("has_community_visible_stats")
    Boolean hasCommunityVisibleStats,
    @JsonProperty("has_leaderboards")
    Boolean hasLeaderboards,
    @JsonProperty("playtime_2weeks")
    @JsonDeserialize(using = LocalDateTimeTimestampDeserializer.class)
    LocalDateTime playtime2weeks
) {}
