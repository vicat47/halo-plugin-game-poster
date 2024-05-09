package top.vicat.halo.plugin.gameposter.service;

import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;

public interface IGamePosterService {

    Mono<UserBaseProfile.UserBaseProfileSpec> getUserBaseProfiles(String accountId);

}
