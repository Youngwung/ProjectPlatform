package com.ppp.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/")
@RequiredArgsConstructor
public class MainApiController {

    @GetMapping("/")
    public String main(){
        return "Hello World";
    }
    @GetMapping("/findproject")
    public String findProject(){
        return "Hello World";
    }

//    @GetMapping("/joinproject")
//    public String joinProject() {
//        return "Hello World";
//    }
}
