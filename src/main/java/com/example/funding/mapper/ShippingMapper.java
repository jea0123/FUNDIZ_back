package com.example.funding.mapper;

import com.example.funding.dto.request.shipping.ShippingStatusDto;
import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.model.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingMapper {
    List<CreatorShippingBackerList> creatorShippingBackerList(@Param("creatorId") Long creatorId,
                                                              @Param("projectId")Long projectId);

    int addShipping(Shipping shipping);

    int updateShippingStatus(@Param("projectId") Long projectId,
                             @Param("creatorId") Long creatorId,
                             @Param("shipping")ShippingStatusDto shippingStatusDto);

    void updateBackingToShippingStatus(Long backingId);
}
