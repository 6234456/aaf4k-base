package eu.qiou.aaf4k.checklist

class Checklist {
    val pool: MutableList<Node> = mutableListOf()
    val params: MutableMap<Long, State> = mutableMapOf()

    fun next(): Node? {
        return pool.last().judgement?.judge(params)
    }

}

operator fun Checklist.plus(node: Node): Checklist {
    pool.add(node)
    return this
}
