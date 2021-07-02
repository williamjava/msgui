package com.opensource.msgui.ctl.user.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whj
 */
@ApiModel("用户对象")
@Data
public class UserVo {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "正式姓名")
    private String realname;

    @ApiModelProperty(value = "国籍")
    private String nationality;

    @ApiModelProperty(value = "注册码")
    private Integer registerCode;

    @ApiModelProperty(value = "头像")
    private String headPic;

    @ApiModelProperty(value = "来源")
    private String source;
}
