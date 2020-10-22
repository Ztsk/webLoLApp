package com.project.webLoLApp.backend.repositories;

import com.project.webLoLApp.backend.SummonerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SummonerDataRepository extends JpaRepository<SummonerData, String> {

    @Query("select s from SummonerData s " + "where lower(s.name) like lower(concat('%', :searchTerm, '%')) ")
    List<SummonerData> search(@Param("searchTerm") String searchTerm);
}
