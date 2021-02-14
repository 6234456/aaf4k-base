package eu.qiou.aaf4k.checklist

data class Node(
    val desc: String, val judgement: Judgement? = null,
    val optionRelation: OptionRelation = OptionRelation.SINGLE
) {
    fun parse(answer: Map<Long, Item>): List<Node> {
        return if (judgement == null)
            listOf(this)
        else
            listOf(this) + judgement.judge(answer).parse(answer)
    }

    fun update(answer: Map<Long, Item>): List<Node> {
        return if (judgement == null)
            listOf(this.copy())
        else
            listOf(this.copy(judgement = judgement.update(answer))) + judgement.judge(answer).update(answer)
    }

    override fun toString(): String {
        return if (judgement == null) "" else "<$desc>\n$judgement"
    }
}

enum class OptionRelation {
    SINGLE,
    MULTIPLE
}