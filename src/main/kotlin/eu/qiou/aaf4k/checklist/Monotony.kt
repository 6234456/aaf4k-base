package eu.qiou.aaf4k.checklist

class Monotony(val node: Node) : Judgement {
    override fun choice(): List<Item> {
        return listOf()
    }

    override fun update(env: Map<Long, Item>): Judgement {
        return this
    }

    override fun judge(env: Map<Long, Item>): Node {
        return node
    }
}