/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.lib;

import java.util.Random;
import java.util.function.Function;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
public final class RandomHelper {
    public static <T extends Enum<T>> T randomEnumConst(final Class<T> klass) {
        return randomEnumConst(klass, null);
    }

    public static <T extends Enum<T>> T randomEnumConst(final Class<T> klass,
                                                        final Function<T, Boolean> acceptFunction) {
        final T[] constants = klass.getEnumConstants();

        T randomValue;
        do {
            final int randomIndex = new Random().nextInt(constants.length);
            randomValue = constants[randomIndex];
        } while (acceptFunction != null && !acceptFunction.apply(randomValue));

        return randomValue;
    }
}
