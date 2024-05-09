package top.vicat.halo.plugin.gameposter.vo;

public record ProxyConfig(
    Boolean enabled,
    String address,
    Integer port,
    String username,
    String password
) {}
