package com.bsdclinic.repository;

import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUserId(String userId);

    @Query("""
            SELECT
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
}
