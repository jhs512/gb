package com.gb.sapp.domain.post.post;

import com.gb.sapp.domain.post.post.entity.Post;
import com.gb.sapp.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    @GetMapping
    public List<Post> getItems() {
        return postService.findByOrderByIdDesc();
    }

    @GetMapping("/{id}")
    public Post getItem(
            @PathVariable long id
    ) {
        return postService.findById(id).get();
    }

    @DeleteMapping("/{id}")
    public void deleteItem(
            @PathVariable long id
    ) {
        postService.deleteById(id);
    }

    @AllArgsConstructor
    @Getter
    public static class PostModifyItemReqBody {
        @NotBlank
        public String title;
        @NotBlank
        public String body;
    }

    @PutMapping("/{id}")
    public Post modifyItem(
            @PathVariable long id,
            @RequestBody @Valid PostModifyItemReqBody reqBody
    ) {
        Post post = postService.findById(id).get();

        postService.modify(post, reqBody.title, reqBody.body);

        return post;
    }

    @AllArgsConstructor
    @Getter
    public static class PostWriteItemReqBody {
        @NotBlank
        public String title;
        @NotBlank
        public String body;
    }

    @PostMapping
    public Post writeItem(
            @RequestBody @Valid PostWriteItemReqBody reqBody
    ) {
        return postService.write(reqBody.title, reqBody.body);
    }
}
