package eu.qiou.aaf4k.checklist

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

        println(gmbh.apply { this.state = State.FULFILLED })

        println(n)

        n.update(
            mapOf(
                124L to Item(124L, "", State.FULFILLED),
                1241L to Item(1241L, "", State.FULFILLED),
                1242L to Item(1242L, "", State.FULFILLED)
            )
        ).forEach { println(it) }

    }
}