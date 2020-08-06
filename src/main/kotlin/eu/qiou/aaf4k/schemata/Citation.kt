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