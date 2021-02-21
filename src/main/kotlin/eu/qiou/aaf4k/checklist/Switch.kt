package eu.qiou.aaf4k.checklist

import eu.qiou.aaf4k.util.mkString


/**
 *  choices: link each solution to the next node
 */
class Switch(val choices: Map<Item, Node>) : Judgement {
    override fun choice(): List<Item> {
        return choices.keys.toList()
    }

    override fun update(env: Map<Long, State>): Judgement {
        return Switch(
            choices.mapKeys { if (env.containsKey(it.key.id)) it.key.copy(state = env[it.key.id]!!) else it.key }
        )
    }

    override fun judge(env: Map<Long, State>): Node {
        choices.forEach {
            if (env.containsKey(it.key.id) && +env[it.key.id]!!) {
                return it.value
            }
        }

        throw Exception("Please make a choice:${choices.keys.map { it.id }.mkString()}")
    }

    override fun toString(): String {
        return choices.keys.mkString("\n\t", "\t", "")
    }
}