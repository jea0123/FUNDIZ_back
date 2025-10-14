package com.example.funding.mapper;

import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.model.Shipping;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShippingMapper {
    List<CreatorShippingBackerList> creatorShippingBackerList(Long creatorId, Long projectId);

    int addShipping(Shipping shipping);
}
