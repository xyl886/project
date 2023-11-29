package com.love.product.util;

import com.love.product.entity.Posts;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/school?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&rewriteBatchedStatements=true&useAffectedRows=true&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "root";
        long begin = 1;//起始id
        long end;
        // 数据总条数
        // 创建Scanner对象
        Scanner scanner = new Scanner(System.in);

        // 获取用户输入的数据总条数n
        System.out.print("请输入数据总条数：");
        int totalRecords = scanner.nextInt();

        // 关闭Scanner
        scanner.close();

        // 每次批处理的条数
        int batchSize = totalRecords / 10; // 每次处理的条数m自行均分，且不超过总条数的十分之一

        end = begin + batchSize;//每次循环插入的数据量
        // 创建数据库连接
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // 开启事务
            connection.setAutoCommit(false);

            // 创建预编译语句对象
            String sql = "INSERT INTO s_posts_copy ( id, user_id, posts_type, title, description, content, school, price, cover_path, img_path, browse_num, collect_num, like_num, comment_num, version, status, deleted, create_time, update_time ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ? , ?, ? )";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                //开始总计时
                long bTime1 = System.currentTimeMillis();

                // 插入数据并批处理
                for (int i = 1; i <= totalRecords / 10 && begin <= totalRecords; i++) {
                    //开启分段计时，计1W数据耗时
                    long bTime = System.currentTimeMillis();
                    while (begin < end) {
                        // 设置参数
                        // 随机生成数据
                        Long id =generateRandomLong();
                        long userId = 1611899504066359297L;
                        Integer postsType = 2;
                        String title = generateRandomString();
                        String description = generateRandomString();
                        String content = generateRandomString();
                        Integer school = 2;
                        BigDecimal price = BigDecimal.valueOf(0);
                        String coverPath = "https://18066.oss-cn-hangzhou.aliyuncs.com/aed6a238a5244b9dbbcec7e116f55a6b.jpg";
                        String imgPath = "https://18066.oss-cn-hangzhou.aliyuncs.com/aed6a238a5244b9dbbcec7e116f55a6b.jpg";
                        Integer browseNum = 0;
                        Integer collectNum = 0;
                        Integer likeNum = 0;
                        Integer commentNum = 0;
                        Integer version = 0;
                        Integer status = 3;
                        Integer deleted = 0;
                        LocalDateTime createTime = LocalDateTime.now();
                        LocalDateTime updateTime = LocalDateTime.now();
                        // 设置参数
                        statement.setLong(1, id);
                        statement.setLong(2, userId);
                        statement.setInt(3, postsType);
                        statement.setString(4, title);
                        statement.setString(5, description);
                        statement.setString(6, content);
                        statement.setInt(7, school);
                        statement.setBigDecimal(8, price);
                        statement.setString(9, coverPath);
                        statement.setString(10, imgPath);
                        statement.setInt(11, browseNum);
                        statement.setInt(12, collectNum);
                        statement.setInt(13, likeNum);
                        statement.setInt(14, commentNum);
                        statement.setInt(15, version);
                        statement.setInt(16, status);
                        statement.setInt(17,deleted);
                        statement.setString(18, createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        statement.setString(19, updateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        // 添加到批处理
                        statement.addBatch();
                        begin++;
                    }
                    statement.executeBatch();
                    connection.commit();
                    end += batchSize;
                    //关闭分段计时
                    long eTime = System.currentTimeMillis();
                    //输出
                    System.out.println("成功插入" + batchSize + "条数据耗时：" + (eTime - bTime));
                    System.out.println(begin+","+end);
                }
                //关闭总计时
                long eTime1 = System.currentTimeMillis();
                //输出
                System.out.println("插入" + totalRecords + "数据共耗时：" + (eTime1 - bTime1));
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 生成随机Long类型数据
    private static Long generateRandomLong() {
        Random random = new Random();
        return random.nextLong();
    }

    // 生成随机Integer类型数据
    private static Integer generateRandomInteger() {
        Random random = new Random();
        return random.nextInt();
    }

    // 生成随机String类型数据
    private static String generateRandomString() {
        Random random = new Random();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return new String(bytes);
    }

    // 生成随机BigDecimal类型数据
    private static BigDecimal generateRandomBigDecimal() {
        Random random = new Random();
        return BigDecimal.valueOf(random.nextDouble());
    }
}
