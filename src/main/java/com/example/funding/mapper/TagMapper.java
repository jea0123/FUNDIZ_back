package com.example.funding.mapper;

import com.example.funding.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    List<Tag> getTagList(@Param("projectId") Long projectId);

    void saveTag(@Param("projectId") Long projectId, @Param("tagName") String tagName);

    void deleteTags(@Param("projectId") Long projectId);
}
