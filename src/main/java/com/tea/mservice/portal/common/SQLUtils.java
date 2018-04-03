package com.tea.mservice.portal.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SQLUtils {
    public static <T> String foreach(String open, String close, String separator, Iterable<T> collection) {
        Iterator<T> it = collection.iterator();
        if (! it.hasNext())
            return open + close;

        StringBuilder sb = new StringBuilder();
        sb.append(open);
        for (;;) {
            T e = it.next();
            sb.append(e);
            if (! it.hasNext())
                return sb.append(close).toString();
            sb.append(separator).append(' ');
        }
    }

    public static String foreachIn(int size) {
        return foreach("(",")\n",",", Collections.nCopies(size, '?'));
    }
}
