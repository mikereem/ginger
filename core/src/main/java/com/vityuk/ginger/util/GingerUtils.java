/*
 * Copyright 2013 Andriy Vityuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vityuk.ginger.util;

import com.vityuk.ginger.Localizable;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class GingerUtils {

    /**
     * Change: simply use the method name without any transformation.
     */
    public static String createKeyFromMethod(Method method) {
        Localizable.Key annotation = method.getAnnotation(Localizable.Key.class);
        if (annotation != null) {
            return annotation.value();
        }

        // Fallback previous behaviour
        String methodName = method.getName();

        return methodName;
    }

    public static boolean isConstantMethod(Method method) {
        return method.getParameterTypes().length == 0;
    }

    public static int indexOfParameterAnnotation(Method method, Class<? extends Annotation> annotationType) {
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0, n = parametersAnnotations.length; i < n; i++) {
            Annotation[] parameterAnnotations = parametersAnnotations[i];
            for (Annotation parameterAnnotation : parameterAnnotations) {
                if (parameterAnnotation.annotationType().equals(annotationType)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean isIntNumericType(Class<?> type) {
        return type == byte.class || type == Byte.class ||
                type == short.class || type == Short.class ||
                type == int.class || type == Integer.class ||
                type == long.class || type == Long.class;
    }
}
