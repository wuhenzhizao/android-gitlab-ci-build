package com.wuhenzhizao.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;

public class ObjectUtils {

    private ObjectUtils() {
        throw new AssertionError();
    }

    /**
     * compare two object
     *
     * @param actual
     * @param expected
     * @return <ul>
     * <li>if both are null, return true</li>
     * <li>return actual.{@link Object#equals(Object)}</li>
     * </ul>
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     * null Object to empty string
     * <p/>
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * convert long array to Long array
     *
     * @param source
     * @return
     */
    public static Long[] transformLongArray(long[] source) {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Long array to long array
     *
     * @param source
     * @return
     */
    public static long[] transformLongArray(Long[] source) {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert int array to Integer array
     *
     * @param source
     * @return
     */
    public static Integer[] transformIntArray(int[] source) {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Integer array to int array
     *
     * @param source
     * @return
     */
    public static int[] transformIntArray(Integer[] source) {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * compare two object
     * <ul>
     * <strong>About result</strong>
     * <li>if v1 > v2, return 1</li>
     * <li>if v1 = v2, return 0</li>
     * <li>if v1 < v2, return -1</li>
     * </ul>
     * <ul>
     * <strong>About rule</strong>
     * <li>if v1 is null, v2 is null, then return 0</li>
     * <li>if v1 is null, v2 is not null, then return -1</li>
     * <li>if v1 is not null, v2 is null, then return 1</li>
     * <li>return v1.{@link Comparable#compareTo(Object)}</li>
     * </ul>
     *
     * @param v1
     * @param v2
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable) v1).compareTo(v2));
    }

    /**
     * Get the Field object from an object's class or its all super classes.
     *
     * @param obj  the object to search
     * @param name field name
     * @return Field object
     * @throws NoSuchFieldException no field found with given name
     */
    public static Field getField(Object obj, String name) throws NoSuchFieldException {
        if (obj == null || TextUtils.isEmpty(name)) {
            return null;
        }

        Field field = null;
        NoSuchFieldException exception = null;

        Class<?> type = obj.getClass();
        while (type != null && !type.equals(Object.class)) {
            // loop up to Object, null means a primitive type
            try {
                field = type.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                // hold the first exception
                if (exception == null) {
                    exception = e;
                }
            }

            if (field != null) {
                // found the field, exit the loop
                break;
            } else {
                // if no such field in current class, then search super class
                type = type.getSuperclass();
            }
        }

        if (field == null && exception != null) {
            // didn't find any field
            throw exception;
        }

        return field;
    }

    /**
     * Get the value of a field
     *
     * @param obj  the object
     * @param name the field name
     * @return the value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object obj, String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(obj, name);
        if (field == null) {
            return null;
        }

        field.setAccessible(true);
        return field.get(obj);
    }
}
