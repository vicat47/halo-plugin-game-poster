package top.vicat.halo.plugin.gameposter.vo;

public record ProxyConfig(
    String address,
    Integer port,
    String username,
    String password
) {}
