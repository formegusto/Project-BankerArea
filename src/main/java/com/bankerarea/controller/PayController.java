package com.bankerarea.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bankerarea.common.KakaoPay;

import lombok.extern.java.Log;

@Log
@Controller
public class PayController {
    
    @Autowired
    private KakaoPay kakaopay;
    
    
    @GetMapping("/kakaoPay")
    public void kakaoPayGet() {
        
    }
    
    @ResponseBody
    @PostMapping("/kakaoPay")
    public String kakaoPay() {
        return kakaopay.kakaoPayReady();
    }
    
    @GetMapping("/kakaoPaySuccess")
    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model,
    		HttpServletResponse res) {
        System.out.println(pg_token);
        System.out.println("여기까지오면 성공한거");
        
        res.setStatus(HttpServletResponse.SC_OK);
        
    }
    
}
 
