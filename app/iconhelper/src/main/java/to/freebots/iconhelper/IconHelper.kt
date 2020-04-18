package to.freebots.iconhelper

class IconHelper {
    // todo add credit to
    // <div>Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
    companion object {

        private val randomIcons = listOf(
            R.drawable.ic_bat,
            R.drawable.ic_cactus,
            R.drawable.ic_cooking_pot,
            R.drawable.ic_death,
            R.drawable.ic_eyeball,
            R.drawable.ic_ghost,
            R.drawable.ic_ghost_blue,
            R.drawable.ic_parchment,
            R.drawable.ic_scientist,
            R.drawable.ic_skull,
            R.drawable.ic_witch_hat
        )

        fun randomEmptyIcon(): Int = randomIcons.random()

        fun attachmentIcon(type: MIME): Int = when (type) {
            MIME.PICTURE -> R.drawable.ic_picture
            MIME.AUDIO -> R.drawable.ic_cassette
            MIME.VIDEO -> R.drawable.ic_video_player
            MIME.OTHER -> R.drawable.ic_attachment
        }

        fun labelIcons(): List<Int> = mutableListOf(
            R.drawable.ic_cd,
            R.drawable.ic_cassette,
            R.drawable.ic_picture,
            R.drawable.ic_video_player
        ).apply {
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