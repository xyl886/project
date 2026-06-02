package com.love.product.entity.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Administrator
 * @date 2022-10-19 10:26
 * @describe 分页查询封装
 */
@Data
@Builder
@Accessors(chain = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResultPage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("返回状态码： (200操作成功) (500操作失败)")
    private Integer code = 200;

    @ApiModelProperty(value = "前端toast展示的信息")
    private String msg = "操作成功";

    @ApiModelProperty(value = "返回数据")
    private T data;

    @ApiModelProperty(value = "当前页，从1开始")
    private int currentPage = 1;

    @ApiModelProperty(value = "每页数量 默认10")
    private int pageSize = 10;

    @ApiModelProperty(value = "总页数")
    private int pageTotal;

    @ApiModelProperty(value = "数据总数")
    private int dataTotal;

    public static <T> ResultPage<T> OK(long dataTotal, long currentPage, long pageSize, Collection<T> data) {
        if (pageSize == 0) {
            pageSize = 10;
        }
        int ps = (int) pageSize;
        int total = (int) dataTotal;
        int pageTotal = total / ps;
        if (total % ps != 0) {
            pageTotal += 1;
        }
        ResultPage<T> resultPage = new ResultPage<>();
        resultPage.setCode(ResultCode.SUCCESS.getCode());
        resultPage.setMsg(ResultCode.SUCCESS.getMsg());
        resultPage.setData((T) data);
        resultPage.setDataTotal(total);
        resultPage.setCurrentPage((int) currentPage);
        resultPage.setPageSize(ps);
        resultPage.setPageTotal(pageTotal);
        return resultPage;
    }

    public static ResultPage FAIL() {
        ResultPage resultPage = new ResultPage();
        resultPage.setCode(ResultCode.FAILED.getCode());
        resultPage.setMsg(ResultCode.FAILED.getMsg());
        resultPage.setData(new ArrayList<>());
        return resultPage;
    }

    public static ResultPage FAIL(Integer code, String msg) {
        ResultPage resultPage = new ResultPage();
        resultPage.setCode(code);
        resultPage.setMsg(msg);
        return resultPage;
    }
}
