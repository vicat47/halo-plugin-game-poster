package top.vicat.halo.plugin.gameposter.platforms.steam.scraper;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SteamScraper {

    private static final String AVATAR_MASK_XPATH = "//*[@id='responsive_page_template_content']/div[contains(@class,'profile_page')]/div[contains(@class,'profile_header_bg')]//div[contains(@class,'profile_avatar_frame')]/img";
    private static final String PROFILE_BG_XPATH = "//*[@id='responsive_page_template_content']/*[contains(@class,'has_profile_background')]";
    private static final String MINI_PROFILE_ID_XPATH = "//*[@id='responsive_page_template_content']/div[contains(@class,'profile_page')]/div[contains(@class,'profile_header_bg')]//*[@data-miniprofile]";
    private static final String MINI_PROFILE_BG_XPATH = "//div[@class='miniprofile_container']/div[@class='miniprofile_nameplatecontainer']/video[@class='miniprofile_nameplate']/source[@type='video/mp4']";
    private static final Pattern staticBackgroundPattern = Pattern.compile("background-image\\s*:\\s*url\\(\\s*'([^']+)'\\s*\\);\\s*");

    public UserBaseProfile.ProfileMedia scrapeAvatarMask(Document document) {
        var docOpt = Optional.ofNullable(document);
        return docOpt.map(doc -> doc.selectXpath(AVATAR_MASK_XPATH).first())
            .map(element -> element.attr("src"))
            .map(resource -> new UserBaseProfile.ProfileMedia(
                resource,
                UserBaseProfile.ProfileMediaType.IMAGE
            ))
            .orElse(null);
    }

    public UserBaseProfile.ProfileMedia scrapePersonalProfileBg(Document document) {
        var docOpt = Optional.ofNullable(document);
        var backgroundElement =
            docOpt.map(doc -> doc.selectXpath(PROFILE_BG_XPATH))
                .map(Elements::first);
        if (backgroundElement.isEmpty()) {
            return null;
        }
        return backgroundElement.map(element -> {
            var style = element.attr("style");
            Matcher matcher = staticBackgroundPattern.matcher(style);
            if (matcher.find()) {
                String group = matcher.group(1);
                return new UserBaseProfile.ProfileMedia(
                    group,
                    UserBaseProfile.ProfileMediaType.IMAGE
                );
            }
            return null;
        }).orElseGet(() ->
            backgroundElement.map(ele -> ele.selectXpath("*[@class='profile_animated_background']/video/source[@type='video/mp4']"))
                .map(Elements::first)
                .map(element -> element.attr("src"))
                .map(src -> new UserBaseProfile.ProfileMedia(
                    src,
                    UserBaseProfile.ProfileMediaType.VIDEO
                ))
                .orElse(null));
    }

    public String scrapeMiniProfileId(Document document) {
        var docOpt = Optional.ofNullable(document);
        return docOpt.map(doc -> doc.selectXpath(MINI_PROFILE_ID_XPATH).first())
            .map(element -> element.attr("data-miniprofile")).orElse(null);
    }

    public UserBaseProfile.ProfileMedia scrapeMiniProfileBgVideo(Document document) {
        var docOpt = Optional.ofNullable(document);
        return docOpt.map(doc -> doc.selectXpath(MINI_PROFILE_BG_XPATH).first())
            .map(ele -> ele.attr("src"))
            .map(src -> new UserBaseProfile.ProfileMedia(
                src,
                UserBaseProfile.ProfileMediaType.VIDEO
            ))
            .orElse(null);
    }

}
