package com.peas.xinrui.api.user.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.common.repository.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    Set<User> findByIdIn(Collection<Long> keys);

    User findByMobileAndSchoolId(String mobile, Integer schoolId);

    User findByName(String name);

    User findByIdentity(String identity);

    @Query(value = "select * from user where identity = :identity", nativeQuery = true)
    User fineByIdentity(String identity);

    @Transactional
    @Query("update User set password = :password where id = :id")
    @Modifying
    void updateByPassword(Long id, String password);

    @Transactional
    @Query("update User set state = :state where id = :id")
    @Modifying
    void updateByState(Long id, Byte state);

    User findByNameAndSchoolId(String name, Integer schoolId);

    @Transactional
    @Query("update User set mobile = :mobile where id = :id")
    @Modifying
    void updateByMobile(Long id, String mobile);
}