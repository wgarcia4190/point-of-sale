package com.devcorerd.pos.model.entity

import org.joda.time.DateTime

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
data class Printer(var name: String, var device: String, var address: String, val size: String, var creationDate: DateTime,
                   var modificationDate: DateTime)