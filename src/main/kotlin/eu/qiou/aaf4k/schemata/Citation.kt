package eu.qiou.aaf4k.schemata

class Citation {
    private val list: MutableList<Citations> = mutableListOf()

    operator fun plus(citations: Citations): Citation {
        return this.apply {
            list.add(citations)
        }
    }

    operator fun plus(citations: Citation): Citation {
        return this.apply {
            list.addAll(citations.list)
        }
    }

    operator fun contains(citations: Citation): Boolean {
        if (this.list.size >= citations.list.size) return false

        return citations.list.take(this.list.size).zip(this.list).all {
            it.first == it.second
        }
    }

    override fun toString(): String {
        return list.map { it.toString() }.reduce { acc, s -> "$acc $s" }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Citation) {
            if (list.size == other.list.size) {
                return list.zip(other.list).all { it.first == it.second }
            }
        }

        return false
    }

}