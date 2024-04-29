---
title: 参考资料
---

# Steam API
[steam web api](https://developer.valvesoftware.com/wiki/Steam_Web_API)
[detailed info](https://steamcommunity.com/discussions/forum/1/1643167006257402427/)
## 1. 获取个人信息
```json5
// GET http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?steamids=1234567890123456

{
    "response": {
        "players": [
            {
                "steamid": "1234567890123456",
                "communityvisibilitystate": 3,
                "profilestate": 1,
                // 网名
                "personaname": "xxxxxx",
                // 个人页面
                "profileurl": "https://steamcommunity.com/id/xxxxxx/",
                // 头像
                "avatar": "https://avatars.steamstatic.com/xxxxxxxxxxxxxxxxxxxxxxx.jpg",
                "avatarmedium": "https://avatars.steamstatic.com/xxxxxxxxxxxxxxxxxxxxxxx_medium.jpg",
                "avatarfull": "https://avatars.steamstatic.com/xxxxxxxxxxxxxxxxxxxxxxx_full.jpg",
                "avatarhash": "xxxxxxxxxxxxxxxxxxxxxxx",
                // 最近登录
                "lastlogoff": 1714213740,
                // 状态(0 - 离线，1 - 在线，2 - 忙碌，3 - 离开，4 - 打盹，5 - 想要交易，6 - 想要玩)
                "personastate": 1,
                // 真名
                "realname": "xxx",
                "primaryclanid": "xxxxxxx",
                // 创建时间
                "timecreated": 1414847017,
                "personastateflags": 0,
                // 只有游戏中才有（游戏名称）
                "gameextrainfo": "HELLDIVERS™ 2",
                // 游戏 id
                "gameid": "553850",
                // 游戏大厅 ID
                "lobbysteamid": "xxxxxxxxxxxxxxx",
                "loccountrycode": "CN",
                "locstatecode": "24"
            }
        ]
    }
}
```