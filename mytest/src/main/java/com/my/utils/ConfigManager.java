package com.my.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenqiuzhen on 2017/12/20.
 */
public class ConfigManager {
    protected static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    public static String ERROR_CODE_CONFIG_START = "E01-001-01";
    public static String ERROR_MSG_CONFIG_START = "配置信息加载失败";
    public static String ERROR_CODE_CONFIG_UPDATE = "E01-001-02";
    public static String ERROR_MSG_CONFIG_UPDATE = "配置信息更新失败";
    public static String ERROR_CODE_CONFIG_BACKUP = "E01-001-03";
    public static String ERROR_MSG_CONFIG_BACKUP = "配置信息更新时备份失败";
    private static Map<String, ConfigManager> managers = new HashMap();
    private static Object lock = new Object();
    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");
    private List<ConfigChangeEventListener> listeners;
    private Properties properties;
    private String configName;
    private boolean monitorFile;
    private Date fileLastUpdateDate;


    private synchronized void loadProperties(String name) {
        InputStream is = this.getClass().getResourceAsStream("/" + name + ".properties");
        this.properties = new Properties();
        try {
            this.properties.load(new InputStreamReader(is, "UTF-8"));
        } catch (IOException var4) {
            log.error("", var4);
        }

    }

    private ConfigManager(String name) {
        this(name, false);
    }

    private ConfigManager(String name, boolean monitor) {
        this.listeners = new ArrayList();
        this.monitorFile = true;
        this.monitorFile = monitor;
        this.configName = name;
        final String path = this.getClass().getResource("/" + name + ".properties").getFile();
        this.loadProperties(this.configName);
        if(monitor) {
            ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
            pool.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if(ConfigManager.this.monitorFile) {
                        try {
                            ConfigManager.log.debug("check file status at " + new Date());
                            File ex = new File(path);
                            synchronized(ConfigManager.lock) {
                                Calendar currentUpdatCalendar;
                                if(ConfigManager.this.fileLastUpdateDate == null) {
                                    currentUpdatCalendar = Calendar.getInstance();
                                    currentUpdatCalendar.setTimeInMillis(ex.lastModified());
                                    ConfigManager.this.fileLastUpdateDate = currentUpdatCalendar.getTime();
                                    ConfigManager.log.info("init fileLastUpdateDate for " + ConfigManager.this.configName + " fileLastUpdateDate=" + currentUpdatCalendar.getTime());
                                } else {
                                    currentUpdatCalendar = Calendar.getInstance();
                                    currentUpdatCalendar.setTimeInMillis(ex.lastModified());
                                    if(ConfigManager.this.fileLastUpdateDate.getTime() != currentUpdatCalendar.getTimeInMillis()) {
                                        ConfigManager.log.info("config file change for " + ConfigManager.this.configName + " fileLastUpdateDate=" + ConfigManager.this.fileLastUpdateDate + " currentUpdateDate=" + currentUpdatCalendar.getTime());
                                        HashMap origConfig = new HashMap();
                                        origConfig.putAll(ConfigManager.this.properties);
                                        ConfigManager.this.loadProperties(ConfigManager.this.configName);
                                        Iterator var6 = ConfigManager.this.listeners.iterator();

                                        while(var6.hasNext()) {
                                            ConfigManager.ConfigChangeEventListener listener = (ConfigManager.ConfigChangeEventListener)var6.next();
                                            listener.onChange(origConfig, ConfigManager.this.properties, (Map)null, ConfigManager.this.getModuleChangedNames((Map)null));
                                        }

                                        ConfigManager.this.fileLastUpdateDate = currentUpdatCalendar.getTime();
                                    }
                                }
                            }
                        } catch (Exception var8) {
                            ConfigManager.log.error("", var8);
                        }
                    }

                }
            }, 0L, 5L, TimeUnit.SECONDS);
        }

        log.trace("key set: {}", this.properties.keySet());
    }

    public static ConfigManager getInstance() {
        return getInstance("system");
    }

    public static ConfigManager getInstance(String name) {
        if(managers.containsKey(name)) {
            return (ConfigManager)managers.get(name);
        } else {
            Object var2 = lock;
            synchronized(lock) {
                ConfigManager manager = new ConfigManager(name);
                managers.put(name, manager);
                return manager;
            }
        }
    }

    public static ConfigManager getInstance(String name, boolean monitorFile) {
        if(managers.containsKey(name)) {
            return (ConfigManager)managers.get(name);
        } else {
            Object var3 = lock;
            synchronized(lock) {
                ConfigManager manager = new ConfigManager(name, monitorFile);
                managers.put(name, manager);
                return manager;
            }
        }
    }


    public Map<Object, Object> getAllConfig() {
        return this.properties;
    }

    public Object getConfig(Object key) {
        return this.properties.get(key);
    }

    public String getString(Object key) {
        return this.getString(key, "");
    }

    public String getString(Object key, String defaultValue) {
        return this.getString(key, defaultValue, false);
    }

    public String getString(Object key, boolean containVar) {
        return this.getString(key, "", containVar);
    }

    public String getString(Object key, String defaultValue, boolean containVar) {
        if (containVar) {
            return this.properties.get(key) == null ? defaultValue : this.loop(this.properties.get(key).toString());
        } else {
            return this.properties.get(key) == null ? defaultValue : this.properties.get(key).toString();
        }
    }

    public int getInt(Object key) {
        return this.getInt(key, 0);
    }

    public int getInt(Object key, int defaultValue) {
        return this.properties.get(key) == null?defaultValue:Integer.parseInt(this.properties.get(key).toString());
    }

    public boolean getBoolean(Object key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(Object key, boolean defaultValue) {
        return this.properties.get(key) == null?defaultValue:Boolean.parseBoolean(this.properties.get(key).toString());
    }



    private String getModuleChangedNames(Map<Object, Object> config) {
        if(config == null) {
            return null;
        } else {
            String moduleNames = "";
            Iterator var4 = config.keySet().iterator();

            while(var4.hasNext()) {
                Object key = var4.next();
                int index = key.toString().indexOf(".");
                if(index > -1) {
                    index = key.toString().indexOf(".", index + 1);
                    if(index > -1) {
                        moduleNames = moduleNames + key.toString().substring(0, index) + ";";
                    }
                }
            }

            return moduleNames;
        }
    }

    public Map<String, Object> filtConfig(String filterName) {
        Hashtable result = new Hashtable();
        Iterator var4 = this.properties.entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry entity = (Map.Entry)var4.next();
            if(entity.getKey().toString().startsWith(filterName)) {
                result.put(entity.getKey().toString(), entity.getValue());
            }
        }

        return result;
    }

    public static Map<Object, Object> filtConfig(Map<Object, Object> config, String moduleName) {
        Hashtable result = new Hashtable();
        Iterator var4 = config.entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry entity = (Map.Entry)var4.next();
            if(entity.getKey().toString().startsWith(moduleName)) {
                result.put(entity.getKey(), entity.getValue());
            }
        }

        return result;
    }

    public void addEventListener(ConfigManager.ConfigChangeEventListener listener) {
        this.listeners.add(listener);
    }



    public interface ConfigChangeEventListener {
        void onChange(Map<Object, Object> var1, Map<Object, Object> var2, Map<Object, Object> var3, String var4);
    }

    /**
     * 查询${}格式的变量
     * @param key
     * @return
     */
    private String loop(String key) {
        //定义matcher匹配其中的路径变量
        Matcher matcher = PATTERN.matcher(key);
        StringBuffer buffer = new StringBuffer();
        boolean flag = false;
        while (matcher.find()) {
            String matcherKey = matcher.group(1);//依次替换匹配到的路径变量
            String matchervalue = this.properties.getProperty(matcherKey);
            if (matchervalue != null) {
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(matchervalue));//quoteReplacement方法对字符串中特殊字符进行转化
                flag = true;
            }
        }
        matcher.appendTail(buffer);
        //flag为false时说明已经匹配不到路径变量，则不需要再递归查找
        return flag ? loop(buffer.toString()) : key;
    }
}
