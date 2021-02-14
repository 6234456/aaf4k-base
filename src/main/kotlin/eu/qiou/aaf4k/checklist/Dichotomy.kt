package eu.qiou.aaf4k.checklist

data class Dichotomy(val item: Item, val ifTrue: Node, val ifFalse: Node) : Judgement {
    override fun choice(): List<Item> {
        return listOf(item)
    }

    override fun update(env: Map<Long, Item>): Judgement {
        return if (env.containsKey(item.id))
            this.copy(item.copy(state = env[item.id]!!.state))
        else
            this
    }

    override fun judge(env: Map<Long, Item>): Node {

        if (!env.containsKey(item.id)) throw Exception("Incomplete answer for $item")

        return if (+env[item.id]!!) ifTrue else ifFalse
    }

    override fun toString(): String {
        return "${item.desc}\n\t${if (+item) "* Yes\n\t No" else " Yes\n\t * No"}"
    }
}