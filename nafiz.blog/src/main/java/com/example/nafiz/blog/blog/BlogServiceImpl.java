package com.example.nafiz.blog.blog;

import com.example.nafiz.blog.common.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    private BlogDTO mapToDTO(BlogEntity entity) {
        BlogDTO dto = new BlogDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        return dto;
    }

    private BlogEntity mapToEntity(BlogDTO dto) {
        BlogEntity entity = new BlogEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        return entity;
    }

    @Override
    public Map<String, Object> createBlog(BlogDTO blogDTO) {
        BlogEntity saved = blogRepository.save(mapToEntity(blogDTO));
        log.info("Created blog with ID: {}", saved.getId());
        return ResponseWrapper.wrap("Blog created successfully", mapToDTO(saved));
    }

    @Override
    public Map<String, Object> getBlogById(String id) {
        BlogEntity blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        log.info("Fetched blog with ID: {}", id);
        return ResponseWrapper.wrap("Blog fetched successfully", mapToDTO(blog));
    }

    @Override
    public Map<String, Object> getAllBlogs() {
        List<BlogDTO> blogs = blogRepository.findAll().stream()
                .map(this::mapToDTO).collect(Collectors.toList());
        log.info("Fetched {} blog(s)", blogs.size());
        return ResponseWrapper.wrap("All blogs fetched successfully", blogs);
    }

    @Override
    public Map<String, Object> updateBlog(String id, BlogDTO blogDTO) {
        BlogEntity blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        BlogEntity updated = blogRepository.save(blog);
        log.info("Updated blog with ID: {}", id);
        return ResponseWrapper.wrap("Blog updated successfully", mapToDTO(updated));
    }

    @Override
    public Map<String, Object> deleteBlog(String id) {
        if (!blogRepository.existsById(id)) {
            throw new RuntimeException("Blog not found with id: " + id);
        }
        blogRepository.deleteById(id);
        log.info("Deleted blog with ID: {}", id);
        return ResponseWrapper.wrap("Blog deleted successfully", null);
    }
}
