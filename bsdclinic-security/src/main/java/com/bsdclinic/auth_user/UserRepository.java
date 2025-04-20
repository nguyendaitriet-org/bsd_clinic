package com.bsdclinic.auth_user;

import com.bsdclinic.dto.IUserResult;
import com.bsdclinic.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("""
            SELECT
                u.userId,
                u.fullName,
                u.email,
                u.password,
                u.phone,
                u.status,
                r.code AS role
            FROM User AS u
            INNER JOIN Role AS r ON u.roleId=r.roleId
            WHERE u.email = :email
    """)
    IUserResult findByEmailWithRole(String email);
}
