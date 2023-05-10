package com.love.product.entity.base;

import cn.hutool.db.PageResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 分页查询封装
 */
@Data
@Builder
@Accessors(chain = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Component
@EqualsAndHashCode(callSuper = true)
public class ResultPage<T> extends Result implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页，从1开始")
    private int currentPage = 1;

    @ApiModelProperty(value = "每页数量 默认10")
    private int pageSize = 10;

    @ApiModelProperty(value = "总页数")
    private int pageTotal;

    @ApiModelProperty(value = "数据总数")
    private int dataTotal;

    public static ResultPage OK(int dataTotal,int currentPage, int pageSize, List<?> data) {
        if(pageSize == 0){//防止pageSize等于0引发的错误：java.lang.ArithmeticException: / by zero
            pageSize = 10;
        }
        ResultPage resultPage = new ResultPage();
        resultPage.setCode(ResultCode.SUCCESS.getCode());
        resultPage.setMsg(ResultCode.SUCCESS.getMsg());
        resultPage.setData(data);
        resultPage.setDataTotal(dataTotal);
        resultPage.setCurrentPage(currentPage);
        resultPage.setPageSize(pageSize);
        int pageTotal = dataTotal / pageSize;
        if(dataTotal % pageSize != 0){
            pageTotal += 1;
        }
        resultPage.setPageTotal(pageTotal);
        return resultPage;
    }

    public static <T> ResultPage<T> OK(long dataTotal, long currentPage, long pageSize, Collection<T> data) {
        if (pageSize == 0) {//防止pageSize等于0引发的错误：java.lang.ArithmeticException: / by zero
            pageSize = 10;
        }
        ResultPage<T> resultPage = new ResultPage();
        resultPage.setData(data);
        resultPage.setDataTotal(Integer.parseInt(String.valueOf(dataTotal)));
        resultPage.setCode(ResultCode.SUCCESS.getCode());
        resultPage.setMsg(ResultCode.SUCCESS.getMsg());
        resultPage.setCurrentPage(Integer.parseInt(String.valueOf(currentPage)));
        resultPage.setPageSize(Integer.parseInt(String.valueOf(pageSize)));
        int pageTotal = Integer.parseInt(String.valueOf(dataTotal)) / Integer.parseInt(String.valueOf(pageSize));
        if(dataTotal % pageSize != 0){
            pageTotal += 1;
        }
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
