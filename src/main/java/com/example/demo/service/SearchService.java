package com.example.demo.service;

import com.example.demo.dto.search.SelectSearch;

import java.util.List;

public interface SearchService {
    List<SelectSearch> searchAll(Long userId, String query);
}
