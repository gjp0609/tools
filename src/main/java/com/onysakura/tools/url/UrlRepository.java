package com.onysakura.tools.url;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, String> {

    Optional<UrlEntity> findByUrl(String url);

}
