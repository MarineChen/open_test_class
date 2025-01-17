package com.example.demo.controller;

import com.example.demo.config.MyPasswordEncoder;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.model.UserQo;
import com.example.demo.repository.RoleRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${securityconfig.urlroles}")
    private String urlroles;

    @RequestMapping("/index")
    public String index(ModelMap model, Principal user) throws Exception{
        Authentication authentication = (Authentication)user;
        List<String> userroles = new ArrayList<>();
        for(GrantedAuthority ga : authentication.getAuthorities()){
            userroles.add(ga.getAuthority());
        }

        boolean newrole=false,editrole=false,deleterole=false;
        if(!StringUtils.isEmpty(urlroles)) {
            String[] resouces = urlroles.split(";");
            for (String resource : resouces) {
                String[] urls = resource.split("=");
                if(urls[0].indexOf("new") > 0){
                    String[] newroles = urls[1].split(",");
                    for(String str : newroles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            newrole = true;
                            break;
                        }
                    }
                }else if(urls[0].indexOf("edit") > 0){
                    String[] editoles = urls[1].split(",");
                    for(String str : editoles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            editrole = true;
                            break;
                        }
                    }
                }else if(urls[0].indexOf("delete") > 0){
                    String[] deleteroles = urls[1].split(",");
                    for(String str : deleteroles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            deleterole = true;
                            break;
                        }
                    }
                }
            }
        }

        model.addAttribute("newrole", newrole);
        model.addAttribute("editrole", editrole);
        model.addAttribute("deleterole", deleterole);

        model.addAttribute("user", user);
        return "user/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model, @PathVariable Long id) {
        User user = userRepository.getOne(id);
        model.addAttribute("user",user);
        return "user/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<User> getList(UserQo userQo) {
        try {
            Pageable pageable = PageRequest.of(userQo.getPage(), userQo.getSize());
            return userRepository.findByName(userQo.getName()==null?"%":"%"+userQo.getName()+"%", pageable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model, User user){
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        return "user/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(User user) throws Exception{
        user.setCreatedate(new Date());
        PasswordEncoder mpe = new MyPasswordEncoder();
        user.setPassword(mpe.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("新增->ID="+user.getId());
        return "1";
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model, @PathVariable Long id){
        User user = userRepository.getOne(id);

        List<Role> roles = roleRepository.findAll();

        List<Long> rids = new ArrayList<Long>();
        for(Role role : user.getRoles()){
            rids.add(role.getId());
        }

        model.addAttribute("user",user);
        model.addAttribute("roles", roles);
        model.addAttribute("rids", rids);
        return "user/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(User user) throws Exception{
        userRepository.save(user);
        logger.info("修改->ID="+user.getId());
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id) throws Exception{
        userRepository.deleteById(id);
        logger.info("删除->ID="+id);
        return "1";
    }

}
