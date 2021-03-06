package com.hotel.controller;

import com.hotel.model.Employ;
import com.hotel.model.Event;
import com.hotel.model.Result;
import com.hotel.service.EventService;
import com.hotel.util.ResultReturn;
import com.hotel.service.EmployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployController
{
    private final EmployService employservice;

    @Autowired
    public EmployController(EmployService employservice)
    {
        this.employservice=employservice;
    }

    @Autowired
    EventService eventService;

    @RequestMapping("/employ/list")
    public Result<Employ> employList() {
        return ResultReturn.success(employservice.findAll());
    }

    @RequestMapping("/employ/searchOne/{employno}")
    public Result employSearchOne(@PathVariable("employno") int employno) {
        Employ r = employservice.findByEmployno(employno);
        if(r == null) {
            return ResultReturn.error(1,"it's not exist, you can't delete!");
        }
        else {
            return ResultReturn.success(r);
        }
    }

    @RequestMapping("/employ/add")
    public Result employAdd(@RequestParam("employno")int employno,@RequestParam("employname") String employname,
                            @RequestParam("employsex")int employsex,@RequestParam("employage") int employage,
                            @RequestParam("employposition")int employposition,
                            @RequestParam("employauthority") int employauthority,
                            @RequestParam("employpaymentpermonth")int employpaymentpermonth,
                            @RequestParam("employworktime") int employworktime,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password) {
        Employ e = employservice.findByEmployno(employno);
        if(e!=null)
            return ResultReturn.error(2,"that employno arleady exist");
        else{
            e = saveEmploy(employno,employname,employsex,employage,employposition,employauthority,
                    employpaymentpermonth,employworktime,username,password);
            return ResultReturn.success(employservice.save(e));
        }
    }

    @RequestMapping("/employ/update/{employno}")
    public Result employUpdate(@PathVariable("employno")int employno,@RequestParam("employname") String employname,
                               @RequestParam("employsex")int employsex,@RequestParam("employage") int employage,
                               @RequestParam("employposition")int employposition,
                               @RequestParam("employauthority") int employauthority,
                               @RequestParam("employpaymentpermonth")int employpaymentpermonth,
                               @RequestParam("employworktime") int employworktime,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password) {
        Employ e = employservice.findByEmployno(employno);
            if(e==null) {
            return ResultReturn.error(1,"that employno did not exist");
        }
        else{
            e = saveEmploy(employno,employname,employsex,employage,employposition,employauthority,
                    employpaymentpermonth,  employworktime,username,password);
            return ResultReturn.success(employservice.save(e));
        }

    }

    @RequestMapping("/employ/delete/{employno}")
    public Result employDelete(@PathVariable("employno")int employno) {
        Employ e = employservice.findByEmployno(employno);
        if (e==null){
            return ResultReturn.error(1,"can't find this employno");
        }
        employservice.delete(e);
        return ResultReturn.success(e);
    }

    @RequestMapping("/employ/personalMeasage")
    public Result getPersonalMeasage() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        Employ e = employservice.findByUsernameAndPassword(username,password);
        System.out.println("employ measage:"+e);
        return ResultReturn.success(e);

    }

    @RequestMapping("/employ/personalEvent")
    public Result getPersonalEvent() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        Employ e = employservice.findByUsernameAndPassword(username,password);
        List<Event> event = eventService.findAllByEmployno(e.getEmployno());
        System.out.println("event: "+event);
        return ResultReturn.success(event);

    }

    public Employ saveEmploy(int employno,String employname,int employsex,int employage,
                             int employposition,int employauthority,
                             int employpaymentpermonth,int employworktime,
                             String username,String password) {
        Employ e = new Employ();
        e.setEmployno(employno);
        e.setEmployposition(employposition);
        e.setEmployworktime(employworktime);
        e.setEmployage(employage);
        e.setEmploysex(employsex);
        e.setEmployname(employname);
        e.setEmploypaymentpermonth(employpaymentpermonth);
        e.setEmployauthority(employauthority);
        e.setUsername(username);
        e.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        return e;
    }


}
