package org.study.community.web;

import lombok.RequiredArgsConstructor;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.study.community.config.auth.LoginUser;
import org.study.community.config.auth.dto.SessionUser;
import org.study.community.service.PostsService;
import org.study.community.web.dto.PostsListResponseDto;
import org.study.community.web.dto.PostsResponseDto;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @RequestParam(value= "page", defaultValue = "1") Integer pageNum, @LoginUser SessionUser user) {

//        model.addAttribute("posts", postsService.findAllDesc());
        model.addAttribute("posts",postsService.getPageList(pageNum));
        model.addAttribute("previous", postsService.getPrev(pageNum));
        model.addAttribute("next", postsService.getNext(pageNum));
        model.addAttribute("pageNum", postsService.getPageNum(pageNum));

        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model,@LoginUser SessionUser user) {

        if(user != null){
            model.addAttribute("userId", user.getId());
        }
        return "posts-save";
    }


    @GetMapping("/posts/read/{id}")
    public String postsRead(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        if(user != null){
            if(user.getId().equals(dto.getWriterId())){
                model.addAttribute("writerName", true);
            }
           if(user.getRoleKey().equals("ROLE_ADMIN")){
               model.addAttribute("isAdmin",true);
           }
        }
        return "posts-read";
    }


    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }



    @GetMapping("/posts/search")
    public String search(String keyword, Model model, @RequestParam(value= "page", defaultValue = "1") Integer pageNum, @LoginUser SessionUser user) {

        model.addAttribute("posts",postsService.searchPosts(keyword, pageNum));
        model.addAttribute("previous", postsService.getPrev(pageNum));
        model.addAttribute("next", postsService.getNext(pageNum));
        model.addAttribute("pageNum", postsService.getPageNum(pageNum));

        model.addAttribute("keyword", keyword);



        if(user != null){
            model.addAttribute("userName", user.getName());
        }

        return "search-page";
    }
}
