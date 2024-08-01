package com.homefirstindia.homefirstwebsite.repository

import com.homefirstindia.homefirstwebsite.model.*
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository




@Component
@Transactional
class PublicRepository(
        @Autowired val testimonialDetailRepository: TestimonialDetailRepository,
        @Autowired val faqDetailRepository: FaqDetailRepository,
        @Autowired val searchMetaDataRepository: SearchMetaDataRepository,
        @Autowired val metaDataKeywordRepository: MetaDataKeywordRepository
)

@Repository
interface TestimonialDetailRepository : JpaRepository<TestimonialDetail, String>{

    fun findByTestimonialCategoryName(categoryName: String): List<TestimonialDetail>?

}


@Repository
interface FaqDetailRepository : JpaRepository<Faq, String>{

    fun findByFaqCategoryName(faqCategoryName: String): List<Faq>?

}


@Repository
interface SearchMetaDataRepository : JpaRepository<SearchMetaData, String>{
//    @Query("SELECT s FROM SearchMetaData s JOIN s.metaDataKeywords k WHERE k.keywordValue LIKE %:keyword%")
//    fun findByKeyword(@Param("keyword") keyword: String): List<SearchMetaData>


        @Query("SELECT distinct NEW com.homefirstindia.homefirstwebsite.model.SearchMetaDataDTO(s.id, s.webPageId, s.webPageUrl, s.description, s.title, s.parent) FROM SearchMetaData s JOIN s.metaDataKeywords k WHERE k.keywordValue LIKE %:keyword%")
        fun findByKeyword(@Param("keyword") keyword: String): List<SearchMetaDataDTO>

    fun findByWebPageId(webPageId: String): SearchMetaData?
    fun findByWebPageUrl(webPageUrl: String): SearchMetaData?


}

@Repository
interface MetaDataKeywordRepository : JpaRepository<MetaDataKeyword, String>{
    fun findAllByKeywordValueIn(keywordValues: List<String?>): List<MetaDataKeyword>

}









