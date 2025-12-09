package com.example.util;

import com.example.bean.AdminUser;
import com.example.dao.AdminUserMapper;
import org.apache.ibatis.session.SqlSession;

public class AdminUserUtil {

    public AdminUser findByUsername(String username) {
        try (SqlSession session = MyBatisUtils.openSession()) {
            AdminUserMapper mapper = session.getMapper(AdminUserMapper.class);
            return mapper.selectByUsername(username);
        }
    }

    public boolean checkPassword(AdminUser user, String rawPassword) {
        if (user == null || rawPassword == null)
            return false;
        // 暂时使用明文对比，后续可改为哈希校验
        return rawPassword.equals(user.getPassword());
    }
}
