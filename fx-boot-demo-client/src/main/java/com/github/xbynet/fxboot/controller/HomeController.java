package com.github.xbynet.fxboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author anzhou.tjw
 * @date 2018/6/8 下午5:08
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mv=new ModelAndView("/home.html");
        mv.getModel().put("title","777测试-1");
        return mv;
    }

}
