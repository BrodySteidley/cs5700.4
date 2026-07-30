package computer

import exception.MemoryOutOfBoundsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

private class TestMemory(size: Int) : Memory(ByteArray(size))

class MemoryTest {
    @Test
    fun `read returns value at address`() {
        val memory = TestMemory(16)

        memory.write(5u, 42)

        assertEquals(42.toByte(), memory.read(5u))
    }

    @Test
    fun `write stores value at address`() {
        val memory = TestMemory(16)

        memory.write(3u, 99)

        assertEquals(99.toByte(), memory.read(3u))
    }

    @Test
    fun `read throws when address is out of bounds`() {
        val memory = TestMemory(16)

        assertThrows(MemoryOutOfBoundsException::class.java) {
            memory.read(16u)
        }
    }

    @Test
    fun `write throws when address is out of bounds`() {
        val memory = TestMemory(16)

        assertThrows(MemoryOutOfBoundsException::class.java) {
            memory.write(16u, 1)
        }
    }

    @Test
    fun `zero clears all memory`() {
        val memory = TestMemory(8)

        for (i in 0u until 8u) {
            memory.write(i.toUShort(), (i + 1u).toByte())
        }

        memory.zero()

        for (i in 0u until 8u) {
            assertEquals(0.toByte(), memory.read(i.toUShort()))
        }
    }

    @Test
    fun `can read and write last valid address`() {
        val memory = TestMemory(16)

        memory.write(15u, 123)

        assertEquals(123.toByte(), memory.read(15u))
    }

    @Test
    fun `zero works on empty memory`() {
        val memory = TestMemory(0)

        assertDoesNotThrow {
            memory.zero()
        }
    }

    @Test
    fun `address well beyond memory size throws`() {
        val memory = TestMemory(16)

        assertAll(
            {
                assertThrows(MemoryOutOfBoundsException::class.java) {
                    memory.read(UShort.MAX_VALUE)
                }
            },
            {
                assertThrows(MemoryOutOfBoundsException::class.java) {
                    memory.write(UShort.MAX_VALUE, 0)
                }
            }
        )
    }
}