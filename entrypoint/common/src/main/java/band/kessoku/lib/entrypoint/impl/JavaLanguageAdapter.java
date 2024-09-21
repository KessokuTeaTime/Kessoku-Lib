package band.kessoku.lib.entrypoint.impl;

import band.kessoku.lib.entrypoint.api.LanguageAdapter;
import band.kessoku.lib.entrypoint.impl.exceptions.LanguageAdapterException;
import band.kessoku.lib.platform.api.ModData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class JavaLanguageAdapter implements LanguageAdapter {
    public static final LanguageAdapter INSTANCE = new JavaLanguageAdapter();

    private JavaLanguageAdapter() {
    }

    @Override
    public Object parse(final ModData mod, final String value) throws LanguageAdapterException {
        final String[] methodSplit = value.split("::");
        if (methodSplit.length >= 3) {
            throw new LanguageAdapterException("Invalid handle format: " + value);
        }
        // Try to get the class
        final Class<?> c;
        try {
            c = Class.forName(methodSplit[0]);
        } catch (ClassNotFoundException e) {
            throw new LanguageAdapterException(e);
        }
        // The class may be an inner class
        if (!Modifier.isStatic(c.getModifiers()))
            throw new LanguageAdapterException("Class is not static: " + methodSplit[0]);
        // try to get the Object
        if (methodSplit.length == 1) {
            try {
                return c.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new LanguageAdapterException(e);
            }
        } else /* methodSplit.length == 2 */ {
            Getter getter = null;
            // Try to find method
            try {
                final Method method = c.getMethod(methodSplit[1], ModData.class);
                if (!Modifier.isStatic(method.getModifiers())) throw new LanguageAdapterException("Method " + value + " must be static!");
                getter = () -> {
                    method.setAccessible(true);
                    try {
                        return method.invoke(null, mod);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new LanguageAdapterException(e);
                    }
                };
            } catch (NoSuchMethodException e) {
                try {
                    final Method method = c.getMethod(methodSplit[1]);
                    if (!Modifier.isStatic(method.getModifiers())) throw new LanguageAdapterException("Method " + value + " must be static!");
                    getter = () -> {
                        method.setAccessible(true);
                        try {
                            return method.invoke(null);
                        } catch (InvocationTargetException | IllegalAccessException e2) {
                            throw new LanguageAdapterException(e2);
                        }
                    };
                } catch (NoSuchMethodException e3) {
                    // not method
                }
            }

            // Try to find field
            try {
                Field field = c.getDeclaredField(methodSplit[1]);

                if (!Modifier.isStatic(field.getModifiers())) {
                    throw new LanguageAdapterException("Field " + value + " must be static!");
                }

                if (getter != null) {
                    throw new LanguageAdapterException("Ambiguous " + value + " - refers to both field and method!");
                }
                return field.get(null);
            } catch (NoSuchFieldException e) {
                if (getter == null) throw new LanguageAdapterException("Could not find " + value + "!");
            } catch (IllegalAccessException e) {
                throw new LanguageAdapterException("Field " + value + " cannot be accessed!", e);
            }
            return getter.get();
        }
    }

    interface Getter {
        Object get() throws LanguageAdapterException;
    }
}