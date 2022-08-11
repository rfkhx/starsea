import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import top.ntutn.starsea.util.KotlinSerializeUtil
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * 测试json序列化工具工作和配置正确
 */
class JsonSerializeTest {
    @Serializable
    data class TeacherTestBean(val name: String, var age: Int, val title: String = "nothing", @SerialName("phone_number") val phoneNumber: String? = null)

    @Test
    fun testDeserialize() {
        val json = """
            {
                "name": "张三",
                "age": 25,
                "title": "优秀教师"
            }
        """.trimIndent()
        val teacher = KotlinSerializeUtil.json.decodeFromString<TeacherTestBean>(json)
        assertEquals(TeacherTestBean("张三", 25, "优秀教师"), teacher)
    }

    /**
     * 测试json中包含未定义字段时工作正常
     */
    @Test
    fun testMissingField() {
        val json = """
            {
                "name": "张三",
                "age": 25,
                "title": "优秀教师",
                "hobby": "game"
            }
        """.trimIndent()
        val teacher = KotlinSerializeUtil.json.decodeFromString<TeacherTestBean>(json)
        assertEquals(TeacherTestBean("张三", 25, "优秀教师"), teacher)
    }

    @Test
    fun testDefaultValue() {
        val json = """
            {
                "name": "张三",
                "age": 25
            }
        """.trimIndent()
        val teacher = KotlinSerializeUtil.json.decodeFromString<TeacherTestBean>(json)
        assertEquals(TeacherTestBean("张三", 25), teacher)
    }
}