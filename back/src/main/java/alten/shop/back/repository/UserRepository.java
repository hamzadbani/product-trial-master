package alten.shop.back.repository;

import alten.shop.back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    @Query("select u from UserEntity u where u.email = :email")
    UserEntity findUserByEmail(@Param("email") String email);

    @Query("select u from UserEntity u where u.id = :id")
    UserEntity findUserByID(@Param("id") int id);


}
