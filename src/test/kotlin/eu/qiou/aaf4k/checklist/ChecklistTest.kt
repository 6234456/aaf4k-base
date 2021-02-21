package eu.qiou.aaf4k.checklist

import eu.qiou.aaf4k.util.mkString
import org.junit.Test

class ChecklistTest {

    @Test
    fun getPool() {
        val ag = Item(123L, "ist AG?")
        val gmbh = Item(124L, "ist GmbH?")
        val persg = Item(125L, "ist PersHG?")
        val hasAR = Item(1241L, "hasAR")
        val ar_rem = Item(1242L, "AR_Vergütung angegeben?")
        val c = Checklist()

        val correct = Node("Correct")
        val wrong = Node("wrong")

        val ar = Node("Vergütung Aufsichtsrat", Dichotomy(ar_rem, correct, wrong))

        val n = Node(
            "Rechtsform", Switch(
                mapOf(
                    ag to ar,
                    gmbh to Node("Has AR?", Dichotomy(hasAR, ar, correct)),
                    persg to correct
                )
            )
        )
        println(
            n.update(
                mapOf(
                    124L to State.FULFILLED,
                    1241L to State.FULFILLED,
                    1242L to State.FULFILLED
                )
            ).mkString("\n\n", "", "")
        )

    }
}