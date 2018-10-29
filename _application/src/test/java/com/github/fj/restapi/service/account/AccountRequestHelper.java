/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account;

import com.github.fj.lib.collection.ArrayUtilsKt;
import com.github.fj.lib.text.SemanticVersion;
import com.github.fj.lib.text.StringUtilsKt;
import com.github.fj.restapi.dto.account.CreateAccountRequestDto;
import com.github.fj.restapi.persistence.consts.account.LoginType;
import com.github.fj.restapi.persistence.consts.account.PlatformType;
import com.github.fj.restapi.persistence.consts.account.Status;
import com.github.fj.restapi.persistence.entity.User;
import test.com.github.fj.lib.RandomHelper;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
public final class AccountRequestHelper {
    private static final String DEFAULT_PLATFORM_VERSION = "Platform-1.0";
    private static final String DEFAULT_APP_VERSION = "0.0.1";
    private static final String DEFAULT_EMAIL = "tester@testcompany.com";

    public static CreateAccountRequestDto newRandomCreateAccountRequest() {
        return newRandomCreateAccountRequest(RandomHelper.randomEnumConst(LoginType.class));
    }

    public static CreateAccountRequestDto newRandomCreateAccountRequest(final LoginType loginType) {
        return new CreateAccountRequestDto(
                /*pushToken=*/       StringUtilsKt.getRandomAlphaNumericString(63),
                /*username=*/        null,
                /*credential=*/      StringUtilsKt.getRandomAlphaNumericString(63),
                /*nickname=*/        StringUtilsKt.getRandomAlphaNumericString(16),  // For Test
                /*gender=*/          null,
                /*loginType=*/       loginType,
                /*platformType=*/    RandomHelper.randomEnumConst(PlatformType.class),
                /*platformVersion=*/ DEFAULT_PLATFORM_VERSION,
                /*appVersion=*/      DEFAULT_APP_VERSION,
                /*email*/            DEFAULT_EMAIL
        );
    }

    public static User newRandomUser() throws Exception {
        final User u = new User();
        u.setId(new Random().nextInt(Short.MAX_VALUE));
        u.setIdToken(StringUtilsKt.getRandomAlphaNumericString(8));
        u.setStatus(Status.NORMAL);
        u.setRoles("");
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

        return u;
    }
}
