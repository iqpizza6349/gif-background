package org.iqpizza.gifbackground.util;

import com.intellij.openapi.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unchecked")
public class ReflectionUtil {

    public static boolean set(Class<?> clazz, Object target,
                              String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            if (isLocked(field)) {
                unlock(field);
            }
            field.set(target, value);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean set(String className, Object target, String fieldName,
                              Object value) {
        try {
            return set(Class.forName(className), target, fieldName, value);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isLocked(Field field) {
        return field.getModifiers() == 0;
    }

    private static void unlock(Field field) throws IllegalAccessException {
        Field target = unlock();
        target.setAccessible(true);
        field.setInt(target, target.getModifiers());
    }

    private static Field unlock() {
        try {
            return Field.class.getDeclaredField("modifiers");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T get(Class<T> returnType, Class<?> clazz, Object target, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T get(Class<T> returnType, String className, Object target, String fieldName) {
        try {
            return get(returnType, Class.forName(className), target, fieldName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invoke(Class<T> returnType, Class<?> clazz, Object target, String methodName,
                               List<Pair<Class<?>, Object>> params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, params.stream().map(p -> p.getFirst()).toArray(Class[]::new));
            method.setAccessible(true);
            return (T) method.invoke(target, params.stream().map(p -> p.getSecond())
                    .toArray(Object[]::new));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invoke(Class<T> returnType, String className, Object target, String methodName,
                               List<Pair<Class<?>, Object>> params) {
        try {
            return invoke(returnType, Class.forName(className), target, methodName, params);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void invokeVoid(Class<?> clazz, Object target, String methodName,
                                  List<Pair<Class<?>, Object>> params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, params.stream().map(p -> p.getFirst()).toArray(Class[]::new));
            method.setAccessible(true);
            method.invoke(target, params.stream().map(p -> p.getSecond()).toArray(Object[]::new));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void invokeVoid(String className, Object target, String methodName,
                                  List<Pair<Class<?>, Object>> params) {
        try {
            invokeVoid(Class.forName(className), target, methodName, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
