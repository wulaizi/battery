cmake_minimum_required(VERSION 3.4.1)

add_library(security SHARED
            security.cpp)

find_library(log-lib log)

target_link_libraries(security
                     ${log-lib})

# 添加日志宏定义
target_compile_definitions(security PRIVATE
    -DANDROID_LOG_TAG=\"SecurityUtil\") 