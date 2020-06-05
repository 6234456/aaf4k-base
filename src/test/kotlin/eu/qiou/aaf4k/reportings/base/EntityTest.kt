package eu.qiou.aaf4k.reportings.base

import org.junit.Test

class EntityTest {

    val mu = Entity(1, "Holding (Frankfurt) AG", "AG", "eine Holding AG in DE")

    @Test
    fun toJSON() {
        mu.add(Entity(2, "Trail GmbH", "GmbH", ""), percentage = 0.021)
        mu.add(Entity(3, "Trail1 GmbH", "GmbH", ""), percentage = 0.21)
        print(mu)
    }
}