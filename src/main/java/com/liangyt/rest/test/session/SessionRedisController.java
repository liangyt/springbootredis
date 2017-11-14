package com.liangyt.rest.test.session;

import com.liangyt.common.rest.MessageReturn;
import com.liangyt.common.view.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 描述：测试session数据是否存储到redis中了<br>
 *     测试session共享的问题：<br>
 *        把项目打包，分别起多个服务，在其中一个执行添加操作，在其它服务执行获取操作
 *
 * @author tony
 * @创建时间 2017-10-19 14:03
 */
@RestController
@RequestMapping(value = "/test/session")
public class SessionRedisController extends BaseController{

    /**
     * 收集数据保存到session中
     * @param key
     * @param value
     * @param session
     * @return
     */
    @RequestMapping(value = "/put")
    public Object sessionPut(@RequestParam("key") String key,
                             @RequestParam("value") String value,
                             HttpSession session) {
        session.setAttribute(key, value);
        return MessageReturn.success();
    }

    /**
     * 返回session中的数据
     * @param key
     * @param session
     * @return
     */
    @RequestMapping(value = "/get")
    public Object sessionGet(@RequestParam("key") String key,
                             HttpSession session) {
        return MessageReturn.success(session.getAttribute(key));
    }
}
