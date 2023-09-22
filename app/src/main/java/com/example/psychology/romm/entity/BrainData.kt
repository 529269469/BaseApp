package com.example.psychology.romm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brain")
class BrainData {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var reportId: Long = 0
    var number: Int? = null //排序
    var delta: Int? = null
    var theta: Int? = null
    var lowAlpha: Int? = null
    var highAlpha: Int? = null
    var lowBeta: Int? = null
    var highBeta: Int? = null
    var lowGamma: Int? = null
    var midGamma: Int? = null

    constructor(
        reportId: Long,
        number: Int?,
        delta: Int?,
        theta: Int?,
        lowAlpha: Int?,
        highAlpha: Int?,
        lowBeta: Int?,
        highBeta: Int?,
        lowGamma: Int?,
        midGamma: Int?
    ) {
        this.reportId = reportId
        this.number = number
        this.delta = delta
        this.theta = theta
        this.lowAlpha = lowAlpha
        this.highAlpha = highAlpha
        this.lowBeta = lowBeta
        this.highBeta = highBeta
        this.lowGamma = lowGamma
        this.midGamma = midGamma
    }
}

