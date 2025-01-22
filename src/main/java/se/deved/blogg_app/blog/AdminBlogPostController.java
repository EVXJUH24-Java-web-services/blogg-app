package se.deved.blogg_app.blog;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminBlogPostController {

    @DeleteMapping("/delete-post")
    public String helloWorld() {
        return "Hello World!";
    }
}
