package com.example.dao;

import com.example.bean.AdminUser;
import org.apache.ibatis.annotations.Param;

public interface AdminUserMapper {
    AdminUser selectByUsername(@Param("username") String username);
}
