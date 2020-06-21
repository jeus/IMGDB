package nl.lunatech.movie.imgdb.core.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Using for checking swagger and web service
 * @author alikhandani
 * @created 27/05/2020
 * @project imgdb
 */
@RestController
@RequestMapping("/hello")
public class SampleController {

    @GetMapping
    public String sayHello() {
        return "hello world";
    }

}