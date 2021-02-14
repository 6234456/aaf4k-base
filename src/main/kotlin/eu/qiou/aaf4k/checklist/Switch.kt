package eu.qiou.aaf4k.checklist

import eu.qiou.aaf4k.util.mkString

class Switch(val choices: Map<Item, Node>) : Judgement {
    override fun choice(): List<Item> {
        return choices.keys.toList()
    }

    override fun update(env: Map<Long, Item>): Judgement {
        return Switch(
            choices.mapKeys { if (env.containsKey(it.key.id)) env[it.key.id]!! else it.key }
        )
    }

    override fun judge(env: Map<Long, Item>): Node {
        choices.forEach {
            if (env.containsKey(it.key.id) && +env[it.key.id]!!) {
                return it.value
            }
        }

        throw Exception("Please make a choice.")
    }

    override fun toString(): String {
        return choices.keys.mkString("\n\t", "\t", "")
    }
}