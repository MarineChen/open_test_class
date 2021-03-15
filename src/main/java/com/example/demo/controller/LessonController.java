package com.example.demo.controller;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.User;
import com.example.demo.model.LessonQo;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程控制器
 */
@Controller
@RequestMapping("/lesson")
public class LessonController {
    private static Logger logger = LoggerFactory.getLogger(LessonController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Value("${securityconfig.urlroles}")
    private String urlroles;

    @RequestMapping("/index")
    public String index(ModelMap model, Principal user) throws Exception{
        Authentication authentication = (Authentication)user;
        List<String> userroles = new ArrayList<>();
        for(GrantedAuthority ga : authentication.getAuthorities()){
            userroles.add(ga.getAuthority());
        }

        boolean subrole=false,unsubrole=false;
        if(!StringUtils.isEmpty(urlroles)) {
            String[] resouces = urlroles.split(";");
            for (String resource : resouces) {
                String[] urls = resource.split("=");
                if(urls[0].indexOf("subscribe") > 0){
                    String[] newroles = urls[1].split(",");
                    for(String str : newroles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            subrole = true;
                            break;
                        }
                    }
                }else if(urls[0].indexOf("unsub") > 0){
                    String[] editoles = urls[1].split(",");
                    for(String str : editoles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            unsubrole = true;
                            break;
                        }
                    }
                }
            }
        }

        model.addAttribute("subrole", subrole);
        model.addAttribute("unsubrole", unsubrole);
        model.addAttribute("user", user);
        return "lesson/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model, @PathVariable Long id) {
        Lesson lesson = lessonRepository.getOne(id);
        model.addAttribute("lesson",lesson);
        return "lesson/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Object> getList(LessonQo lessonQo) {
        try {
            Pageable pageable = PageRequest.of(lessonQo.getPage(), lessonQo.getSize());
            return lessonRepository.findByName(lessonQo.getName()==null?"%":"%"+lessonQo.getName()+"%", pageable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(){
        return "lesson/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Lesson lesson) throws Exception{
        lessonRepository.save(lesson);
        logger.info("新增->ID="+lesson.getId());
        return "1";
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model, @PathVariable Long id){
        Lesson lesson = lessonRepository.getOne(id);
        model.addAttribute("lesson",lesson);
        return "lesson/edit";
    }

    @RequestMapping(value="/subscribe/{id}")
    @ResponseBody
    public String subscribe(@PathVariable Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user=userRepository.findByName(userDetails.getUsername());
        Lesson lesson = lessonRepository.getOne(id);
        user.getLessons().remove(lesson);
        user.getLessons().add(lesson);
        userRepository.save(user);
        logger.info("订阅->ID="+lesson.getId());
        return "1";
    }

    @RequestMapping(value="/unsubscribe/{id}")
    @ResponseBody
    public String unsubscribe(@PathVariable Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user=userRepository.findByName(userDetails.getUsername());
        Lesson lesson = lessonRepository.getOne(id);
        user.getLessons().remove(lesson);
        userRepository.save(user);
        logger.info("取消订阅->ID="+lesson.getId());
        return "1";
    }


    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(Lesson lesson) throws Exception{
        lessonRepository.save(lesson);
        logger.info("修改->ID="+lesson.getId());
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id) throws Exception{
        lessonRepository.deleteById(id);
        logger.info("删除->ID="+id);
        return "1";
    }

}
