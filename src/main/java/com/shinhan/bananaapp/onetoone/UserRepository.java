package com.shinhan.bananaapp.onetoone;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,String> {

}
