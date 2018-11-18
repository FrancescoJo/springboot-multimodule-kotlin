/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.account;

import com.github.fj.lib.collection.ArrayUtilsKt;
import com.github.fj.lib.text.SemanticVersion;
import com.github.fj.lib.text.StringUtilsKt;
import com.github.fj.lib.util.ProtectedProperty;
import com.github.fj.restapi.endpoint.v1.account.dto.CreateAccountRequestDto;
import com.github.fj.restapi.endpoint.v1.account.dto.LoginRequestDto;
import com.github.fj.restapi.persistence.consts.account.*;
import com.github.fj.restapi.persistence.entity.Membership;
import com.github.fj.restapi.persistence.entity.User;
import org.springframework.data.util.Pair;
import test.com.github.fj.lib.RandomHelper;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
public final class AccountRequestUtils {
    private static final String DEFAULT_PLATFORM_VERSION = "Platform-1.0";
    private static final String DEFAULT_APP_VERSION = "0.0.1";
    private static final String DEFAULT_EMAIL = "tester@testcompany.com";

    public static CreateAccountRequestDto newRandomCreateAccountRequest() {
        return newRandomCreateAccountRequest(RandomHelper.randomEnumConst(LoginType.class));
    }

    public static CreateAccountRequestDto newRandomCreateAccountRequest(final LoginType loginType) {
        final Pair<String, ProtectedProperty<String>> identity = userIdentityByLoginType(loginType);
        final String username = identity.getFirst();
        final ProtectedProperty<String> credential = identity.getSecond();

        return new CreateAccountRequestDto(
                /*pushToken=*/       new ProtectedProperty<>(StringUtilsKt.getRandomAlphaNumericString(63)),
                /*username=*/        username,
                /*credential=*/      credential,
                /*nickname=*/        StringUtilsKt.getRandomAlphaNumericString(15),
                /*gender=*/          null,
                /*loginType=*/       loginType,
                /*platformType=*/    RandomHelper.randomEnumConst(PlatformType.class),
                /*platformVersion=*/ DEFAULT_PLATFORM_VERSION,
                /*appVersion=*/      DEFAULT_APP_VERSION,
                /*email*/            DEFAULT_EMAIL,
                /*invitedBy*/        StringUtilsKt.getRandomAlphaNumericString(15)
        );
    }

    public static User newRandomUser() throws Exception {
        final User u = new User();
        u.setId(new Random().nextInt(Short.MAX_VALUE));
        u.setIdToken(StringUtilsKt.getRandomAlphaNumericString(8));
        u.setStatus(Status.NORMAL);
        u.setRole(Role.USER);
        u.setName(StringUtilsKt.getRandomAlphaNumericString(12));
        u.setLoginType(RandomHelper.randomEnumConst(LoginType.class));
        u.setPlatformType(RandomHelper.randomEnumConst(PlatformType.class));
        u.setPlatformVersion(DEFAULT_PLATFORM_VERSION);
        u.setAppVersion(SemanticVersion.Companion.parse(DEFAULT_APP_VERSION));
        u.setEmail(DEFAULT_EMAIL);
        u.setCreatedDate(LocalDateTime.now());
        u.setCreatedIp(InetAddress.getLocalHost());
        u.setPushToken(StringUtilsKt.getRandomAlphaNumericString(63));
        u.setCredential(ArrayUtilsKt.getRandomBytes(56));

        final Membership m = new Membership();
        m.setId(u.getId());
        m.setNickname(StringUtilsKt.getRandomAlphaNumericString(16));
        m.setGender(RandomHelper.randomEnumConst(Gender.class));
        m.setLastActiveTimestamp(LocalDateTime.now());
        u.setMember(m);

        return u;
    }

    public static User copyOf(final User originalUser) {
        final User u = new User();
        u.setId(originalUser.getId());
        u.setIdToken(originalUser.getIdToken());
        u.setStatus(originalUser.getStatus());
        u.setRole(originalUser.getRole());
        u.setName(originalUser.getName());
        u.setLoginType(originalUser.getLoginType());
        u.setPlatformType(originalUser.getPlatformType());
        u.setPlatformVersion(originalUser.getPlatformVersion());
        u.setAppVersion(originalUser.getAppVersion());
        u.setEmail(originalUser.getEmail());
        u.setCreatedDate(originalUser.getCreatedDate());
        u.setCreatedIp(originalUser.getCreatedIp());
        u.setPushToken(originalUser.getPushToken());
        u.setCredential(originalUser.getCredential());

        final Membership m = new Membership();
        m.setId(u.getMember().getId());
        m.setNickname(u.getMember().getNickname());
        m.setGender(u.getMember().getGender());
        m.setLastActiveTimestamp(u.getMember().getLastActiveTimestamp());
        u.setMember(m);

        return u;
    }

    public static LoginRequestDto newRandomLoginRequest(final LoginType loginType) {
        final Pair<String, ProtectedProperty<String>> identity = userIdentityByLoginType(loginType);
        final String username = identity.getFirst();
        final ProtectedProperty<String> credential = identity.getSecond();

        return new LoginRequestDto(
                /*username=*/        username,
                /*credential=*/      credential,
                /*loginType=*/       loginType,
                /*platformType=*/    RandomHelper.randomEnumConst(PlatformType.class),
                /*platformVersion=*/ DEFAULT_PLATFORM_VERSION,
                /*appVersion=*/      DEFAULT_APP_VERSION
        );
    }

    private static Pair<String, ProtectedProperty<String>> userIdentityByLoginType(final LoginType loginType) {
        final String username;
        final ProtectedProperty<String> credential;
        if (loginType == LoginType.GUEST) {
            username = "";
            credential = new ProtectedProperty<>("");
        } else {
            username = StringUtilsKt.getRandomAlphaNumericString(User.MAXIMUM_NAME_LENGTH);
            credential = new ProtectedProperty<>(StringUtilsKt.getRandomAlphaNumericString(63));
        }

        return Pair.of(username, credential);
    }
}
