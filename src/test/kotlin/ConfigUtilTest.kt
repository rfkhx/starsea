import top.ntutn.starsea.util.ConfigUtil
import kotlin.test.Test

class ConfigUtilTest {
    @Test
    fun testReadConfig() {
        val config = ConfigUtil.configBean
        assert(config.botToken.isNotBlank())
    }
}