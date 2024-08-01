package com.homefirstindia.homefirstwebsite.repository

import com.homefirstindia.homefirstwebsite.model.common.City
import com.homefirstindia.homefirstwebsite.model.common.State
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.Optional

@Component
@Transactional
class CommonMasterRepository(
    @Autowired val cityRepository: CityRepository,
    @Autowired val stateRepository: StateRepository,

)

@Repository
interface CityRepository : JpaRepository<City, String> {

    fun findByNameIn(name: List<String>?) : List<City>?

}

@Repository
interface StateRepository : JpaRepository<State, String> {}