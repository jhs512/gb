package com.gb.sapp.domain.post.post;

import com.gb.sapp.domain.member.member.entity.Member;
import com.gb.sapp.domain.member.member.service.MemberService;
import com.gb.sapp.domain.post.post.entity.Post;
import com.gb.sapp.domain.post.post.service.PostService;
import com.gb.sapp.global.app.AppConfig;
import com.gb.sapp.standard.base.KwTypeV1;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public Page<Post> getItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeV1 kwType
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Post> itemPage = postService.findByKw(kwType, kw, null, true, true, pageable);

        return itemPage;
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

        @NotNull
        boolean published;

        @NotNull
        boolean listed;
    }

    @PostMapping
    public Post writeItem(
            @RequestBody @Valid PostWriteItemReqBody reqBody
    ) {
        Member author = memberService.findById(3).get();

        return postService.write(author, reqBody.title, reqBody.body, reqBody.published, reqBody.listed);
    }
}
