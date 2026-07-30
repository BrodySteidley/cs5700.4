package computer

import exception.MemoryAccessException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class D5700InputTest {

    @Test
    fun `write always throws MemoryAccessException`() {
        val input = D5700Input()

        assertThrows(MemoryAccessException::class.java) {
            input.write(0u, 0)
        }

        assertThrows(MemoryAccessException::class.java) {
            input.write(7u, 0)
        }

        assertThrows(MemoryAccessException::class.java) {
            input.write(200u, 0)
        }
    }


    @Test
    fun `read accepts valid hexadecimal`() {
        System.setIn(ByteArrayInputStream("7F\n".toByteArray()))

        val input = D5700Input()

        assertEquals(0x7F.toByte(), input.read(0u))
    }

    @Test
    fun `read retries until valid input`() {
        System.setIn(
            ByteArrayInputStream(
                """
                XYZ
                100
                AF
                """.trimIndent().toByteArray()
            )
        )

        val input = D5700Input()

        assertEquals(0xAF.toByte(), input.read(0u))
    }

    @Test
    fun `read accepts lowercase hexadecimal`() {
        System.setIn(ByteArrayInputStream("ff\n".toByteArray()))

        val input = D5700Input()

        assertEquals(0xFF.toByte(), input.read(0u))
    }
}