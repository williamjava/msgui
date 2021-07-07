package com.opensource.msgui.domain.stock;

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
@TableName("s_stock")
public class Stock extends Domain {
    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品数量")
    private Integer productNum;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal productPrice;
}