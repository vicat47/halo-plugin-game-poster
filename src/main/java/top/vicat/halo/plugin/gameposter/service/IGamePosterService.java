package top.vicat.halo.plugin.gameposter.service;

import reactor.core.publisher.Flux;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;

public interface IGamePosterService {

    Flux<UserBaseProfile.UserBaseProfileSpec> getUserBaseProfiles(String accountId);

}
