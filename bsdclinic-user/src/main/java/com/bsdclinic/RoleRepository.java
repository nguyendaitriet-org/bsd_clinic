package com.bsdclinic;

import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByRoleId(String roleId);
}
