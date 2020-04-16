package to.freebots.iconhelper

class IconHelper {
    // todo add credit to
    // <div>Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
    companion object {

        private val randomIcons = listOf(
            R.drawable.ic_bat,
            R.drawable.ic_cactus,
            R.drawable.ic_death,
            R.drawable.ic_eyeball,
            R.drawable.ic_ghost,
            R.drawable.ic_ghost_blue,
            R.drawable.ic_scientist,
            R.drawable.ic_skull,
            R.drawable.ic_witch_hat
        )

        fun randomEmptyIcon(): Int = randomIcons.random()

        fun attachmentIcon(type: MIME): Int = when (type) {
            MIME.PICTURE, MIME.AUDIO, MIME.VIDEO, MIME.OTHER -> -1
        }

        fun labelIcons(): List<Int> = mutableListOf(R.drawable.ic_bat)
            .apply {
                addAll(randomIcons)
            }
    }

    enum class MIME {
        PICTURE,
        AUDIO,
        VIDEO,
        OTHER
    }
}