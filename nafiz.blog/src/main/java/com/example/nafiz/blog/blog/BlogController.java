package com.example.nafiz.blog.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createBlog(@RequestBody BlogDTO blogDTO) {
        return ResponseEntity.ok(blogService.createBlog(blogDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBlog(@PathVariable String id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBlog(@PathVariable String id, @RequestBody BlogDTO blogDTO) {
        return ResponseEntity.ok(blogService.updateBlog(id, blogDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBlog(@PathVariable String id) {
        return ResponseEntity.ok(blogService.deleteBlog(id));
    }
}
