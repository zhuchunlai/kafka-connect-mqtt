package be.jovacon.connect.mqtt.util;

import be.jovacon.connect.mqtt.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Instantiates given classes reflectively.
 *
 * @author zhuchunlai
 */
public class Instantiator {

    public static ClassLoader getClassLoader() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        if (classloader == null) {
            classloader = Configuration.class.getClassLoader();
        }

        return classloader;
    }

    /**
     * Instantiates the specified class either using the no-args constructor.
     *
     * @return The newly created instance or {@code null} if no class name was given
     */
    public static <T> T getInstance(String className) {
        return getInstanceWithProvidedConstructorType(className, null, null);
    }

    /**
     * Instantiates the specified class either using the no-args constructor or the
     * constructor with a single parameter of type {@link Configuration}, if a
     * configuration object is passed.
     *
     * @return The newly created instance or {@code null} if no class name was given
     */
    public static <T> T getInstance(String className, Configuration configuration) {
        return getInstanceWithProvidedConstructorType(className, Configuration.class, configuration);
    }

    /**
     * Instantiates the specified class either using the no-args constructor or the
     * constructor with a single parameter of type {@link Properties}, if a
     * properties object is passed.
     *
     * @return The newly created instance or {@code null} if no class name was given
     */
    public static <T> T getInstanceWithProperties(String className, Properties prop) {
        return getInstanceWithProvidedConstructorType(className, Properties.class, prop);
    }

    @SuppressWarnings("unchecked")
    public static <T, C> T getInstanceWithProvidedConstructorType(String className, Class<C> constructorType, C constructorValue) {
        if (className != null) {
            ClassLoader classloader = Instantiator.getClassLoader();
            try {
                Class<? extends T> clazz = (Class<? extends T>) classloader.loadClass(className);
                return constructorValue == null ? clazz.getDeclaredConstructor().newInstance()
                        : clazz.getConstructor(constructorType).newInstance(constructorValue);
            }
            catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Unable to find class " + className, e);
            }
            catch (InstantiationException e) {
                throw new IllegalArgumentException("Unable to instantiate class " + className, e);
            }
            catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Unable to access class " + className, e);
            }
            catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new IllegalArgumentException("Call to constructor of class " + className + " failed", e);
            }
        }

        return null;
    }
}
