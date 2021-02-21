package eu.qiou.aaf4k.checklist

interface Judgement {
    fun judge(env: Map<Long, State>): Node
    fun choice(): List<Item>
    fun update(env: Map<Long, State>): Judgement
}