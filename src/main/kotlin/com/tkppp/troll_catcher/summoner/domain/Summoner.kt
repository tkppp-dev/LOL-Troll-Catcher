package com.tkppp.troll_catcher.summoner.domain

import com.tkppp.troll_catcher.summoner.util.MatchInfo
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.TypeDef
import javax.persistence.*
import org.hibernate.annotations.Type

@Entity
@DynamicInsert
@TypeDef(name="matchInfo-list", typeClass = JsonBinaryType::class)
class Summoner(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val puuid: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var recentMatchId: String? = null,

    @Type(type = "matchInfo-list")
    @Column(nullable = true, columnDefinition = "jsonb")
    var matchInfos: List<MatchInfo>? = null
) {

}