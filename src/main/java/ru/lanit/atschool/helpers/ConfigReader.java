package ru.lanit.atschool.helpers;


import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final String DEBUG_PROPERTIES = "config.properties";

    public static String getStringSystemProperty(String name) throws IOException {
        String variable = System.getProperty(name);
        if (variable != null) {
            return variable.trim();
        }
        variable = loadProperty(name);
        if (variable== null) throw new IOException("Переменная:'" + name + "' не задана.");
        return variable;
    }
    /**
     * Получение значение  переменной из Property файла.
     * Для установки нужного Property файла используется значение переменной "application.properties".
     * По умолчанию используется переменные из файла default.properties.
     *
     * @param name - название переменной
     *
     * @return  значение переменной типа String.
     */
    private static String loadProperty(String name) throws IOException {
        return loadProperty(name, System.getProperty("application.properties", DEBUG_PROPERTIES));
    }

    /**
     * Получение значение  переменной из Property файла.
     *
     * @param name - название переменной
     * @param fromResource - имя ресурса откуда будут считываться переменные
     *
     * @return  значение переменной типа String.
     */

    private static String loadProperty(String name, String fromResource) throws IOException {
        Properties props = new Properties();
        try {
            if(!fromResource.startsWith("/")) fromResource="/"+fromResource;
            props.load(ConfigReader.class.getResourceAsStream(fromResource));
        } catch (IOException e) {
            throw new IOException("Ошибка получение значение'" + name + "' из конфиг файла'" + fromResource + "'.", e);
        }
        String value = props.getProperty(name);
        if (value == null) {
            return null;
        }
        if (value.trim().equalsIgnoreCase("${" + name + "}")) {
            return null;
        }
        return value;
    }
}
