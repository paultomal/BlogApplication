package com.example.nafiz.blog.blog;

import java.util.Map;

public interface BlogService {
    Map<String, Object> createBlog(BlogDTO blogDTO);
    Map<String, Object> getBlogById(String id);
    Map<String, Object> getAllBlogs();
    Map<String, Object> updateBlog(String id, BlogDTO blogDTO);
    Map<String, Object> deleteBlog(String id);
}
