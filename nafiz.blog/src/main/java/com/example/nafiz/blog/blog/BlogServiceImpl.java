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
        try {
            BlogEntity saved = blogRepository.save(mapToEntity(blogDTO));
            log.info("Created blog with ID: {}", saved.getId());
            return ResponseWrapper.wrap("Blog created successfully", mapToDTO(saved));
        } catch (Exception e) {
            log.error("Failed to create blog", e);
            return ResponseWrapper.wrapFailure("Failed to create blog");
        }
    }


    @Override
    public Map<String, Object> getBlogById(String id) {
        try {
            BlogEntity blog = blogRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
            log.info("Fetched blog with ID: {}", id);
            return ResponseWrapper.wrap("Blog fetched successfully", mapToDTO(blog));
        } catch (Exception e) {
            log.error("Error fetching blog with ID: {}", id, e);
            return ResponseWrapper.wrapFailure(e.getMessage());
        }
    }


    @Override
    public Map<String, Object> getAllBlogs() {
        try {
            List<BlogDTO> blogs = blogRepository.findAll().stream()
                    .map(this::mapToDTO).collect(Collectors.toList());
            log.info("Fetched {} blog(s)", blogs.size());
            return ResponseWrapper.wrap("All blogs fetched successfully", blogs);
        } catch (Exception e) {
            log.error("Error fetching all blogs", e);
            return ResponseWrapper.wrapFailure("Failed to fetch blogs");
        }
    }


    @Override
    public Map<String, Object> updateBlog(String id, BlogDTO blogDTO) {
        try {
            BlogEntity blog = blogRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
            blog.setTitle(blogDTO.getTitle());
            blog.setContent(blogDTO.getContent());
            BlogEntity updated = blogRepository.save(blog);
            log.info("Updated blog with ID: {}", id);
            return ResponseWrapper.wrap("Blog updated successfully", mapToDTO(updated));
        } catch (Exception e) {
            log.error("Error updating blog with ID: {}", id, e);
            return ResponseWrapper.wrapFailure(e.getMessage());
        }
    }


    @Override
    public Map<String, Object> deleteBlog(String id) {
        try {
            if (!blogRepository.existsById(id)) {
                throw new RuntimeException("Blog not found with id: " + id);
            }
            blogRepository.deleteById(id);
            log.info("Deleted blog with ID: {}", id);
            return ResponseWrapper.wrap("Blog deleted successfully", null);
        } catch (Exception e) {
            log.error("Error deleting blog with ID: {}", id, e);
            return ResponseWrapper.wrapFailure(e.getMessage());
        }
    }

}
