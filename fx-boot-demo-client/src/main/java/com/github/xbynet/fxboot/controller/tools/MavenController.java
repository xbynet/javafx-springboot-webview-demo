package com.github.xbynet.fxboot.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author anzhou.tjw
 * @date 2018/6/13 下午3:17
 */
@Controller
@RequestMapping("/tools/maven")
public class MavenController {

    @RequestMapping("")
    public String index(){
        return "/tools/maven.html";
    }
}
