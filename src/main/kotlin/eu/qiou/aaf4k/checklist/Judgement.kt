package eu.qiou.aaf4k.checklist

interface Judgement {
    fun judge(env: Map<Long, Item>): Node
    fun choice(): List<Item>
    fun update(env: Map<Long, Item>): Judgement
}