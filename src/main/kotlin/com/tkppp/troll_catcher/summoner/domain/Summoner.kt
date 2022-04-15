package com.tkppp.troll_catcher.summoner.domain

import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.TypeDef
import javax.persistence.*
import org.hibernate.annotations.Type

@Entity
@DynamicInsert
@TypeDef(name="list-int", typeClass = ListArrayType::class)
class Summoner(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(nullable = false)
    val puuid: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var recentMatchId: String? = null,

    @Type(type = "list-int")
    @Column(nullable = true, columnDefinition = "integer[]")
    var analysisResults: List<Int>? = null
) {

}