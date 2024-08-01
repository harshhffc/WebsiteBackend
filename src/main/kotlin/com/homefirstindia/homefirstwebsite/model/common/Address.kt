package com.homefirstindia.homefirstwebsite.model.common

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
class State {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    var id: String? = null

    @Column(nullable = false)
    var name: String? = null

}

@Entity
class City {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    var id: String? = null

    @Column(nullable = false)
    var name: String? = null

    @ManyToOne
    @JoinColumn(name = "state_id")
    var state : State? = null

}
