package com.homefirstindia.homefirstwebsite.repository

import com.homefirstindia.homefirstwebsite.model.InternApplication
import com.homefirstindia.homefirstwebsite.model.JobApplication
import com.homefirstindia.homefirstwebsite.model.JobCity
import com.homefirstindia.homefirstwebsite.model.JobList
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.Optional


@Component
@Transactional
class CareersMasterRepository(
    @Autowired val jobListRepository: JobListRepository,
    @Autowired val jobApplicationRepository: JobApplicationRepository,
    @Autowired val jobCityRepository: JobCityRepository,
    @Autowired val internJobApplicationRepository: InternJobApplicationRepository

)



@Repository
interface JobListRepository : JpaRepository<JobList, String>{
    fun findByJobType(jobType: String): List<JobList>?
}

@Repository
interface JobApplicationRepository : JpaRepository<JobApplication, String> {

//    fun findByJobLists(jobList: JobList): List<JobApplication>?
    fun findByEmail( email: String ): Optional<JobApplication>


}

@Repository
interface JobCityRepository : JpaRepository<JobCity, String>{

    @Query(value = "From JobCity jc where jc.job.jobType = :jobType")
    fun findByJobType(jobType: String): List<JobCity>?


}

@Repository
interface InternJobApplicationRepository : JpaRepository<InternApplication, String>{}
