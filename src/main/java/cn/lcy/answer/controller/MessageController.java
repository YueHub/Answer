package cn.lcy.answer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
	
	private static String result = "test";

    @RequestMapping("/request")
    public Message request(@RequestParam(value="content", defaultValue="hello, world") String content) {
    	
    	return new Message(String.format(result));
    }

}
