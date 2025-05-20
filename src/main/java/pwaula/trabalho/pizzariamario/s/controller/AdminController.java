package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    public ModelAndView controlPanel() {
        return new ModelAndView("controlpanel");
    }

}
