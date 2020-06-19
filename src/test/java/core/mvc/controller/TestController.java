package core.mvc.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

@Controller
public class TestController {
    @RequestMapping(value = "/tests", method = RequestMethod.GET)
    void testInt(int i) {
    }

    @RequestMapping(value = "/tests", method = RequestMethod.GET)
    void testInt(long i) {
    }
}
