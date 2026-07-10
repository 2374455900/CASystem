package com.kuugae.ca.test;

import com.kuugae.ca.entity.Certificate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import javax.sql.DataSource;

@Component
public class DatabaseCheckRunner implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;
    
    private final DataSource dataSource;

    public DatabaseCheckRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== 检查 certificate 表结构 ===");
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "certificate", null);
            
            System.out.println("certificate 表的列定义：");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String typeName = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                String isNullable = columns.getString("IS_NULLABLE");
                
                if (columnName.equals("username") || columnName.equals("user_public_key") || 
                    columnName.equals("user_private_key_encrypted") || columnName.equals("organization")) {
                    System.out.printf("列名：%s, 类型：%s, 长度：%d, 可空：%s%n", 
                        columnName, typeName, columnSize, isNullable);
                }
            }
            columns.close();
        }
    }
}
