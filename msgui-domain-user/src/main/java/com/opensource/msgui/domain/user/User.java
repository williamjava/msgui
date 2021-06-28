package com.opensource.msgui.domain.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.opensource.msgui.domain.base.Domain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_user")
public class User extends Domain {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

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