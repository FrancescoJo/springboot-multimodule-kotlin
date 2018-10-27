/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.helper.account;

import com.github.fj.lib.text.SemanticVersion;
import com.github.fj.lib.text.StringUtilsKt;
import com.github.fj.restapi.dto.account.CreateAccountRequestDto;
import com.github.fj.restapi.persistence.consts.account.LoginType;
import com.github.fj.restapi.persistence.consts.account.PlatformType;
import test.com.github.fj.lib.RandomHelper;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
public final class AccountControllerRequestHelper {
    public static CreateAccountRequestDto newRandomCreateAccountRequest() {
        return new CreateAccountRequestDto(
                /*pushToken=*/       StringUtilsKt.getRandomAlphaNumericString(63),
                /*username=*/null,
                /*credential=*/      StringUtilsKt.getRandomAlphaNumericString(63),
                /*nickname=*/        StringUtilsKt.getRandomAlphaNumericString(16),  // For Test
                /*gender=*/null,
                /*loginType=*/       RandomHelper.randomEnumConst(LoginType.class, loginType ->
                loginType != LoginType.UNDEFINED),
                /*platformType=*/    RandomHelper.randomEnumConst(PlatformType.class, platformType ->
                platformType != PlatformType.UNDEFINED),
                /*platformVersion=*/ "Platform-1.0",
                /*appVersion=*/      "0.0.1",
                /*email*/            "tester@testcompany.com"
        );
    }
}
