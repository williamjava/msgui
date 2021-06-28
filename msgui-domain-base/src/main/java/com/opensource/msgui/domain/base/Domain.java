package com.opensource.msgui.domain.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.io.Serializable;

public class Domain implements Serializable {
    private static final long serialVersionUID = 972890510484764455L;
    @TableId(
        value = "id",
        type = IdType.ASSIGN_ID
    )
    @JsonFormat(
        shape = Shape.STRING
    )
    private Long id;
    @TableField(
        value = "create_time",
        fill = FieldFill.INSERT
    )
    private String createTime;
    @TableField(
        value = "update_time",
        fill = FieldFill.INSERT_UPDATE
    )
    private String updateTime;
    @TableField(
        value = "is_deleted",
        fill = FieldFill.INSERT
    )
    private Boolean deleted;
    private Long createBy;
    private Long updateBy;

    public Domain() {
    }

    public Domain(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Long getCreateBy() {
        return this.createBy;
    }

    public Long getUpdateBy() {
        return this.updateBy;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setCreateTime(final String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(final String updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public void setCreateBy(final Long createBy) {
        this.createBy = createBy;
    }

    public void setUpdateBy(final Long updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Domain)) {
            return false;
        } else {
            Domain other = (Domain)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$id = this.getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }

                Object this$createTime = this.getCreateTime();
                Object other$createTime = other.getCreateTime();
                if (this$createTime == null) {
                    if (other$createTime != null) {
                        return false;
                    }
                } else if (!this$createTime.equals(other$createTime)) {
                    return false;
                }

                Object this$updateTime = this.getUpdateTime();
                Object other$updateTime = other.getUpdateTime();
                if (this$updateTime == null) {
                    if (other$updateTime != null) {
                        return false;
                    }
                } else if (!this$updateTime.equals(other$updateTime)) {
                    return false;
                }

                label62: {
                    Object this$deleted = this.getDeleted();
                    Object other$deleted = other.getDeleted();
                    if (this$deleted == null) {
                        if (other$deleted == null) {
                            break label62;
                        }
                    } else if (this$deleted.equals(other$deleted)) {
                        break label62;
                    }

                    return false;
                }

                label55: {
                    Object this$createBy = this.getCreateBy();
                    Object other$createBy = other.getCreateBy();
                    if (this$createBy == null) {
                        if (other$createBy == null) {
                            break label55;
                        }
                    } else if (this$createBy.equals(other$createBy)) {
                        break label55;
                    }

                    return false;
                }

                Object this$updateBy = this.getUpdateBy();
                Object other$updateBy = other.getUpdateBy();
                if (this$updateBy == null) {
                    if (other$updateBy != null) {
                        return false;
                    }
                } else if (!this$updateBy.equals(other$updateBy)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Domain;
    }

    @Override
    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        Object $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        Object $deleted = this.getDeleted();
        result = result * 59 + ($deleted == null ? 43 : $deleted.hashCode());
        Object $createBy = this.getCreateBy();
        result = result * 59 + ($createBy == null ? 43 : $createBy.hashCode());
        Object $updateBy = this.getUpdateBy();
        result = result * 59 + ($updateBy == null ? 43 : $updateBy.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Domain(id=" + this.getId() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleted=" + this.getDeleted() + ", createBy=" + this.getCreateBy() + ", updateBy=" + this.getUpdateBy() + ")";
    }
}
