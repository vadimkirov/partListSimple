package com.example.partlist.repository;

import com.example.partlist.model.PartModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainRepository extends JpaRepository<PartModel,Integer> {

    Page<PartModel> findAll(Pageable pageable);
    Page<PartModel> findAllByTitleIsContainingIgnoreCase(String string, Pageable pageable);

    Page<PartModel> findByRequired(Boolean iRequired, Pageable pageable);
    List<PartModel> findByRequiredIsTrue();
}
