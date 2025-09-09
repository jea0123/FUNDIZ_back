package com.example.funding.mapper;

import com.example.funding.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    List<Tag> getTagListById(@Param("projectId") Long projectId);

    void saveTagList(@Param("projectId") Long projectId, @Param("tagName") String tagName);
}
