package com.liangyt.config.session;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 描述：配置Session失效时间
 *
 * @author tony
 * @创建时间 2017-10-19 13:24
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60)
public class SessionConfig {

}
