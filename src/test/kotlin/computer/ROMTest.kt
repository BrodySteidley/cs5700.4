package computer

import exception.MemoryAccessException
import exception.MemoryOutOfBoundsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ROMTest {

    @Test
    fun `read returns stored value`() {
        val data = byteArrayOf(1, 2, 3, 4)
        val rom = ROM(data)

        assertEquals(1.toByte(), rom.read(0u))
        assertEquals(3.toByte(), rom.read(2u))
    }

    @Test
    fun `write always throws MemoryAccessException`() {
        val rom = ROM(byteArrayOf(1, 2, 3))

        assertThrows(MemoryAccessException::class.java) {
            rom.write(0u, 42)
        }
        assertThrows(MemoryAccessException::class.java) {
            rom.write(1u, 42)
        }
        assertThrows(MemoryAccessException::class.java) {
            rom.write(300u, 42)
        }
    }

    @Test
    fun `write does not modify underlying data`() {
        val data = byteArrayOf(10, 20, 30)
        val rom = ROM(data)

        assertThrows(MemoryAccessException::class.java) {
            rom.write(1u, 99)
        }

        assertEquals(10.toByte(), rom.read(0u))
        assertEquals(20.toByte(), rom.read(1u))
        assertEquals(30.toByte(), rom.read(2u))
    }

    @Test
    fun `read out of bounds throws MemoryOutOfBoundsException`() {
        val rom = ROM(byteArrayOf(1, 2, 3))

        assertThrows(MemoryOutOfBoundsException::class.java) {
            rom.read(3u)
        }
    }

    @Test
    fun `out of bounds write still throws MemoryAccessException`() {
        val rom = ROM(byteArrayOf(1, 2, 3))

        assertThrows(MemoryAccessException::class.java) {
            rom.write(UShort.MAX_VALUE, 1)
        }
    }
}