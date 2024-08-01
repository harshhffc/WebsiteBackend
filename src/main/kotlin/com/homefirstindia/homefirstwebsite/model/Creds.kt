package com.homefirstindia.homefirstwebsite.model

import com.homefirstindia.homefirstwebsite.utils.DateTimeUtils.getCurrentDateTimeInIST
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "`Creds`")
class Creds {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    var id: String? = null

    @Column(nullable = false)
    var partnerName: String? = null
    var credType: String? = null
    var username: String? = null
    var password: String? = null
    var memberId: String? = null
    var memberPasscode: String? = null
    var salt: String? = null
    var apiKey: String? = null
    var apiUrl: String? = null

    @ColumnDefault("1")
    var isValid = true

    @ColumnDefault("0")
    var isEncrypted = false

    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime = getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime = getCurrentDateTimeInIST()

}