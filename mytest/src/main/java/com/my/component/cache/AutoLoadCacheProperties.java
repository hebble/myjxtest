package com.my.component.cache;

import com.jarvis.cache.to.AutoLoadConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AutoLoadCache配置
 *
 */
@Data
@ConfigurationProperties("autolaod.cache")
public class AutoLoadCacheProperties extends AutoLoadConfig {
  private boolean printSlowLog = false;
  private String nameSpace;
}
