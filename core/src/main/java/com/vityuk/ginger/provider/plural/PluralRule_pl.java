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

package com.vityuk.ginger.provider.plural;

/**
 * Plural rule for Polish language.
 *
 * @author Andriy Vityuk
 */
public final class PluralRule_pl extends AbstractPluralRule {
    private static final String[] QUALIFIERS = new String[]{"one", "few", "many"};

    @Override
    protected String[] qualifiers() {
        return QUALIFIERS;
    }

    @Override
    protected int selectQualifier(int count) {
        return count == 1 ? 0
                : count % 10 >= 2 && count % 10 <= 4 && (count % 100 < 10 || count % 100 > 20) ? 1
                : 2;
    }
}
