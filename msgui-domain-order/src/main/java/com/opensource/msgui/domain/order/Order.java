package com.opensource.msgui.domain.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.opensource.msgui.domain.base.Domain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("o_order")
public class Order extends Domain {
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "订单单号")
    private String orderNo;

    @ApiModelProperty(value = "库存ID")
    private Long stockId;

    @ApiModelProperty(value = "库存数量")
    private Integer stockNum;

    @ApiModelProperty(value = "金额")
    private BigDecimal totalPrice;
}