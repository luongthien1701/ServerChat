package com.example.demo.worker;

/**
 * Interface đại diện cho một task bất đồng bộ cần được thực thi bởi DBWorker.
 * Tên theo chuẩn Java convention (PascalCase).
 */
public interface DbTask {
    void execute();
}
