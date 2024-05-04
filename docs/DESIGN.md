---
title: 设计说明书
---

# 1. 功能设计

## 1.1 Steam 游戏及相关功能

![steam-简图](./assets/steam-%E7%AE%80%E5%9B%BE.png)

`steam` 具有接口齐全的特性，通过 `steam` 接口可以获取如下信息：

### 1. 用户基本信息及相关内容

`GET http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key=key&steamids=列表`

可以获取到如下相关数据（有截取）：
- 社区可见状态
- 社区页面 `url`
- 头像（大中小）
- 最近登录时间
- 创建时间
- 当前状态
- 正在进行的游戏（如果正在游戏）
- 地区

页面上需要有一个在线状态的展示区，且需要是可扩展其他平台的。主要用来展示头像。针对 `steam` 来说，如果个人页面是公开的，就可以通过访问个人页面（https://steamcommunity.com/id/{{STEAM_NAME}}/）获取一些相关信息，暂包括如下内容

![steam-详情](./assets/steam-%E8%AF%A6%E6%83%85.png)

- 头像的覆盖层
- 页面的背景
- mini_profile
- steam 等级
- 展示的徽章

所以该功能则设计为用户请求后在页面区域上展示用户相关信息，包括状态等，并附加上从个人页面获取到的叠加层。重度用户的 `steam` 页面较为美观，轻度用户也可以自定义页面背景。

请求过来的是 `html` 内容，所以需要一些解析 `DOM` 的工具。

### 2. 游戏相关内容

#### 获取已拥有游戏

`GET http://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key=key&steamid={{STEAM_ID}}&include_appinfo=true`

可以获取到如下信息（有截取）：

- 拥有的游戏数量
- 具体每个游戏的信息
  - 游戏 ID
  - 游戏名称
  - 游戏时间
  - 最近启动时间
  - 两周内游戏时间
  - 游戏缩略图 `url` （即菜单中的小方块图标）
    - `http://media.steampowered.com/steamcommunity/public/images/apps/{appid}/{img_icon_url}.jpg`

#### 获取游戏成就

- `GET http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v1/?key=key&appid=553850&steamid={{STEAM_ID}}&l=schinese`
- `GET http://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/?appid=553850&l=schinese`

可以获取到如下成就相关信息：

- 名称
- 描述
- 金色图标
- 灰色图标
- 解锁时间
- 全球成就进度

游戏页面应展示相关成就信息，并为全成就游戏进行特殊标记。

#### 获取其他游戏信息（需要加速器）

##### 截图

`GET https://steamcommunity.com/id/{user_id}/screenshots/?appid=0&sort=newestfirst&browsefilter=myfiles&view=grid`

可以展示游戏的截图，因为截图比较麻烦，可以缓存到本地来。

使用爬虫的方式分页获取截图。暂时实现第一页获取截图的功能，后续再进行完善

![steam-截图](./assets/steam-%E6%88%AA%E5%9B%BE.png)

##### 评测

`GET https://steamcommunity.com/id/{user_id}/recommended/`

针对 `steam` 部分游戏的评测。

![steam-评价](./assets/steam-%E8%AF%84%E4%BB%B7.png)

## 1.2 博主锐评

博主（不）需要对游戏进行锐评，可以简单打打分，发表下暴论一类的。可以和 `steam` 的评测一致，也可以为其他平台提供类似功能。

锐评中可以加入链接等。

# 2. 流程设计

获取某些信息可认为代价比较昂贵，需要对获取到的数据进行持久化存储，对当前数据而言拥有一个过期时间。若超出过期时间则需要重新进行请求。可以由用户强制刷新。

## 2.1 获取数据流程

![数据流程](./assets/数据流程.drawio.svg)

# 3. 页面设计

展示页概况

<iframe src='https://www.figma.com/proto/3byWuGTAzLc25kcqAV4M85/%E5%8D%9A%E5%AE%A2%E6%8F%92%E4%BB%B6?type=design&node-id=1-353&t=4ih4owBoIGYGlxWK-1&scaling=min-zoom&page-id=0%3A1&mode=design' style="height:1000px;"></iframe>

# 4. 配置项设计

## 基本配置

- 总开关
- 各个平台开关
- 游戏过滤器
- 新游展示策略
  - 先审后展
  - 直接展示
- 多平台同游戏合并展示
- 分页开关
- 评论开关
- 游戏置顶
- 预设隐私等级
- 博主评论

## steam 配置

- 实时状态
- 游戏时间
- 游戏截图
- 游戏评论
- 游戏成就

# 5. 实体设计



# 6. 接口设计