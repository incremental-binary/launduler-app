package kr.ac.kaist.launduler.models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import kr.ac.kaist.launduler.CustomDateDeserializer
import java.util.*

/**
 * Created by kyukyukyu on 02/06/2017.
 */
data class Machine(val serialNum: String,
                   val location: String,
                   val inUse: Boolean,
                   val isBroken: Boolean,
                   val reservations: List<Reservation>)

data class Reservation(val id: Long,
                       val machine: String,
                       @JsonDeserialize(using = CustomDateDeserializer::class) val scheduledAt: Calendar,
                       val userId: String) {
    companion object {
        const val LENGTH_MINUTE = 90
    }

    val endsAt: Calendar by lazy {
        val ret = scheduledAt.clone() as Calendar
        ret.add(Calendar.MINUTE, LENGTH_MINUTE)
        ret
    }
}

/**
 * A place where washing machines are installed.
 */
data class Place(val id: Long,
                 val location: String,
                 val operationTime: String)