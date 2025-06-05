package com.bsdclinic.repository;

import com.bsdclinic.dto.response.IUserSelectResponse;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUserId(String userId);

    boolean existsByEmailAndUserIdNot(String email, String userId);

    boolean existsByPhoneAndUserIdNot(String phone, String userId);

    @Query("""
            SELECT
                u.userId AS userId,
                u.email AS email,
                u.phone AS phone,
                u.fullName AS fullName,
                u.status AS status,
                r.code AS role,
                u.createdAt AS createdAt
            FROM User AS u
            INNER JOIN Role AS r ON u.roleId = r.roleId
            WHERE u.userId = :userId
    """)
    IUserResponse findByIdRole(String userId);

    @Query("""
            SELECT
                u.userId AS userId,
                u.fullName AS fullName
            FROM User AS u
            INNER JOIN Role AS r ON u.roleId = r.roleId
            WHERE u.status = "ACTIVE" AND r.code IN :roleCodes
    """)
    List<IUserSelectResponse> findUsersForSelectByRoles(List<String> roleCodes);
}
