package org.whut.mc.server.core.config;

import org.whut.mc.server.core.log.Log;

import java.io.*;
import java.util.Properties;

/**
 * Created by yangyang on 2015/10/21.
 */
public class PropConfig implements Config {
    private static Log log = Log.getLogger(PropConfig.class);
    private static PropConfig propConfig;
    private Properties properties;
    private static final String PROP_PATH = System.getProperty("user.dir") + "/src/prop.properties";

    private PropConfig() {
        if (properties == null) {
            synchronized (PropConfig.class) {
                if (properties == null) {
                    try {
                        InputStream is = new FileInputStream(new File(PROP_PATH));
                        properties = new Properties();
                        properties.load(is);
                    } catch (FileNotFoundException e) {
                        log.error(e.getMessage());
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
    }

    public static PropConfig getPropConfig() {
        if (propConfig == null) {
            return new PropConfig();
        }
        return propConfig;
    }

    public Object get(String key) {
        return properties.get(key);
    }

    public static void main(String[] args) {
        log.info((String) PropConfig.getPropConfig().get("test"));
    }
}
