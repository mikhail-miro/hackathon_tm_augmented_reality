package com.miro.hackathon.augmentedreality.repository;

import com.miro.hackathon.augmentedreality.entity.ImageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageEntityRepository extends CrudRepository<ImageEntity, Long> {

    List<ImageEntity> findAllByBoardId(String boardId);
}
